package com.rebin.booking.reservation.domain.infrastructure;

import com.rebin.booking.reservation.domain.repository.CustomTimeSlotRepository;
import com.rebin.booking.reservation.dto.response.TimeSlotResponseForAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomTimeSlotRepositoryImpl implements CustomTimeSlotRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<TimeSlotResponseForAdmin> rowMapper = (ResultSet rs, int rowNum) ->
            new TimeSlotResponseForAdmin(
                    rs.getLong("id"),
                    rs.getObject("date", LocalDate.class),
                    rs.getObject("start_time", LocalTime.class),
                    rs.getBoolean("is_available"),
                    rs.getString("code")
            );

    @Override
    public List<TimeSlotResponseForAdmin> findAllByDate(LocalDate date) {
        String sql = """
                SELECT time_slot.id, date, start_time, is_available, code 
                FROM time_slot 
                LEFT JOIN reservation 
                ON time_slot.id = reservation.time_slot_id
                WHERE date = :date
                """;
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date);
        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }
}
