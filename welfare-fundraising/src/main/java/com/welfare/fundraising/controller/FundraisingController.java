package com.welfare.fundraising.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.welfare.core.base.entity.UnifiedOrder;
import com.welfare.core.base.service.UnifiedOrderService;
import com.welfare.core.contant.WebContant;
import com.welfare.core.fundraising.entity.Fundraising;
import com.welfare.core.fundraising.entity.FundraisingLog;
import com.welfare.core.fundraising.service.FundraisingService;
import com.welfare.fundraising.ConstantObject;
import com.welfare.fundraising.cache.FundraisingRedisCacheHelper;

@Controller
@Scope("prototype")
@RequestMapping("/control/fundraising/")
public class FundraisingController extends AbstractController {

	@Autowired
	private FundraisingService fundraisingService;
	@Autowired
	private UnifiedOrderService unifiedOrderService;
	@Autowired
	private FundraisingRedisCacheHelper fundraisingRedisCacheHelper;

	@RequestMapping(value = { "{path}" }, method = { RequestMethod.GET })
	public ModelAndView basePath(HttpServletRequest request, HttpServletResponse response, @PathVariable("path") String path) throws IOException {
		System.out.println(request.getRequestURI());
		return new ModelAndView("/fundraising/" + path);
	}

	@RequestMapping(value = "fundraisingMain", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getToNext(HttpServletRequest request, HttpServletResponse response) {
		Fundraising fundraising;
		// System.out.println(request.getContextPath());
		try {
			int id = Integer.valueOf(request.getParameter("id"));
			fundraising = fundraisingService.getActivFundraisingByPk(id);
			if (null == fundraising) {
				return exceptionPage("活动过期/没到开始时间。", null, "next", "id");
			}
			Map<String, String> statisticalAndLatestRecord = (Map<String, String>) fundraisingRedisCacheHelper.getFundraisingStatisticalAndLatestRecord(id);
			// // 生成签名
			Map<String, String> sign = ConstantObject.newInstance().getSign(getCurrentUrl(request));

			return new ModelAndView("/fundraising/fundraisingMain").addObject("fundraising", fundraising).addObject("sign", sign).addObject("title", fundraising.getTitle()).addObject("statisticalAndLatestRecord", statisticalAndLatestRecord);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@RequestMapping(value = "edit/{next}", method = { RequestMethod.GET })
	public ModelAndView editNext(HttpServletRequest request, HttpServletResponse response, @PathVariable("next") String next) {
		Fundraising fundraising;
		try {
			fundraising = fundraisingService.getByPk(Integer.valueOf(request.getParameter("id")));
			return new ModelAndView("/fundraising/" + next).addObject("fundraising", fundraising);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@RequestMapping(value = "get/{id}/{next}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") int id, @PathVariable("next") String next) {
		Fundraising fundraising;
		try {
			fundraising = fundraisingService.getByPk(id);
			// // 生成签名
			Map<String, String> sign = ConstantObject.newInstance().getSign(getCurrentUrl(request));
			request.getSession().setAttribute(WebContant.SIGN, sign);
			return new ModelAndView("/fundraising/" + next).addObject("fundraising", fundraising).addObject("sign", sign);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@RequestMapping(value = "/fundraisingResult", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView fundraisingResult(HttpServletRequest request, HttpServletResponse response) {
		Fundraising fundraising;
		// , @PathVariable("fundraisingId") int fundraisingId, @PathVariable("fundraisingLogId") int fundraisingLogId
		try {
			int fundraisingId = Integer.valueOf(request.getParameter("fundraising"));
			int fundraisingLogId = Integer.valueOf(request.getParameter("fundraisingLog"));
			fundraising = fundraisingService.getByPk(fundraisingId);
			// // 生成签名 10分钟过期
			Map<String, String> sign = ConstantObject.newInstance().getSign(getCurrentUrl(request), 0, true);
			request.getSession().setAttribute(WebContant.SIGN, sign);
			return new ModelAndView("/fundraising/fundraisingResult").addObject("fundraising", fundraising).addObject("fundraisingLogId", fundraisingLogId).addObject("sign", sign);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "get", method = { RequestMethod.GET })
	public Object get(HttpServletRequest request, HttpServletResponse response) {
		Fundraising fundraising;
		try {
			int id = Integer.valueOf(request.getParameter("id"));
			fundraising = fundraisingService.getByPk(id);
			return new ModelAndView("fundraising").addObject("fundraising", fundraising);
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@RequestMapping(value = "list/{next}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getToNext(HttpServletRequest request, HttpServletResponse response, @PathVariable("next") String next) {
		List<Fundraising> fundraisings = null;
		try {
			fundraisings = fundraisingService.getList(null, false, -1, -1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new ModelAndView("/fundraising/" + next).addObject("fundraisings", fundraisings).addObject("serverUrl", getContextPath(request, false));
	}

	@RequestMapping(value = "put/{next}", method = { RequestMethod.POST })
	public ModelAndView put(HttpServletRequest request, HttpServletResponse response, @PathVariable("next") String next) {
		Map<String, Object> params = getParams(request);
		Fundraising fundraising = new Fundraising();
		try {
			fundraising = fillObject(fundraising, params);
			fundraisingService.update(fundraising);
			return new ModelAndView(next).addObject("fundraising", fundraising);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "put", method = { RequestMethod.POST })
	public Object put(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = getParams(request);
		Fundraising fundraising;
		String id = request.getParameter("id");
		try {
			if (null == id || id.length() < 1) {
				fundraising = new Fundraising();
				fundraising = fillObject(fundraising, params);
				List<String> uploadFile = uploadFile(request, "upload");
				if (!uploadFile.isEmpty())
					fundraising.setThemeUrl(uploadFile.get(0));
				fundraisingService.save(fundraising);
			} else {
				List<String> uploadFile = uploadFile(request, "upload");
				if (!uploadFile.isEmpty())
					params.put("themeUrl", uploadFile.get(0));
				fundraisingService.update(Integer.valueOf(id), params);
			}
			return success();
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@RequestMapping(value = "delete/{next}", method = { RequestMethod.POST })
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, @PathVariable("next") String next) {
		try {
			Fundraising fundraising = new Fundraising(Integer.valueOf(request.getParameter("id")));
			fundraisingService.delete(fundraising);
			return new ModelAndView(next).addObject("fundraising", fundraising);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "delete", method = { RequestMethod.DELETE })
	public Object delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			Fundraising fundraising = new Fundraising(Integer.valueOf(request.getParameter("id")));
			fundraisingService.delete(fundraising);
			return success();
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "createFundraisingOrder", method = { RequestMethod.POST })
	public Object createFundraisingOrder(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = getParams(request);
		Map<String, Object> result = null;
		UnifiedOrder unifiedOrder = new UnifiedOrder();
		FundraisingLog fundraisingLog = new FundraisingLog();
		try {
			unifiedOrder = fillObject(unifiedOrder, params);
			fundraisingLog = fillObject(fundraisingLog, params);

			ConstantObject contantObject = ConstantObject.newInstance();
			DateTime now = new DateTime();
			String spBillCreateIP = "10.0.0.1";
			String appId = contantObject.getAppId();
			String mchId = contantObject.getMchId();
			String apiKey = contantObject.getAipKey();
			String body = "ABC";
			// <![CDATA[筹款活动]]>
			String attach = "fundraising:" + Integer.toString(fundraisingLog.getId());
			String timeStart = now.toString("yyyyMMddHHmmss");
			String timeExpire = now.plusMinutes(10).toString("yyyyMMddHHmmss");
			String openID = unifiedOrder.getWeiXinInfoId();
			String notifyUrl = getContextPath(request, false) + "/control/unifiedOrder/weixinUnifiedOrderCallBack";
			double amount = contantObject.isDevelop() ? 0.01 : unifiedOrder.getPayAmount();

			result = unifiedOrderService.createFundraisingOrder(unifiedOrder, fundraisingLog, appId, mchId, apiKey, body, attach, amount, spBillCreateIP, timeStart, timeExpire, notifyUrl, "no_credit", openID);
		} catch (Throwable e) {
			return fail(e);
		}
		return success(result);
	}

	@ResponseBody
	@RequestMapping(value = "getFundraisingLogLatestRecord", method = { RequestMethod.GET })
	public Object getFundraisingLogLatestRecord(HttpServletRequest request, HttpServletResponse response) {
		try {
			String record = fundraisingRedisCacheHelper.getFundraisingLatestRecord(Integer.valueOf(request.getParameter("fundraisingId")));
			return success(record);
		} catch (Throwable e) {
			return fail(e);
		}
	}

}
