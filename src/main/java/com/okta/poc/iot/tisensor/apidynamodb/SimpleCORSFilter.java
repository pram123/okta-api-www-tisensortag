package com.okta.poc.iot.tisensor.apidynamodb;

/**
 * Created by pram on 8/6/17.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

    public SimpleCORSFilter() {
        log.info("SimpleCORSFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        //response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin http://localhost:8100"));
        //response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin 'http://localhost:8100'"));
        //response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin '*'"));
        //response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin *"));

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Test, Content-Type, Accept, X-Requested-With, remember-me, Authorization");
        response.setHeader("Access-Control-Request-Method","GET, POST ,DELETE, OPTIONS");
        // Access-Control-Request-Method
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
