package com.rebin.booking.product.domain.repository;

import com.rebin.booking.product.domain.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String TABLE = "product_image";

    public void saveAll(List<ProductImage> images) {
        String sql = String.format("""
                INSERT INTO %s (product_id, url) VALUES(:product_id, :url)
                """, TABLE);

        SqlParameterSource[] params = images.stream()
                .map(this::getImageToSqlParameterSource)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public void deleteAll(List<ProductImage> images){
        String sql = String.format("""
                delete from %s where id in (:ids)
                """,TABLE);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ids", getImageIds(images));

        namedParameterJdbcTemplate.update(sql,params);
    }

    private MapSqlParameterSource getImageToSqlParameterSource(final ProductImage productImage){
        return new MapSqlParameterSource()
                .addValue("product_id",productImage.getProduct().getId())
                .addValue("url",productImage.getUrl());
    }

    private List<Long> getImageIds(List<ProductImage> images) {
        return images.stream()
                .map(ProductImage::getId)
                .toList();
    }
}
