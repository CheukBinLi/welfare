package com.welfare.fundraising.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.cheuks.bin.original.common.util.ObjectFill;
import com.welfare.core.base.model.JsonMsgModel;

public abstract class AbstractController extends ObjectFill {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

	private static volatile String serverFullPath;
	private static volatile String serverPath;
	private static volatile String contextFullPath;
	private static volatile String contextPath;

	public static String getServerPath(HttpServletRequest request) {
		if (null == serverPath) {
			serverPath = request.getScheme() + "://" + request.getServerName();
			serverFullPath = serverPath + (request.getServerPort() != 80 ? ":" + request.getServerPort() : "");
			contextPath = serverPath + request.getContextPath();
			contextFullPath = serverFullPath + request.getContextPath();
		}
		return serverPath;
	}

	public static String getServerFullPath(HttpServletRequest request) {
		if (null == serverPath) {
			getServerPath(request);
		}
		return serverFullPath;
	}

	public static String getCurrentUrl(HttpServletRequest request) {
		return getCurrentUrl(request, true) + "?" + request.getQueryString();
	}

	public static String getContextPath(HttpServletRequest request, boolean isFullPath) {
		if (null == contextPath)
			getServerPath(request);
		return isFullPath ? contextFullPath : contextPath;
	}

	public static String getCurrentUrl(HttpServletRequest request, boolean isFullPath) {
		return (isFullPath ? getServerFullPath(request) : getServerPath(request)) + request.getRequestURI();
	}

	protected final Map<String, Object> getParams(HttpServletRequest request) {
		Enumeration<String> en = request.getParameterNames();
		Map<String, Object> map = new HashMap<String, Object>();
		String name;
		while (en.hasMoreElements()) {
			name = en.nextElement();
			map.put(name, request.getParameter(name));
		}
		return map;
	}

	protected Map<String, Object> checkPageAndSize(HttpServletRequest request) {
		Map<String, Object> map = getParams(request);
		if (!map.containsKey("page")) {
			map.put("page", -1);
			map.put("size", -1);
		}
		return map;
	}

	protected JsonMsgModel fail(String msg, Throwable e) {
		if (null != e)
			e.printStackTrace();
		return new JsonMsgModel(JsonMsgModel.FAIL, msg);
	}

	protected JsonMsgModel fail(Throwable e) {
		e.printStackTrace();
		return new JsonMsgModel(JsonMsgModel.FAIL, "操作失败");
	}

	protected JsonMsgModel fail() {
		return new JsonMsgModel(JsonMsgModel.FAIL);
	}

	protected JsonMsgModel success(String msg, Object data, Object attachment) {
		return new JsonMsgModel(JsonMsgModel.SUCCESS, msg, data, attachment);
	}

	protected JsonMsgModel success(Object data) {
		return success(null, data, null);
	}

	protected JsonMsgModel success() {
		return success(null, null, null);
	}

	protected ModelAndView exceptionPage(Throwable e) {
		return exceptionPage("操作失败", e);
	}

	protected ModelAndView exceptionPage(String msg, Throwable e, String... withOutParams) {
		if (null != e)
			LOG.error(msg, e);
		return new ModelAndView("forward:/error").addObject("withOutParams", withOutParams).addObject("msg", msg);
	}

	protected ModelAndView forwardPage(String path, Map<String, Object> params) {
		return new ModelAndView("forward:" + path).addAllObjects(params);
	}

	/***
	 * 
	 * @param path
	 * @param params
	 *            key,value,key,value
	 * @return
	 */
	protected ModelAndView forwardPage(String path, Object... params) {
		ModelAndView modelAndView = new ModelAndView("forward:" + path);
		if (params.length % 2 == 0)
			for (int i = 0, len = params.length; i < len; i++) {
				modelAndView.addObject(params[i++].toString(), params[i]);
			}
		return modelAndView;
	}

	protected ModelAndView weiXinAuth(Object state) {
		return forwardPage("forward:/weiXinAuth", "state", state);
	}

	protected List<String> uploadFile(HttpServletRequest request, String savePath) throws IllegalStateException, IOException {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		String realPath = request.getServletContext().getRealPath("/");
		List<String> result = null;
		File file;
		if (commonsMultipartResolver.isMultipart(request)) {
			result = new ArrayList<String>();
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> it = multiRequest.getFileNames();
			MultipartFile multipartFile;
			String fileName = null;
			while (it.hasNext()) {
				multipartFile = multiRequest.getFile(it.next());
				if (multipartFile.getSize() < 1)
					continue;
				fileName = multipartFile.getOriginalFilename();
				fileName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
				fileName = String.format("%s/%s%d%s", savePath, "2nd", System.currentTimeMillis(), fileName);
				result.add(fileName);
				file = new File(realPath + "/" + fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
				multipartFile.transferTo(file);
			}
		}
		return result;
	}

	public String returnPayCallBackMsg(String returnCode, String returnMsg) {
		return "<xml><return_code><![CDATA[" + returnCode + "]]></return_code><return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>";
	}

}
