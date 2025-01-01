package com.rebin.booking.info.service;

import com.rebin.booking.info.domain.Info;
import com.rebin.booking.info.domain.repository.InfoRepository;
import com.rebin.booking.info.dto.InfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminInfoService {
    private final InfoRepository infoRepository;

    public void saveInfoList(List<InfoDto> infoDtoList){
        infoRepository.deleteAll();
        List<Info> infos = infoDtoList.stream()
                .map(info ->
                        Info.builder().image(info.image())
                                .sentence(info.sentence())
                                .build())
                .toList();
        infoRepository.saveAll(infos);
    }
}
