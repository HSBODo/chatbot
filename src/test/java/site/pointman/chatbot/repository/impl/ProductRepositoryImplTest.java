package site.pointman.chatbot.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.customer.CustomerDto;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class ProductRepositoryImplTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    void save() {
    }

    @Test
    void productImageSave() {
        CustomerDto customerDto = CustomerDto.builder()
                .userKey("QFJSyeIZbO77")
                .build();

        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .customer(customerDto.toEntity())
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setProduct(productDto.toEntity());
        productImageDto.getImageUrl().add("테스트1");
        productImageDto.getImageUrl().add("테스트2");
        productImageDto.getImageUrl().add("테스트3");
        productImageDto.getImageUrl().add("테스트4");
        productImageDto.getImageUrl().add("테스트5");

        productRepository.productImageSave(productImageDto);
    }
}