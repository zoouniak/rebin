package com.rebin.booking.reservation.domain.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rebin.booking.reservation.domain.repository.CustomReservationRepository;
import com.rebin.booking.reservation.dto.response.AdminReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.rebin.booking.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AdminReservationResponse> getReservations(final LocalDate startDate, final LocalDate endDate,
                                                          final String code, final String name) {
        return queryFactory.select(Projections.constructor(AdminReservationResponse.class,
                        reservation.id,
                        reservation.code,
                        reservation.product.name,
                        reservation.member.name,
                        reservation.timeSlot.date,
                        reservation.timeSlot.startTime,
                        reservation.createdAt,
                        reservation.status
                ))
                .from(reservation)
                .where(
                        reservation.shootDate.between(startDate, endDate),
                        eqCode(code),
                        eqName(name)
                )
                .fetch();
    }

    private BooleanExpression eqName(final String name) {
        if (name == null)
            return null;
        return reservation.member.name.eq(name);
    }

    private BooleanExpression eqCode(final String code) {
        if (code == null)
            return null;
        return reservation.code.eq(code);
    }
}
