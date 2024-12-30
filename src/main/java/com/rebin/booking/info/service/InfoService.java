package com.rebin.booking.info.service;

import com.rebin.booking.info.domain.repository.InfoRepository;
import com.rebin.booking.info.dto.InfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final InfoRepository infoRepository;
    public List<InfoDto> getInfoList(){
        return infoRepository.findAll().stream()
                .map(InfoDto::from)
                .toList();
    }
}
