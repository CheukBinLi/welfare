package com.welfare.fundraising.filter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.welfare.core.contant.WebContant;

public class LoginFilter extends OncePerRequestFilter {

	private final Set<String> except = new CopyOnWriteArraySet<String>();
	private String[] regular;
	private boolean isDebug;

	private final static Logger LOG = LoggerFactory.getLogger(LoginFilter.class);

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		String tempDebug = this.getFilterConfig().getInitParameter("debug");
		if (null != tempDebug)
			isDebug = Boolean.valueOf(tempDebug);
		regular = ("^" + this.getFilterConfig().getInitParameter("regular").replace("*", ".*").replace(".*.*", ".*") + "$").split(",");
		String exceptStr = this.getFilterConfig().getInitParameter("except");
		if (null == exceptStr)
			return;
		String[] excpts = exceptStr.split(",");
		for (String str : excpts)
			except.add(str);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String path = request.getServletPath();
		if (LOG.isDebugEnabled())
			LOG.debug(path);
		if (isDebug || except.contains(path) || (null != regular && matches(path))) {
			filterChain.doFilter(request, response);
			return;
		}
		Object tempWeiXinInfo = request.getSession().getAttribute(WebContant.OPEN_ID);
		if (null == tempWeiXinInfo) {
			RequestDispatcher rd = request.getRequestDispatcher("/weiXinAuth?" + request.getQueryString());
			rd.forward(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}

	private boolean matches(String path) {
		if (null != regular)
			for (String str : regular) {
				if (path.matches(str))
					return true;
			}
		return false;
	}

}
