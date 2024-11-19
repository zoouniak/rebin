package com.rebin.booking.product.service;

import com.rebin.booking.common.excpetion.ErrorCode;
import com.rebin.booking.common.excpetion.ProductException;
import com.rebin.booking.image.domain.S3ImageEvent;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.ProductImage;
import com.rebin.booking.product.domain.repository.CustomProductRepository;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.product.dto.request.ProductCreateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductWriteServiceTest {
    @InjectMocks
    ProductWriteService productWriteService;

    @Mock
    ProductRepository productRepository;

    @Mock
    CustomProductRepository customProductRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @Test
    void 존재하지않는_상품은_수정할수없다() {
        ProductCreateRequest request = new ProductCreateRequest(
                "수정한 이름",
                20_000,
                "요약",
                "상품의 상세설명",
                "thumbnail",
                List.of("i1"),
                10_000,
                10_000,
                "가이드라인"
        );
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        ProductException exception = assertThrows(ProductException.class, () -> productWriteService.updateProduct(1L, request));
        Assertions.assertEquals(ErrorCode.INVALID_PRODUCT.getCode(), exception.getCode());
    }

    @Test
    @DisplayName("상품 정보만 수정한다 : 썸네일, 사진 수정 없는 경우")
    void 상품을_수정한다1() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                "수정한 이름",
                20_000,
                "요약",
                "상품의 상세설명",
                "thumbnail",
                List.of("i1"),
                10_000,
                10_000,
                "가이드라인"
        );
        Product product = getProduct();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        productWriteService.updateProduct(1L, request);

        // then
        verify(productRepository).save(argThat(updated ->
                updated.getName().equals(request.name()) &&
                        updated.getPrice() == request.price() &&
                        updated.getSummary().equals(request.summary()) &&
                        updated.getDescription().equals(request.description()) &&
                        updated.getThumbnail().equals(request.thumbnail()) &&
                        updated.getDeposit() == request.deposit() &&
                        updated.getAdditionalFee() == request.additionalFee() &&
                        // 이미지는 기존과 동일해야함
                        (IntStream.range(0, updated.getImages().size())
                                .allMatch(i -> updated.getImages().get(i).equals(product.getImages().get(i)))) &&
                        updated.getGuideLine().equals(request.guideLine())
        ));
        // s3 이벤트 발행 없어야함
        ArgumentCaptor<S3ImageEvent> eventCaptor = ArgumentCaptor.forClass(S3ImageEvent.class);
        verify(publisher, times(0)).publishEvent(eventCaptor.capture());

    }

    @Test
    @DisplayName("상품을 수정한다 : 사진 수정 있는 경우, 기존 사진에서 새로운 사진이 추가됨")
    void 상품을_수정한다2() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                "수정한 이름",
                20_000,
                "요약",
                "상품의 상세설명",
                "thumbnail",
                List.of("i1", "i2", "i3"),
                10_000,
                10_000,
                "가이드라인"
        );
        Product product = getProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        productWriteService.updateProduct(1L, request);

        // then
        verify(productRepository).save(argThat(updated ->
                updated.getName().equals(request.name()) &&
                        updated.getPrice() == request.price() &&
                        updated.getSummary().equals(request.summary()) &&
                        updated.getDescription().equals(request.description()) &&
                        updated.getThumbnail().equals(request.thumbnail()) &&
                        updated.getDeposit() == request.deposit() &&
                        updated.getAdditionalFee() == request.additionalFee() &&
                        // 이미지 url은 요청의 url과 동일해야함
                        (IntStream.range(0, updated.getImages().size())
                                .allMatch(i -> updated.getImages().get(i).getUrl().equals(request.images().get(i)))) &&
                        updated.getGuideLine().equals(request.guideLine())
        ));
        // s3이벤트 발행되었는지 확인
        ArgumentCaptor<S3ImageEvent> eventCaptor = ArgumentCaptor.forClass(S3ImageEvent.class);
        // 삭제된 이미지가 없으므로 s3 이벤트 없어야함
        verify(publisher, times(0)).publishEvent(eventCaptor.capture());
    }

    @Test
    @DisplayName("상품을 수정한다 : 사진 수정 있는 경우, 기존 사진이 삭제되고 새로운 사진이 추가됨")
    void 상품을_수정한다3() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(
                "수정한 이름",
                20_000,
                "요약",
                "상품의 상세설명",
                "thumbnail",
                List.of("i2", "i3"),
                10_000,
                10_000,
                "가이드라인"
        );
        Product product = getProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        productWriteService.updateProduct(1L, request);

        // then
        verify(productRepository).save(argThat(updated ->
                updated.getName().equals(request.name()) &&
                        updated.getPrice() == request.price() &&
                        updated.getSummary().equals(request.summary()) &&
                        updated.getDescription().equals(request.description()) &&
                        updated.getThumbnail().equals(request.thumbnail()) &&
                        updated.getDeposit() == request.deposit() &&
                        updated.getAdditionalFee() == request.additionalFee() &&
                        // 이미지 url은 요청의 url과 동일해야함
                        (IntStream.range(0, updated.getImages().size())
                                .allMatch(i -> updated.getImages().get(i).getUrl().equals(request.images().get(i)))) &&
                        updated.getGuideLine().equals(request.guideLine())
        ));
        // s3이벤트 발행되었는지 확인
        ArgumentCaptor<S3ImageEvent> eventCaptor = ArgumentCaptor.forClass(S3ImageEvent.class);
        // 삭제된 이미지가 있으므로 s3 이벤트 있어야함
        verify(publisher, times(1)).publishEvent(eventCaptor.capture());
    }

    private static Product getProduct() {
        ProductImage image = new ProductImage("i1");
        Product product = Product.builder()
                .id(1L)
                .name("상품이름")
                .price(10_000)
                .summary("요약")
                .description("상품의")
                .thumbnail("thumbnail")
                .deposit(10_000)
                .additionalFee(10_000)
                .images(List.of(image))
                .guideLine("가이드라인")
                .build();
        image.setProduct(product);
        return product;
    }
}