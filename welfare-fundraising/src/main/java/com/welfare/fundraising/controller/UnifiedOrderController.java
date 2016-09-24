package com.welfare.fundraising.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.common.api.weixin.common.XMLParser;
import com.welfare.core.base.entity.UnifiedOrder;
import com.welfare.core.base.service.UnifiedOrderService;
import com.welfare.core.fundraising.entity.Fundraising;
import com.welfare.core.fundraising.entity.FundraisingLog;
import com.welfare.core.util.Task;
import com.welfare.core.util.TaskScheduling;
import com.welfare.fundraising.ConstantObject;
import com.welfare.fundraising.cache.FundraisingRedisCacheHelper;

@Controller
@Scope("prototype")
@RequestMapping("/control/unifiedOrder/")
public class UnifiedOrderController extends AbstractController {

	@Autowired
	private UnifiedOrderService unifiedOrderService;
	@Autowired
	private FundraisingRedisCacheHelper fundraisingRedisCacheHelper;

	private static final Logger LOG = LoggerFactory.getLogger(UnifiedOrderController.class);

	@RequestMapping(value = { "{path}" }, method = { RequestMethod.GET })
	public ModelAndView basePath(HttpServletRequest request, HttpServletResponse response, @PathVariable("path") String path) throws IOException {
		return new ModelAndView(path);
	}

	@RequestMapping(value = "get/{id}/to/{next}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getToNext(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id, @PathVariable("next") String next) {
		UnifiedOrder fundraising;
		try {
			fundraising = unifiedOrderService.getByPk(id);
			return new ModelAndView(next).addObject("fundraising", fundraising);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@RequestMapping(value = "get/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		UnifiedOrder unifiedOrder;
		try {
			unifiedOrder = unifiedOrderService.getByPk(id);
			return new ModelAndView("unifiedOrder").addObject("unifiedOrder", unifiedOrder);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "get", method = { RequestMethod.GET })
	public Object get(HttpServletRequest request, HttpServletResponse response) {
		UnifiedOrder unifiedOrder;
		try {
			String id = request.getParameter("id");
			unifiedOrder = unifiedOrderService.getByPk(id);
			return new ModelAndView("unifiedOrder").addObject("unifiedOrder", unifiedOrder);
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@RequestMapping(value = "put/{next}", method = { RequestMethod.POST })
	public ModelAndView put(HttpServletRequest request, HttpServletResponse response, @PathVariable("next") String next) {
		Map<String, Object> params = getParams(request);
		UnifiedOrder unifiedOrder = new UnifiedOrder();
		try {
			unifiedOrder = fillObject(unifiedOrder, params);
			unifiedOrderService.update(unifiedOrder);
			return new ModelAndView(next).addObject("unifiedOrder", unifiedOrder);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "put", method = { RequestMethod.POST })
	public Object put(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = getParams(request);
		UnifiedOrder unifiedOrder = new UnifiedOrder();
		try {
			unifiedOrder = fillObject(unifiedOrder, params);
			unifiedOrderService.update(unifiedOrder);
			return success(unifiedOrder);
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "weixinUnifiedOrderCallBack", method = { RequestMethod.POST, RequestMethod.GET })
	public void weixinUnifiedOrderCallBack(HttpServletRequest request, HttpServletResponse response) {
		if (LOG.isDebugEnabled())
			LOG.debug("微信回调");
		try {
			// 回调
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			// 读取流
			InputStream in = request.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.close();
			in.close();
			String xml = new String(out.toByteArray(), "utf-8");
			Map<String, Object> returnBody = XMLParser.getMapFromXML(xml);
			if (LOG.isDebugEnabled())
				LOG.debug(xml);

			String return_code = (String) returnBody.get("return_code");
			String return_msg = (String) returnBody.get("return_msg");
			String appid = (String) returnBody.get("appid");
			String mch_id = (String) returnBody.get("mch_id");
			String device_info = (String) returnBody.get("device_info");// 终端，没记录
			String result_code = (String) returnBody.get("result_code");// SUCCESS/FAIL
			String err_code = (String) returnBody.get("err_code");// 错误返回的信息描述
			String err_code_des = (String) returnBody.get("err_code_des");// 错误返回的信息描述
			String total_fee = (String) returnBody.get("total_fee");// 订单总金额，单位为分
			final String out_trade_no = (String) returnBody.get("out_trade_no");// 订单号 
			String attach = (String) returnBody.get("attach");

			if (ConstantObject.newInstance().getMchId().equals(mch_id) && ConstantObject.newInstance().getAppId().equals(appid)) {
				// 签名通过,证书没保存不校验
				if ("SUCCESS".equals(result_code)) {
					// 回城回调
					// 根据attach分配适配器/异步任务
					// 异步操作(1分钟后运行)
					TaskScheduling.newInstance().addTask(new Task(30, 0, false) {
						@Override
						public void action() {
							try {
								FundraisingLog fundraisingLog = unifiedOrderService.executeWeixinCallByFundraisingOrder(out_trade_no, null);
								if (LOG.isDebugEnabled())
									LOG.debug("异步任务-订单号：" + out_trade_no);
								if (null != fundraisingLog)
									fundraisingRedisCacheHelper.setFundraisingStatisticalAndLatestRecord(new Fundraising(fundraisingLog.getFundraisingId()), fundraisingLog);
							} catch (Throwable e) {
								LOG.error(null, e);
							}
						}
					});
				} else {
					LOG.error("支付失败:err_code" + err_code + " err_code_des:" + err_code_des);
				}
				response.getWriter().write(returnPayCallBackMsg(return_code, return_msg));
			}
		} catch (Throwable e) {
			LOG.error(null, e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "weixinUnifiedOrderCallBackTest", method = { RequestMethod.POST, RequestMethod.GET })
	public void weixinUnifiedOrderCallBackTest(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		final String out_trade_no = "2150438057862909952";// 订单号 

		// 回城回调
		// 根据attach分配适配器/异步任务
		// 异步操作(1分钟后运行)
		TaskScheduling.newInstance().addTask(new Task(30, 0, false) {
			@Override
			public void action() {
				try {
					FundraisingLog fundraisingLog = unifiedOrderService.executeWeixinCallByFundraisingOrder(out_trade_no, null);
					if (LOG.isDebugEnabled())
						LOG.debug("异步任务-订单号：" + out_trade_no);
					if (null != fundraisingLog)
						fundraisingRedisCacheHelper.setFundraisingStatisticalAndLatestRecord(new Fundraising(fundraisingLog.getFundraisingId()), fundraisingLog);
				} catch (Throwable e) {
					LOG.error(null, e);
				}
			}
		});
	}

}
