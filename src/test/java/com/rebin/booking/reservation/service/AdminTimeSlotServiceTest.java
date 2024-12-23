package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.TimeSlotException;
import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.repository.CustomTimeSlotRepository;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.rebin.booking.common.excpetion.ErrorCode.ALREADY_EXIST;
import static com.rebin.booking.common.excpetion.ErrorCode.CANT_DELETE_TIMESLOT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTimeSlotServiceTest {
    @InjectMocks
    private AdminTimeSlotService adminTimeSlotService;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private CustomTimeSlotRepository customTimeSlotRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("타임슬롯의 {날짜, 시간}은 중복될 수 없다")
    void CreateTimeSlot_alreadyExist() {
        // given
        LocalDate date = LocalDate.of(2024, 10, 25);
        LocalTime time = LocalTime.of(10, 0);
        when(timeSlotRepository.existsByDateAndStartTime(date, time))
                .thenReturn(true);

        // when
        Assertions.assertThatThrownBy(() -> adminTimeSlotService.createTimeSlot(date, time))
                .isInstanceOf(TimeSlotException.class)
                .hasMessage(ALREADY_EXIST.getMsg());
    }

    @Test
    @DisplayName("타임슬롯을 저장한다")
    void saveTimeSlot() {
        // given
        LocalDate date = LocalDate.of(2024, 10, 25);
        LocalTime time = LocalTime.of(10, 0);
        when(timeSlotRepository.existsByDateAndStartTime(date, time))
                .thenReturn(false);
        when(timeSlotRepository.save(any()))
                .thenReturn(new TimeSlot(1L, date, time));

        // when
        TimeSlotResponse actual = adminTimeSlotService.createTimeSlot(date, time);
        Assertions.assertThat(actual)
                .extracting(TimeSlotResponse::date, TimeSlotResponse::startTime)
                .containsExactly(date, time);
    }

    @Test
    @DisplayName("존재하지 않는 타임슬롯을 삭제하면 에러가 발생한다.")
    void deleteTimeSlotById_cantDelete() {
        when(reservationRepository.existsByTimeSlotId(any()))
                .thenReturn(false);

        // when
        Assertions.assertThatThrownBy(() -> adminTimeSlotService.deleteTimeSlotById(1L))
                .isInstanceOf(TimeSlotException.class)
                .hasMessage(CANT_DELETE_TIMESLOT.getMsg());
    }

    @Test
    @DisplayName("타임슬롯을 삭제한다.")
    void deleteTimeSlotById() {
        when(reservationRepository.existsByTimeSlotId(any()))
                .thenReturn(true);

        // when
        adminTimeSlotService.deleteTimeSlotById(1L);

        // then
        verify(timeSlotRepository, times(1)).deleteById(1L);
    }
}