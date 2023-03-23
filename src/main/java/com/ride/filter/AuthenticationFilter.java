package com.ride.filter;

import com.ride.constants.Constants;
import com.ride.constants.ResponseCodeMapping;
import com.ride.entity.UserDetails;
import com.ride.exception.TokenMissingException;
import com.ride.service.IUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class AuthenticationFilter extends GenericFilterBean {

    @Autowired
    private IUserService userService;

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String path = req.getServletPath();
        final String token = extractToken(req);
        MDC.put(Constants.MDC_ID, token);

        if(path.contains(Constants.SIGNUP_URL) || path.contains(Constants.LOGIN_URL)){
            log.info("Signup or Login authentication is not required");
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            String key = req.getHeader(Constants.HEADER_AUTHORIZATION) == null ? "" : req.getHeader(Constants.HEADER_AUTHORIZATION);
            log.info("Authorization key: " + key);
            if (key == null || !key.startsWith(Constants.HEADER_AUTH_PREFIX)) {
                throw new TokenMissingException(ResponseCodeMapping.TOKEN_NOT_FOUND.getCode(), ResponseCodeMapping.TOKEN_NOT_FOUND.getMessage());
            }

            String authToken = key.substring(7);

            /**
             * Authenticate Token- valid or not, expired or not - assumptions for simplicity
             */

            UserDetails userDetails = userService.findByAuthToken(authToken.substring(Constants.TOKEN_START_INDEX,Constants.TOKEN_END_INDEX));
            if (!ObjectUtils.isEmpty(userDetails)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                HttpServletResponse resp = (HttpServletResponse) servletResponse;
                String error = ResponseCodeMapping.TOKEN_NOT_FOUND.getMessage();
                resp.reset();
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                servletResponse.setContentLength(error.length());
                servletResponse.getWriter().write(error);
            }
        }

    }
    private String extractToken(final HttpServletRequest request) {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "");
    }
}
