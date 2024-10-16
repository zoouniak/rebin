package com.rebin.booking.reservation.service;

import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    // 날짜별 타임슬롯 조회
    public List<TimeSlotResponse> getTimeSlotsByDate(final LocalDate date) {
        List<TimeSlot> allByDate = timeSlotRepository.findAllByDate(date);
        return allByDate.stream()
                .map(TimeSlotResponse::of)
                .toList();
    }

}
