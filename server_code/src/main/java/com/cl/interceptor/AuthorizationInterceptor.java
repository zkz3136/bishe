package com.cl.interceptor;

import java.io.PrintWriter;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;

import com.cl.annotation.IgnoreAuth;
import com.cl.utils.JwtUtils;
import com.cl.utils.R;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String LOGIN_TOKEN_KEY = "Token";

    @Autowired
    private JwtUtils jwtUtils;
    
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		//支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,request-source,Token, Origin,imgType, Content-Type, cache-control,postman-token,Cookie, Accept,authorization");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
	// 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
	if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
        	response.setStatus(HttpStatus.OK.value());
            return false;
        }
        
        IgnoreAuth annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }

        //从header中获取token
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        
        /**
         * 不需要验证权限的方法直接放过
         */
        if(annotation!=null) {
        	return true;
        }

        Claims claims = jwtUtils.parseClaims(token);
        if (claims != null) {
            Long userId = toLong(claims.get("userId"));
            String username = toStringOrNull(claims.get("username"));
            String role = toStringOrNull(claims.get("role"));
            String tableName = toStringOrNull(claims.get("tableName"));

            if (userId != null && StringUtils.isNotBlank(role)) {
                String normalizedTableName;
                if ("管理员".equals(role)) {
                    normalizedTableName = "admin";
                } else if ("用户".equals(role)) {
                    normalizedTableName = "user";
                } else if ("员工".equals(role)) {
                    normalizedTableName = "staff";
                } else {
                    normalizedTableName = tableName;
                }
                request.getSession().setAttribute("userId", userId);
                request.getSession().setAttribute("role", role);
                request.getSession().setAttribute("tableName", normalizedTableName);
                request.getSession().setAttribute("username", username);
                return true;
            }
        }
        
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
		    writer = response.getWriter();
		    writer.print(JSONObject.toJSONString(R.error(401, "登录状态失效，请重新登录！")));
		} finally {
		    if(writer != null){
		        writer.close();
		    }
		}
//				throw new EIException("登录状态失效，请重新登录！", 401);
		return false;
    }

    private static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }

    private static String toStringOrNull(Object value) {
        if (value == null) {
            return null;
        }
        String str = String.valueOf(value);
        return StringUtils.isBlank(str) ? null : str;
    }
}
