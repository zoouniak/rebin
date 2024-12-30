package com.rebin.booking.reservation.service;

import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.service.strategy.ReservationFinders;
import com.rebin.booking.reservation.util.PriceCalculator;
import com.rebin.booking.reservation.validator.ReservationCancelValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = "/reserve-test.sql")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReservationConcurrencyTest {
    @Autowired
    ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReservationCodeService reservationCodeService;
    @Autowired
    private ReservationFinders reservationFinders;
    @Autowired
    private ReservationCancelValidator cancelValidator;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private PriceCalculator calculator;

    @Test
    @DisplayName("동시에 10개의 예약 요청이 오면 한 개만 성공한다.")
    void reserve_concurrency() throws InterruptedException {
        memberRepository.findById(1L).orElseThrow();
        productRepository.findById(1L).orElseThrow();
        timeSlotRepository.findById(1L).orElseThrow();
        ReservationRequest request = new ReservationRequest(
                "오주은",
                "emila@naver.com",
                "010-1111-1111",
                2,
                "notes",
                true,
                true,
                1L,
                1L
        );
        int thread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(thread);
        CountDownLatch latch = new CountDownLatch(thread);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        // when
        for (int i = 0; i < thread; i++) {
            executorService.execute(() -> {
                try {
                    reservationService.reserve(1L, request);
                    success.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1);
        assertThat(fail.get()).isEqualTo(9);
    }

    @Test
    @DisplayName("하나의 타임슬롯에 대해 다수의 예약생성/예약변경 요청이 오면 그 중 하나만 성공한다.")
    void rescheduleTimeSlot_concurrency() throws InterruptedException {
        ReservationRequest reserveRequest = new ReservationRequest(
                "오주은",
                "emila@naver.com",
                "010-1111-1111",
                2,
                "notes",
                true,
                true,
                1L,
                1L
        );


        int thread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(thread);
        CountDownLatch latch = new CountDownLatch(thread);
        AtomicInteger successReserve = new AtomicInteger(0);
        AtomicInteger failReserve = new AtomicInteger(0);
        AtomicInteger successReschedule = new AtomicInteger(0);
        AtomicInteger failReschedule = new AtomicInteger(0);

        // when
        for (int i = 0; i < thread; i++) {
            if (i < 5) {
                // 예약 작업
                executorService.execute(() -> {
                    try {
                        reservationService.reserve(1L, reserveRequest);
                        successReserve.incrementAndGet();
                    } catch (Exception e) {
                        System.out.println("Reserve Error: " + e.getMessage());
                        failReserve.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            } else {
                // 재예약 작업
                executorService.execute(() -> {
                    try {
                        reservationService.rescheduleTimeSlot(1L, 100L, 1L);
                        successReschedule.incrementAndGet();
                    } catch (Exception e) {
                        System.out.println("Reschedule Error: " + e.getMessage());
                        failReschedule.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(successReserve.get() + successReschedule.get()).isEqualTo(1);
        assertThat(failReserve.get() + failReschedule.get()).isEqualTo(9);
    }

}
