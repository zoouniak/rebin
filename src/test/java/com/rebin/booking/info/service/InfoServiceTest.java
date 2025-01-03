package com.rebin.booking.info.service;

import com.rebin.booking.info.domain.Info;
import com.rebin.booking.info.domain.repository.InfoRepository;
import com.rebin.booking.info.dto.InfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InfoServiceTest {
    @InjectMocks
    private InfoService infoService;

    @Mock
    private InfoRepository infoRepository;

    @Test
    @DisplayName("")
    void getInfoList() {
        // given
        List<Info> infos = List.of(new Info(1L, "image1", "sentenc1"), new Info(2L, "image2", "sentenc2"));
        Mockito.when(infoRepository.findAll()).thenReturn(infos);

        // when
        List<InfoDto> response = infoService.getInfoList();
        // then
        Assertions.assertThat(response).usingRecursiveComparison()
                .isEqualTo(infos.stream().map(InfoDto::from).toList());
    }

}