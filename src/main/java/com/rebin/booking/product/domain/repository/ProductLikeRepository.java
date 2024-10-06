package com.rebin.booking.product.domain.repository;

import com.rebin.booking.product.domain.ProductLIke;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeRepository extends JpaRepository<ProductLIke,Long> {
    boolean existsByMemberIdAndProductId(Long memeberId, Long productId);
}
