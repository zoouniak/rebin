package com.rebin.booking.product.service;

import com.rebin.booking.common.excpetion.ErrorCode;
import com.rebin.booking.common.excpetion.ProductException;
import com.rebin.booking.image.domain.S3ImageEvent;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.ProductImage;
import com.rebin.booking.product.domain.repository.CustomProductRepository;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.product.dto.request.ProductCreateRequest;
import com.rebin.booking.product.dto.response.ProductCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductWriteService {
    private final ProductRepository productRepository;
    private final CustomProductRepository customProductRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public ProductCreateResponse createProduct(final ProductCreateRequest request) {

        final Product product = productRepository.save(Product.builder()
                .name(request.name())
                .price(request.price())
                .summary(request.summary())
                .description(request.description())
                .thumbnail(request.thumbnail())
                .deposit(request.deposit())
                .additionalFee(request.additionalFee())
                .guideLine(request.guideLine())
                .build());

        final List<ProductImage> images = makeImages(request.images(), product);
        customProductRepository.saveAll(images);

        return new ProductCreateResponse(product.getId());
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductCreateRequest request) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.INVALID_PRODUCT));

        final Product updatedProduct = Product.builder()
                .id(product.getId())
                .name(request.name())
                .price(request.price())
                .summary(request.summary())
                .description(request.description())
                .thumbnail(updateThumbNail(request.thumbnail(), product))
                .deposit(request.deposit())
                .images(updateImages(product.getImages(), request.images(), product))
                .additionalFee(request.additionalFee())
                .guideLine(request.guideLine())
                .build();


        productRepository.save(updatedProduct);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }

    private List<ProductImage> updateImages(final List<ProductImage> oldImages, final List<String> newImageNames, Product product) {
        List<ProductImage> newImages = newImageNames.stream()
                .map(imageName -> makeUpdatedImages(oldImages, imageName, product))
                .toList();

        deleteNotUsedImages(oldImages, newImages);
        saveNewlyImages(oldImages, newImages);
        return newImages;
    }

    private ProductImage makeUpdatedImages(final List<ProductImage> oldImages, final String imageName, final Product product) {
        return oldImages.stream()
                .filter(oldImage -> oldImage.getUrl().equals(imageName))
                .findAny()
                .orElseGet(() -> new ProductImage(product, imageName));
    }

    private void saveNewlyImages(final List<ProductImage> oldImages, final List<ProductImage> newImages) {
        final List<ProductImage> newlyImages = newImages.stream()
                .filter(image -> !oldImages.contains(image))
                .toList();

        customProductRepository.saveAll(newlyImages);
    }

    private void deleteNotUsedImages(final List<ProductImage> oldImages, final List<ProductImage> newImages) {
        final List<ProductImage> deletedImages = oldImages.stream()
                .filter(image -> !newImages.contains(image))
                .toList();
        if (deletedImages.isEmpty())
            return;

        customProductRepository.deleteAll(deletedImages);
        deletedImages.
                forEach(image -> publisher.publishEvent(new S3ImageEvent(image.getUrl())));
    }

    private String updateThumbNail(final String thumbnail, final Product product) {
        if (!product.getThumbnail().equals(thumbnail)) {
            publisher.publishEvent(new S3ImageEvent(product.getThumbnail()));
        }

        return thumbnail;
    }

    private List<ProductImage> makeImages(final List<String> images, final Product product) {
        return images.stream()
                .map(image ->
                        ProductImage.builder()
                                .product(product)
                                .url(image)
                                .build())
                .toList();
    }


}
