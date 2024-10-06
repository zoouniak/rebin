package com.rebin.booking.product.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rebin.booking.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.rebin.booking.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class CustomProductRepository {
    private final JPAQueryFactory queryFactory;
    private static final long PRODUCT_LIMIT = 6L;

    public List<ProductResponse> getProducts(final Long productId) {
        return queryFactory.select(Projections.constructor(ProductResponse.class,
                        product.id,
                        product.name,
                        product.thumbnail,
                        product.summary,
                        product.price
                )).from(product)
                .where(ltProductId(productId))
                .limit(PRODUCT_LIMIT)
                .orderBy(product.id.desc())
                .fetch();
    }

    private BooleanExpression ltProductId(final Long productId) {
        if (productId == null) {
            return null;
        }
        return product.id.lt(productId);
    }
}
