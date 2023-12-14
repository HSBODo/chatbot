package site.pointman.chatbot.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.dto.customer.CustomerDto;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.utill.NumberUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class ProductRepositoryImplTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    void saveProduct() {
        CustomerDto customerDto = CustomerDto.builder()
                .userKey("QFJSyeIZbO77")
                .build();

        ProductDto productDto = ProductDto.builder()
                .customer(customerDto.toEntity())
                .build();
        productDto.setId(NumberUtils.createProductId());
        productRepository.saveProduct(productDto.toEntity());
    }

    @Test
    void saveProductImage() {
        CustomerDto customerDto = CustomerDto.builder()
                .userKey("QFJSyeIZbO77")
                .build();

        ProductDto productDto = ProductDto.builder()
                .customer(customerDto.toEntity())
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setProduct(productDto.toEntity());
        productImageDto.getImageUrl().add("테스트1");
        productImageDto.getImageUrl().add("테스트2");
        productImageDto.getImageUrl().add("테스트3");
        productImageDto.getImageUrl().add("테스트4");
        productImageDto.getImageUrl().add("테스트5");

        productRepository.saveProductImage(productImageDto.toEntity());
    }

    @Test
    void addProduct() {
    }

    @Test
    void findByUserKey() {
        List<Product> byUserKey = productRepository.findByUserKey("QFJSyeIZbO77");
        byUserKey.forEach(product -> {
            Long id = product.getProductImages().getId();
            log.info("id={}",id);
        });
    }

    @Test
    void findByProductId() {
        Optional<Product> byProductId = productRepository.findByProductId(4717L);
        Product product = byProductId.get();
        log.info("name={}",product.getName());
    }
}