package com.rebin.booking.info.dto;

import com.rebin.booking.info.domain.Info;

public record InfoDto(
        String image,
        String sentence
) {
    public static InfoDto from(Info info){
        return new InfoDto(
                info.getImage(),
                info.getSentence()
        );
    }
}
