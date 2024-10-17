package com.rebin.booking.reservation.service.strategy;

import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.dto.request.ReservationLookUpRequest;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rebin.booking.reservation.dto.request.ReservationLookUpRequest.CANCELED;

@Service
@RequiredArgsConstructor
public class ShootCanceledReservationFinder implements ReservationFinder {
    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationResponse> getReservations(Long memberId) {
        return reservationRepository.findAllCanceledShootByMemberId(memberId).stream()
                .map(ReservationResponse::of)
                .toList();
    }

    @Override
    public ReservationLookUpRequest getStatus() {
        return CANCELED;
    }
}
