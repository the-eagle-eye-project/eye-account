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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    /**
     * Handles the authorization of any user performing request to the API.
     *
     * @param request     servlet request
     * @param response    servlet response (at this point not yet created)
     * @param filterChain to continue the request with other filters
     * @throws ServletException default exception class for this filter
     * @throws IOException      default exception class for this filter
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        String authorizationHeader = contentCachingRequestWrapper.getHeader(HttpHeaders.AUTHORIZATION);

        String jwt;
        String userId;

        // Extract the token from the Bearer word.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        } else {
            throw new BirdException(ExceptionCategory.UNAUTHORIZED, "The Application doesn't have authorization to consume this API.");
        }

        // Validate if the token is valid.
        if (jwtUtil.isTokenValid(jwt)) {
            userId = jwtUtil.extractClaims(jwt).get("sub").toString();
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Authentication authentication = getAuthentication(userId);
                // Creates the Security Context from the JWT.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            throw new BirdException(ExceptionCategory.UNAUTHORIZED, "The Application doesn't have authorization to consume this API.");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Creates a security context token, so that it can be injected into Spring Security and the security information is
     * available throughout the life of the transaction.
     *
     * @param userId UUID of the user making a request to the API
     * @return an object of type {@link Authentication}
     */
    private Authentication getAuthentication(String userId) {
        return new UsernamePasswordAuthenticationToken(userId, null, null);
    }
}
