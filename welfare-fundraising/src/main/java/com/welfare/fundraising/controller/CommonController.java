package com.welfare.fundraising.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.common.api.weixin.auth.AuthUtil;
import com.common.api.weixin.user.UserUtil;
import com.welfare.core.base.entity.WeiXinInfo;
import com.welfare.core.base.service.WeiXinInfoService;
import com.welfare.core.contant.WebContant;
import com.welfare.fundraising.ConstantObject;

@Controller
@Scope("prototype")
@RequestMapping({ "/*", "/*/**", "/", "" })
public class CommonController extends AbstractController {

	private final static Logger LOG = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private WeiXinInfoService weiXinInfoService;

	@Autowired
	private FreeMarkerViewResolver freeMarkerViewResolver;

	@RequestMapping({ "{path}" })
	public ModelAndView basePath(@PathVariable("path") String path, HttpServletRequest request, HttpServletResponse resopnse) {
		return new ModelAndView(path);
	}

	@RequestMapping({ "tokenTest" })
	public void tokenTest(HttpServletRequest request, HttpServletResponse resopnse) throws IOException {
		resopnse.getWriter().write(request.getParameter("echostr"));
	}

	@RequestMapping({ "error" })
	public ModelAndView errorNext(HttpServletRequest request, HttpServletResponse resopnse) throws IOException {
		Object withOutParams = request.getAttribute("withOutParams");
		if (null != withOutParams)
			for (String params : (String[]) withOutParams) {
				request.removeAttribute(params);
			}
		request.removeAttribute("withOutParams");
		return new ModelAndView("/error");
	}

	@RequestMapping({ "weiXinAuth" })
	public ModelAndView weiXinAuth(HttpServletRequest request, HttpServletResponse resopnse) throws IOException {
		// System.out.println(getContextPath(request, false));
		String state = request.getParameter("state");
		String id = request.getParameter("id");
		if (null != request.getSession().getAttribute(WebContant.OPEN_ID)) {
			return new ModelAndView("redirect:/control/fundraising/fundraisingMain?id=" + (null == state ? id : state));
		}
		if (LOG.isDebugEnabled())
			LOG.debug("登录授权");
		String addId = ConstantObject.newInstance().getAppId();
		return new ModelAndView("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + addId + "&redirect_uri=" + URLEncoder.encode(getContextPath(request, false)) + "%2Fauthorization&response_type=code&scope=snsapi_userinfo" + (null == state ? "" : "&state=" + state) + "#wechat_redirect");
	}

	@RequestMapping({ "authorization" })
	public ModelAndView authorization(HttpServletRequest request, HttpServletResponse resopnse) throws Throwable {
		request.setCharacterEncoding("utf-8");
		// 用户同意授权后，能获取到code
		String code = request.getParameter("code");

		ConstantObject constantObject = ConstantObject.newInstance();

		String appId = constantObject.getAppId();
		String appSecret = constantObject.getAppSecret();
		String openId;
		String accessToken;

		String state = request.getParameter("state");
		String id = request.getParameter("id");
		if (null == code || "".equals(code)) {
			return new ModelAndView("forward:weiXinAuth");
		}
		// 用户token，和openid
		Map<String, String> authToken = AuthUtil.getWebLoginAuth(appId, appSecret, code);
		if (LOG.isDebugEnabled())
			LOG.debug("authToken:" + authToken);
		openId = authToken.get("openId");
		accessToken = authToken.get("accessToken");
		if (null == openId || "".equals(openId)) {
			return new ModelAndView("forward:weiXinAuth");
		}
		// 完成登录
		// 读取信息
		WeiXinInfo weiXinInfo = weiXinInfoService.getByPk(openId);
		if (null == weiXinInfo) {
			// 用户信息
			Map<String, String> userInfo = UserUtil.getUserInfo(accessToken, openId);
			weiXinInfo = new WeiXinInfo(openId, userInfo.get("nickname"), userInfo.get("headImgUrl"), null, new Date());
			weiXinInfoService.save(weiXinInfo);
		}
		request.getSession().setAttribute(WebContant.OPEN_ID, openId);
		return new ModelAndView("redirect:/control/fundraising/fundraisingMain?id=" + (null == state ? id : state));

	}

	public static void main(String[] args) {
		String a = "asdf/resource/asdf/asdf";
		System.err.println(a.matches("^.*/resource/.*$"));
		System.err.println(a.matches("^.*/resource/.*$"));
		System.err.println("**/resource/**".replace("*", ".*").replace(".*.*", ".*"));
	}

}
