package com.theeagleeyeproject.eyeaccount.filter;

import com.theeagleeyeproject.eaglewings.controller.BirdErrorController;
import com.theeagleeyeproject.eaglewings.exception.BirdException;
import com.theeagleeyeproject.eaglewings.exception.ExceptionCategory;
import com.theeagleeyeproject.eaglewings.security.Role;
import com.theeagleeyeproject.eaglewings.utility.JwtUtil;
import com.theeagleeyeproject.eyeaccount.controller.CreateAccountController;
import com.theeagleeyeproject.eyeaccount.dao.EyeAccountRepository;
import com.theeagleeyeproject.eyeaccount.entity.EyeAccountEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * {@link AccountAuthorizationFilter} used to authorize a transaction.
 *
 * @author johnmartinez
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Log4j2
public class AccountAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Used to access all the JWT utilities
     */
    private final JwtUtil jwtUtil;

    /**
     * Used to verify the account id is registered and validated
     */
    private final EyeAccountRepository accountRepository;


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

        String requestURI = request.getRequestURI();

        try {
            if (!CreateAccountController.ACCOUNT_RESOURCE_URL.equals(requestURI)) {
                String jwt = getHeaderJWT(request);

                // Validate if the token is valid.
                if (jwtUtil.isTokenValid(jwt)) {
                    String userId = jwtUtil.extractClaims(jwt).get("sub").toString();
                    Optional<EyeAccountEntity> accountEntity = accountRepository.findById(userId);
                    Authentication au = SecurityContextHolder.getContext().getAuthentication();

                    if (accountEntity.isPresent() && (au == null || au.getPrincipal().equals("anonymousUser"))) {
                        Object jwtRole = jwtUtil.extractClaims(jwt).get("role");
                        Role role = null;
                        // Creates the role, if the role is present in the JWT.
                        if (jwtRole != null) {
                            try {
                                role = Role.valueOf(jwtRole.toString().toUpperCase());
                            } catch (IllegalArgumentException e) {
                                logger.warn("Unexpected role: " + jwtRole);
                            }
                        }
                        Authentication authentication = getAuthentication(userId, createAuthorities(role));
                        // Creates the Security Context from the JWT.
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    }
                } else {
                    throw new BirdException(ExceptionCategory.FORBIDDEN, "The access to this resource is forbidden with the current credentials.");
                }
            }
            filterChain.doFilter(request, response);
        } catch (BirdException e) {
            request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, e);
            request.getRequestDispatcher(BirdErrorController.EXCEPTION_MAPPING).forward(request, response);
        }
    }

    /**
     * Extracts the JWT from the header request
     *
     * @param request http servlet request
     * @return a JWT of type {@link String}
     */
    private String getHeaderJWT(HttpServletRequest request) {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
        String authorizationHeader = contentCachingRequestWrapper.getHeader(HttpHeaders.AUTHORIZATION);

        String jwt = null;

        // Extract the token from the Bearer word.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }
        return jwt;
    }

    /**
     * Creates a security context token, so that it can be injected into Spring Security and the security information is
     * available throughout the life of the transaction.
     *
     * @param userId UUID of the user making a request to the API
     * @return an object of type {@link Authentication}
     */
    private Authentication getAuthentication(String userId, Collection<GrantedAuthority> roles) {
        return new UsernamePasswordAuthenticationToken(userId, null, roles);
    }

    /**
     * Method used to set roles in the Authorities
     *
     * @param role user's stored role in the database
     * @return a Collection of GrantedAuthority
     */
    private Collection<GrantedAuthority> createAuthorities(Role role) {
        // If the user doesn't have any user, passed from the JWT Authorization Filter, then it will assign a DEFAULT role.
        if (role == null) {
            role = Role.DEFAULT;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }
}
