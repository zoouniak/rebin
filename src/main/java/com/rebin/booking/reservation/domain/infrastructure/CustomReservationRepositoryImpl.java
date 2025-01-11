package com.rebin.booking.reservation.domain.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rebin.booking.reservation.domain.repository.CustomReservationRepository;
import com.rebin.booking.reservation.dto.response.AdminReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationDailyResponse;
import com.rebin.booking.reservation.dto.response.ReservationSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.rebin.booking.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JPAQueryFactory queryFactory;
    private final ObjectMapper objectMapper;

    private static final String RESERVATION_TABLE_NAME = "reservation";
    private static final String PRODUCT_TABLE_NAME = "product";
    private static final String TIME_SLOT_TABLE_NAME = "time_slot";

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

    @Override
    public List<ReservationDailyResponse> getReservationsThisMonth() {
        String sql = String.format("""
                    SELECT
                    r.shoot_date,
                    COUNT(*) AS reservation_count,
                    CONCAT(
                    '[',
                    GROUP_CONCAT(
                        CONCAT(
                            '{"productName":"', p.name, '", "time":"', ts.start_time, '"}'
                        )
                        ORDER BY ts.start_time
                    ),
                     ']'
                   )  AS reservations
                FROM
                    %s r
                JOIN
                    %s p ON r.product_id = p.id
                JOIN
                    %s ts ON r.time_slot_id = ts.id
                WHERE
                    YEAR(r.shoot_date) = YEAR(CURDATE())
                    AND MONTH(r.shoot_date) = MONTH(CURDATE())
                GROUP BY
                    r.shoot_date
                ORDER BY
                    r.shoot_date;
                """, RESERVATION_TABLE_NAME, PRODUCT_TABLE_NAME, TIME_SLOT_TABLE_NAME);

        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), new ReservationDailyResponseRowMapper(objectMapper));
    }

    @RequiredArgsConstructor
    public class ReservationDailyResponseRowMapper implements RowMapper<ReservationDailyResponse> {
        private final  ObjectMapper objectMapper;


        @Override
        public ReservationDailyResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            // 날짜 및 예약 건수
            LocalDate date = LocalDate.parse(rs.getString("shoot_date"));
            int reservationCount = rs.getInt("reservation_count");

            // 예약 내역 JSON 파싱
            String reservationsJson = rs.getString("reservations");
            System.out.println(reservationsJson);
            List<ReservationSummaryResponse> reservationSummaryList = parseReservations(reservationsJson);

            // ReservationDailyResponse 객체 생성
            return new ReservationDailyResponse(date, reservationCount, reservationSummaryList);
        }

        private List<ReservationSummaryResponse> parseReservations(String reservationsJson) {
            List<ReservationSummaryResponse> reservations = new ArrayList<>();
            try {
                // JSON 배열을 Java 객체 리스트로 변환
                List<ReservationSummaryResponse> parsedReservations = objectMapper.readValue(
                        reservationsJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ReservationSummaryResponse.class)
                );
                reservations.addAll(parsedReservations);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return reservations;
        }
    }
}
