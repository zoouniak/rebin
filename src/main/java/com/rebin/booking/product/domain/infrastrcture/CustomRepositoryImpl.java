package com.rebin.booking.product.domain.infrastrcture;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rebin.booking.product.domain.ProductImage;
import com.rebin.booking.product.domain.repository.CustomProductRepository;
import com.rebin.booking.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.rebin.booking.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JPAQueryFactory queryFactory;
    private static final long PRODUCT_LIMIT = 6L;
    private static final String TABLE = "product_image";

    @Override
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

    public void saveAll(final List<ProductImage> images) {
        String sql = String.format("""
                INSERT INTO %s (product_id, url) VALUES(:product_id, :url)
                """, TABLE);

        SqlParameterSource[] params = images.stream()
                .map(this::getImageToSqlParameterSource)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public void deleteAll(final List<ProductImage> images) {
        String sql = String.format("""
                delete from %s where id in (:ids)
                """, TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ids", getImageIds(images));

        namedParameterJdbcTemplate.update(sql, params);
    }

    private BooleanExpression ltProductId(final Long productId) {
        if (productId == null) {
            return null;
        }
        return product.id.lt(productId);
    }

    private MapSqlParameterSource getImageToSqlParameterSource(final ProductImage productImage) {
        return new MapSqlParameterSource()
                .addValue("product_id", productImage.getProduct().getId())
                .addValue("url", productImage.getUrl());
    }

    private List<Long> getImageIds(final List<ProductImage> images) {
        return images.stream()
                .map(ProductImage::getId)
                .toList();
    }
}
