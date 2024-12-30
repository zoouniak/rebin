package com.rebin.booking.info.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;
    private String sentence;

    @Builder
    public Info(Long id, String image, String sentence) {
        this.id = id;
        this.image = image;
        this.sentence = sentence;
    }
}
