package com.rebin.booking.listener;

import com.rebin.booking.reservation.domain.ReservationEvent;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventListener {
    private final JavaMailSender mailSender;
    private final String sender;
    private final String receiver;

    public ReservationEventListener(@Value("${spring.mail.username}") String sender,
                                    @Value("${spring.mail.receiver}") String receiver,
                                    JavaMailSender mailSender) {
        this.sender = sender;
        this.receiver = receiver;
        this.mailSender = mailSender;
    }

    @EventListener
    @Async
    public void sendMailToAdmin(ReservationEvent event) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(receiver);
            messageHelper.setSubject(getSubject(event.type()));
            messageHelper.setText("예약번호 : " + event.code());
        };

        mailSender.send(messagePreparator);
    }

    private String getSubject(ReservationStatusType status) {
        StringBuilder subject = new StringBuilder("[Re:bin] ");
        switch (status) {
            case PENDING_PAYMENT -> subject.append("새로운 예약이 생성되었습니다");
            case CANCELED -> subject.append("예약이 취소되었습니다.");
            case CONFIRM_REQUESTED -> subject.append("예약금 입금 확인 요청이 도착했습니다");
            case SHOOTING_COMPLETED -> subject.append("촬영이 완료되었습니다.");
            case PAYMENT_CONFIRMED -> subject.append("예약금 입금 확인이 완료되었습니다.");
        }
        return subject.toString();
    }

}
