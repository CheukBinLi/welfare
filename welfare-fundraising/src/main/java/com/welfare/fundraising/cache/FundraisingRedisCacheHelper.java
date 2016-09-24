package com.welfare.fundraising.cache;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cheuks.bin.original.common.util.CollectionUtil;
import com.google.gson.Gson;
import com.welfare.core.fundraising.dto.FundraisingLogStatisticalDto;
import com.welfare.core.fundraising.dto.FundraisingLogWeixinDto;
import com.welfare.core.fundraising.entity.Fundraising;
import com.welfare.core.fundraising.entity.FundraisingLog;
import com.welfare.core.fundraising.service.FundraisingLogService;
import com.welfare.fundraising.ConstantObject;

@Component
public class FundraisingRedisCacheHelper {

	@Autowired
	private FundraisingLogService fundraisingLogService;

	private final String getFundraisingStatisticalField(int id) {
		return "FundraisingStatistical:" + id;
	}

	private final String getFundraisingLatestRecordField(int id) {
		return "FundraisingLatestRecord:" + id;
	}

	private final String getStatisticalTotalAmountField = "totalAmount";
	private final String getStatisticalCountsField = "counts";
	private final String getStatisticalField = "statistical";
	private final String getRecordField = "record";

	public List<String> initRedisData(int id) throws Throwable {
		List<String> json = null;
		if (!ConstantObject.redisFactory.exists(getFundraisingStatisticalField(id))) {
			Gson gson = new Gson();
			List<FundraisingLogWeixinDto> fundraisingLogWeixinDtos = fundraisingLogService.getFundraisingLogWeixinDtoList(id, 0, 30);
			FundraisingLogStatisticalDto statistical = fundraisingLogService.getFundraisingLogStatisticalDto(id);
			Map<String, Object> o = CollectionUtil.newInstance().toMap("record", fundraisingLogWeixinDtos, "statistical", statistical);
			json = Arrays.asList(gson.toJson(statistical), gson.toJson(fundraisingLogWeixinDtos));
			ConstantObject.redisFactory.evalSha(ConstantObject.redisLua.getSha("FundraisingInitStatisticalAndLatestRecord"), 2, getFundraisingStatisticalField(id), getFundraisingLatestRecordField(id), gson.toJson(o));
		}
		return json;
	}

	public String getFundraisingLatestRecord(int id) throws Throwable {
		String result;
		List<String> tempResult = initRedisData(id);
		if (null == tempResult || tempResult.isEmpty())
			result = (String) ConstantObject.redisFactory.evalSha(ConstantObject.redisLua.getSha("FundraisingGetLatestRecord"), 1, getFundraisingLatestRecordField(id));
		else
			result = tempResult.get(1);
		return result;
	}

	/***
	 * 
	 * @param id
	 * @return map<String,String>
	 *         <p>
	 *         ----statistical
	 *         <p>
	 *         ----record
	 * @throws Throwable
	 */
	public Map<String, ?> getFundraisingStatisticalAndLatestRecord(int id) throws Throwable {
		List<String> result = initRedisData(id);
		if (null == result) {
			result = (List<String>) ConstantObject.redisFactory.evalSha(ConstantObject.redisLua.getSha("FundraisingGetStatisticalAndLatestRecord"), 2, getFundraisingStatisticalField(id), getFundraisingLatestRecordField(id));
		}
		return CollectionUtil.newInstance().toMap(getStatisticalField, result.get(0), getRecordField, result.get(1));
	}

	public void setFundraisingStatisticalAndLatestRecord(Fundraising fundraising, FundraisingLog fundraisingLog) throws Throwable {
		// --ARGV :{record:{record:广告数据,size:广告显示数据量},statistical:{totalAmount:总数}}
		int id = fundraising.getId();
		List<String> result = initRedisData(id);
		if (null != result && !result.isEmpty())
			return;
		Map<String, Object> record = CollectionUtil.newInstance().toMap("record", fundraisingLogService.getFundraisingLogWeixinDto(fundraisingLog.getId()), "size", 30);
		Map<String, Object> statistical = CollectionUtil.newInstance().toMap("totalAmount", fundraisingLog.getPayAmount());
		Gson gson = new Gson();
		String json = gson.toJson(CollectionUtil.newInstance().toMap("record", record, "statistical", statistical));
		ConstantObject.redisFactory.evalSha(ConstantObject.redisLua.getSha("FundraisingStatisticalAndUpdateLatestRecord"), 2, getFundraisingStatisticalField(id), getFundraisingLatestRecordField(id), json);
	}

}
