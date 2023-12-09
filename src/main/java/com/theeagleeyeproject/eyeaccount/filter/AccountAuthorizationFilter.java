package com.theeagleeyeproject.eyeaccount.filter;

import com.theeagleeyeproject.eaglewings.exception.BirdException;
import com.theeagleeyeproject.eaglewings.exception.ExceptionCategory;
import com.theeagleeyeproject.eaglewings.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

/**
 * {@link AccountAuthorizationFilter} used to authorize a transaction.
 *
 * @author johnmartinez
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class AccountAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Used to access all the JWT utilities
     */
    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        String authorizationHeader = contentCachingRequestWrapper.getHeader(HttpHeaders.AUTHORIZATION);

        String jwt = null;

        // Extract the token from the Bearer word.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        } else {
            throw new BirdException(ExceptionCategory.UNAUTHORIZED, "The Application doesn't have authorization to consume this API.");
        }

        // Validate if the token is valid.
        if (!jwtUtil.isTokenValid(jwt)) {
            throw new BirdException(ExceptionCategory.UNAUTHORIZED, "The Application doesn't have authorization to consume this API.");
        }

        filterChain.doFilter(request, response);
    }
}
