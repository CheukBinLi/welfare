package com.welfare.fundraising.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.welfare.core.base.entity.WeiXinInfo;
import com.welfare.core.base.service.WeiXinInfoService;

@Controller
@Scope("prototype")
@RequestMapping("/weiXinInfo/")
public class WeiXinInfoController extends AbstractController {

	@Autowired
	private WeiXinInfoService weiXinInfoService;

	@RequestMapping(value = { "{path}" }, method = { RequestMethod.GET })
	public ModelAndView basePath(HttpServletRequest request, HttpServletResponse response, @PathVariable("path") String path) throws IOException {
		return new ModelAndView(path);
	}

	@RequestMapping(value = "get/{id}/to/{next}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getToNext(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id, @PathVariable("next") String next) {
		WeiXinInfo fundraising;
		try {
			fundraising = weiXinInfoService.getByPk(id);
			return new ModelAndView(next).addObject("fundraising", fundraising);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@RequestMapping(value = "get/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		WeiXinInfo weiXinInfo;
		try {
			weiXinInfo = weiXinInfoService.getByPk(id);
			return new ModelAndView("weiXinInfo").addObject("weiXinInfo", weiXinInfo);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "get", method = { RequestMethod.GET })
	public Object get(HttpServletRequest request, HttpServletResponse response) {
		WeiXinInfo weiXinInfo;
		try {
			String id = request.getParameter("id");
			weiXinInfo = weiXinInfoService.getByPk(id);
			return new ModelAndView("weiXinInfo").addObject("weiXinInfo", weiXinInfo);
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@RequestMapping(value = "put/{next}", method = { RequestMethod.POST })
	public ModelAndView put(HttpServletRequest request, HttpServletResponse response, @PathVariable("next") String next) {
		Map<String, Object> params = getParams(request);
		WeiXinInfo weiXinInfo = new WeiXinInfo();
		try {
			weiXinInfo = fillObject(weiXinInfo, params);
			weiXinInfoService.update(weiXinInfo);
			return new ModelAndView(next).addObject("weiXinInfo", weiXinInfo);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "put", method = { RequestMethod.PUT })
	public Object put(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = getParams(request);
		WeiXinInfo weiXinInfo = new WeiXinInfo();
		try {
			weiXinInfo = fillObject(weiXinInfo, params);
			weiXinInfoService.update(weiXinInfo);
			return success();
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@RequestMapping(value = "delete/{next}", method = { RequestMethod.POST })
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, @PathVariable("next") String next) {
		try {
			WeiXinInfo weiXinInfo = new WeiXinInfo(request.getParameter("id"));
			weiXinInfoService.delete(weiXinInfo);
			return new ModelAndView(next).addObject("weiXinInfo", weiXinInfo);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "delete", method = { RequestMethod.DELETE })
	public Object delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			WeiXinInfo weiXinInfo = new WeiXinInfo(request.getParameter("id"));
			weiXinInfoService.delete(weiXinInfo);
			return success();
		} catch (Throwable e) {
			return fail(e);
		}
	}

}
