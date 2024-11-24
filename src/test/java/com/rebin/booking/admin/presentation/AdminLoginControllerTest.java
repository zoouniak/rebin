package com.rebin.booking.admin.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebin.booking.admin.config.PasswordEncoder;
import com.rebin.booking.admin.domain.repository.AdminRepository;
import com.rebin.booking.admin.dto.request.AdminLoginRequest;
import com.rebin.booking.admin.service.AdminLoginService;
import com.rebin.booking.login.domain.repository.RefreshTokenRepository;
import com.rebin.booking.login.dto.response.AccessTokenResponse;
import com.rebin.booking.login.dto.response.AuthTokens;
import com.rebin.booking.login.infra.JwtProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AdminLoginController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AdminLoginControllerTest {
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String ACCESS_TOKEN = "access-token";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdminLoginService loginService;
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void 관리자_로그인한다() throws Exception {
        final AdminLoginRequest request = new AdminLoginRequest("loginId", "password");
        final AuthTokens authTokens = new AuthTokens("accessToken", "refreshToken");

        when(loginService.login(request)).thenReturn(authTokens);

        ResultActions resultActions = mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        MockHttpServletResponse response = resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse();

        String authorizationResult = response.getHeader(ACCESS_TOKEN);
        String refreshTokenResult = response.getCookie(REFRESH_TOKEN).getValue();

        Assertions.assertEquals(authTokens.accessToken(), authorizationResult);
        Assertions.assertEquals(authTokens.refreshToken(), refreshTokenResult);
    }

    @Test
    void 로그인을_연장한다() throws Exception {
        String refreshToken = "refreshToken";
        String authorizeHeader = "accessToken";

        String newAccessToken = "newAccessToken";

        when(loginService.extend(authorizeHeader, refreshToken))
                .thenReturn(new AccessTokenResponse(newAccessToken));

        ResultActions resultActions = mockMvc.perform(post("/admin/login/token")
                .header(AUTHORIZATION, authorizeHeader)
                .cookie(new Cookie(REFRESH_TOKEN, refreshToken)));

        MockHttpServletResponse response = resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse();

        String accessTokenResult = response.getHeader(ACCESS_TOKEN);
        Assertions.assertEquals(newAccessToken, accessTokenResult);
    }

    @Test
    void 로그아웃을_한다() throws Exception {
        doNothing().when(loginService).removeRefreshToken(any());
        final String refreshToken = "refreshToken";

        ResultActions resultActions = mockMvc.perform(delete("/admin/logout")
                .header(AUTHORIZATION, "accessToken")
                .cookie(new Cookie(REFRESH_TOKEN, refreshToken)));

        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(loginService).removeRefreshToken(refreshToken);
    }

}