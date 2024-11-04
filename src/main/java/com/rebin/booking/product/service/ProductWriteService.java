package com.rebin.booking.product.service;

import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.ProductImage;
import com.rebin.booking.product.domain.repository.ProductImageRepository;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.product.dto.request.ProductCreateRequest;
import com.rebin.booking.product.dto.response.ProductCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductWriteService {
    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = productRepository.save(Product.builder()
                .name(request.name())
                .price(request.price())
                .summary(request.summary())
                .description(request.description())
                .thumbnail(request.thumbnail())
                .extraPersonFee(request.extraPersonFee())
                .guideLine(request.guideLine())
                .build());

        List<ProductImage> images = makeImages(request.images(), product);
        imageRepository.saveAll(images);

        return new ProductCreateResponse(product.getId());
    }

    private List<ProductImage> makeImages(List<String> images, Product save) {
        return images.stream()
                .map(image ->
                        ProductImage.builder()
                        .product(save)
                        .url(image)
                        .build())
                .toList();
    }

}
