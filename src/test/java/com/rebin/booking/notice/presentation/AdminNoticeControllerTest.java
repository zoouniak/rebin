package com.rebin.booking.notice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebin.booking.admin.service.AdminLoginArgumentResolver;
import com.rebin.booking.admin.service.AdminLoginResolverConfig;
import com.rebin.booking.login.domain.repository.RefreshTokenRepository;
import com.rebin.booking.login.infra.JwtProvider;
import com.rebin.booking.notice.domain.repository.NoticeRepository;
import com.rebin.booking.notice.dto.request.NoticeRequest;
import com.rebin.booking.notice.service.AdminNoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AdminNoticeController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AdminNoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdminNoticeService adminNoticeService;
    @MockBean
    private NoticeRepository noticeRepository;

    @MockBean
    private AdminLoginArgumentResolver adminLoginArgumentResolver;
    @MockBean
    private AdminLoginResolverConfig adminLoginResolverConfig;
    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void saveNotice_exceedTitleLength() throws Exception {
        NoticeRequest request = new NoticeRequest("titletitletitletitletitletitle", "content");

        ResultActions resultActions = mockMvc.perform(post("/admin/notices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn()
                .getResponse();
    }

}