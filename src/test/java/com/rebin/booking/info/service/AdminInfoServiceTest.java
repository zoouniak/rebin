package com.rebin.booking.info.service;

import com.rebin.booking.info.domain.repository.InfoRepository;
import com.rebin.booking.info.dto.InfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdminInfoServiceTest {
    @InjectMocks
    private AdminInfoService adminInfoService;

    @Mock
    private InfoRepository infoRepository;

    @Test
    @DisplayName("소개를 저장한다")
    void saveInfoList() {
        // given
        List<InfoDto> request = List.of(new InfoDto("image1", "sentenc1"), new InfoDto("image1", "sentenc1"));

        // when
        adminInfoService.saveInfoList(request);

        // then
        Mockito.verify(infoRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

}