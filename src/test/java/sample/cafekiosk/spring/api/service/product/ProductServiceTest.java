package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.BOTTlE;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품 번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {
        // given
        Product product1 = createProduct("001", BOTTlE, "아메리카노", 1000);
        productRepository.saveAll(List.of(product1));

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("카푸치노")
            .price(5000).build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .contains("002", HANDMADE, SELLING, "카푸치노", 5000);
        assertThat(productRepository.findAll()).hasSize(2)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .containsExactlyInAnyOrder(
                Tuple.tuple("001", BOTTlE, SELLING, "아메리카노", 1000),
                Tuple.tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
            );
    }

    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001 이다.")
    @Test
    void createProductWhenProducstIsEmpty() {
        // given
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("카푸치노")
            .price(5000).build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .contains("001", HANDMADE, SELLING, "카푸치노", 5000);
    }

    private Product createProduct(String productNumber, ProductType type, String name, int price) {
        return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(SELLING)
            .name(name)
            .build();
    }
}
