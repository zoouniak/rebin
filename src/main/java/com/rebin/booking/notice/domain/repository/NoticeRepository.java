package com.rebin.booking.notice.domain.repository;

import com.rebin.booking.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
