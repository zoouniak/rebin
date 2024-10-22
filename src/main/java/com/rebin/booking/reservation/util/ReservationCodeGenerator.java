package com.rebin.booking.reservation.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;



public class ReservationCodeGenerator {
    private static final Random RANDOM = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateCode() {
        String dateTimePart = generateTimePart();
        String randomPart = generateRandomString(3);
        return "RE" + dateTimePart + randomPart;
    }
    private static String generateTimePart(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
    }

    // 랜덤한 3자리 문자열 생성 (대문자)
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
