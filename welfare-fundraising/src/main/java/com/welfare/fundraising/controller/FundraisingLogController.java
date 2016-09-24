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

import com.welfare.core.fundraising.entity.FundraisingLog;
import com.welfare.core.fundraising.service.FundraisingLogService;

@Controller
@Scope("prototype")
@RequestMapping("/fundraisingLog/")
public class FundraisingLogController extends AbstractController {

	@Autowired
	private FundraisingLogService fundraisingLogService;

	@RequestMapping(value = { "{path}" }, method = { RequestMethod.GET })
	public ModelAndView basePath(HttpServletRequest request, HttpServletResponse response, @PathVariable("path") String path) throws IOException {
		return new ModelAndView(path);
	}

	@RequestMapping(value = "get/{id}/to/{next}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getToNext(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") int id, @PathVariable("next") String next) {
		FundraisingLog fundraising;
		try {
			fundraising = fundraisingLogService.getByPk(id);
			return new ModelAndView(next).addObject("fundraising", fundraising);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@RequestMapping(value = "get/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") int id) {
		FundraisingLog fundraisingLog;
		try {
			fundraisingLog = fundraisingLogService.getByPk(id);
			return new ModelAndView("fundraisingLog").addObject("fundraisingLog", fundraisingLog);
		} catch (Throwable e) {
			return exceptionPage(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "get", method = { RequestMethod.GET })
	public Object get(HttpServletRequest request, HttpServletResponse response) {
		FundraisingLog fundraisingLog;
		try {
			int id = Integer.valueOf(request.getParameter("id"));
			fundraisingLog = fundraisingLogService.getByPk(id);
			return new ModelAndView("fundraisingLog").addObject("fundraisingLog", fundraisingLog);
		} catch (Throwable e) {
			return fail(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "put", method = { RequestMethod.POST })
	public Object put(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = getParams(request);
		params.put("weiXinInfoId", request.getSession().getAttribute("OPEN_ID"));
		try {
			fundraisingLogService.update(Integer.valueOf(request.getParameter("id")), params);
			return success();
		} catch (Throwable e) {
			return fail(e);
		}
	}

}
