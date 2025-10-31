package com.edw.aop;

import com.edw.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * <pre>
 *  com.edw.aop.SessionLoginAspect
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 31 Oct 2025 15:57
 */
@Component
@Aspect
@Order(1)
public class SessionLoginAspect {

    private HttpSession httpSession;

    @Autowired
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    private JwtUtils jwtUtils;

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Before("execution(public * (@org.springframework.web.bind.annotation.RestController *).*(..))")
    public void checkerSession(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestURL = request.getRequestURI();

        if(null != requestURL && requestURL.trim().length() >= 0){
            boolean isTokenValid = false;
            String myToken = request.getHeader("my_token");
            if(null != myToken && !myToken.trim().isEmpty()){
                if (jwtUtils.validateJwtToken(myToken)) {
                    isTokenValid = true;
                }
            }
            if (!isTokenValid) {
                throw new RuntimeException("Session login habis silakan login kembali");
            }
        }

    }

}
