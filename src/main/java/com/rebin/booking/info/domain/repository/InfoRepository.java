package com.rebin.booking.info.domain.repository;

import com.rebin.booking.info.domain.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRepository extends JpaRepository<Info,Long> {

}
