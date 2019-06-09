package com.dream.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限拦截器
 */
public class JurisdictionInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(JurisdictionInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		logger.info("进入MVC拦截器。请求路径:"+requestURI);
//		if (requestURI.contains("login")) {
//			return true;
//		}
		/**获取登录信息*/
//		UserSessionInfo usrSessionInfo = (UserSessionInfo) request.getSession().getAttribute(UserSessionInfo.SessionKey);
//		if (usrSessionInfo == null) {
//			logger.info("用户未登录，跳转到登录页面!");
//			response.sendRedirect(request.getContextPath() +"/login");
//			return false;
//		}
//		if (requestURI.contains("login")) {
//				HandlerMethod handlerMethod = (HandlerMethod) handler;
//				Method method = handlerMethod.getMethod();
				/*获取用户拥有所有权限ID*/
//				Set<Long> userRules = usrSessionInfo.getAuthorities();
//				if (method.isAnnotationPresent(AuthAnno.class)) {
//					AuthAnno authAnno = method.getAnnotation(AuthAnno.class);
//					/*获取方法权限ID*/
//					BackendAuth[] rules = authAnno.rules();
//					if (rules == null || rules.length < 1) {
//						return true;
//					}
//					/**如果系统管理员登录-放行*/
//					if(BackendCodeCommon.ADMIN_ID.equals(usrSessionInfo.getId())) {
//						return true;
//					}
//					if (userRules == null || userRules.size() < 1) {
//						// handlerAccessError(handlerMethod, response);
//						logger.info("权限拒绝！该用户无权限!用户Id：" + usrSessionInfo.getId());
//						response.sendRedirect(request.getContextPath() + "/error/noAuth");
//						return false;
//					}
//					for (int i = 0; i < rules.length; i++) {
//						if (!userRules.contains(rules[i].getValue())) {
//							// handlerAccessError(handlerMethod, response);
//							logger.info("该用户无权限访问，权限拒绝！用户Id：" + usrSessionInfo.getId() + ";权限ID：" + rules[i].getValue());
//							response.sendRedirect(request.getContextPath() + "/error/noAuth");
//							return false;
//						}
//					}
//					return true;
//				}
//		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

	}

	private void handlerAccessError(HandlerMethod handlerMethod, HttpServletResponse resp) throws IOException {
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter writer = resp.getWriter();
		writer.write("{'温馨提示':'当前用户无权限！'}");
		writer.flush();
		writer.close();
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
