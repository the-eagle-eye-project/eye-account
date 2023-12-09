package com.theeagleeyeproject.eyeaccount.filter;

import com.theeagleeyeproject.eaglewings.utility.JwtUtil;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class AccountAuthorizationFilterTest {

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private MockHttpServletResponse response;

    @Mock
    private MockFilterChain filterChain;

    @Mock
    private JwtUtil jwtUtil;


    @Test
    void doFilterInternal() throws ServletException, IOException {

//        AccountAuthorizationFilter filter = new AccountAuthorizationFilter(jwtUtil);
//        Assertions.filter.doFilterInternal(request, response, filterChain);


    }
}