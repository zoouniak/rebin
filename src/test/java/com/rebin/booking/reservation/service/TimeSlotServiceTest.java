package com.rebin.booking.reservation.service;

import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceTest {
    @InjectMocks
    TimeSlotService timeSlotService;

    @Mock
    TimeSlotRepository timeSlotRepository;

    @Test
    void 날짜로_타임슬롯을_조회한다() {
        // given
        LocalDate date = LocalDate.now();
        TimeSlot timeSlot1 = new TimeSlot(LocalDate.now(), LocalTime.now());
        TimeSlot timeSlot2 = new TimeSlot(LocalDate.now(), LocalTime.now());
        List<TimeSlot> timeSlotList = List.of(timeSlot1, timeSlot2);

        when(timeSlotRepository.findAllByDate(any())).thenReturn(timeSlotList);
        // when
        List<TimeSlotResponse> timeSlotsByDate = timeSlotService.getTimeSlotsByDate(date);
        // then
        Assertions.assertEquals(2, timeSlotsByDate.size());
        verify(timeSlotRepository, times(1)).findAllByDate(date);
    }
}