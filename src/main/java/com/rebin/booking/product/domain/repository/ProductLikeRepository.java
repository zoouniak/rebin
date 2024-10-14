package com.rebin.booking.product.domain.repository;

import com.rebin.booking.product.domain.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeRepository extends JpaRepository<ProductLike,Long> {
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
