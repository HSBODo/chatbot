package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void saveProduct() {
        // given
        Product product = Product.builder()
                .id(123132132L)
                .build();

        // when
        productRepository.saveProduct(product);

        // then
    }

    @Test
    void saveProductImage() {
        // given
        ProductImage productImage = ProductImage.builder()
                .build();

        // when
        productRepository.saveProductImage(productImage);

        // then
    }

    @Test
    void insertProduct() {
        // given
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("213213213");
        ProductDto productDto = ProductDto.builder()
                .id(123123L)
                .name("상품")
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setImageUrl(imageUrls);


        // when
        productRepository.insertProduct(productDto,productImageDto);

        // then
    }

    @Test
    void updateStatus() {
        // given
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("213213213");
        ProductDto productDto = ProductDto.builder()
                .id(123123L)
                .name("상품")
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setImageUrl(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        productRepository.updateStatus(123123L, ProductStatus.숨김);

        // then
    }

    @Test
    void deleteProduct() {
        // given
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("213213213");
        ProductDto productDto = ProductDto.builder()
                .id(123123L)
                .name("상품")
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setImageUrl(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        productRepository.deleteProduct(123123L);

        // then
    }

    @Test
    void findByUserKey() {
        // given
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("213213213");
        Customer customer = Customer.builder()
                .userKey("QFJSyeIZbO77")
                .build();
        ProductDto productDto = ProductDto.builder()
                .id(123123L)
                .customer(customer)
                .name("상품")
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setImageUrl(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        List<Product> Products = productRepository.findByUserKey("QFJSyeIZbO77");

        // then

    }

    @Test
    void findByProductId() {
        // given
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://");
        Customer customer = Customer.builder()
                .userKey("QFJSyeIZbO77")
                .build();
        ProductDto productDto = ProductDto.builder()
                .id(100000L)
                .customer(customer)
                .name("상품")
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setImageUrl(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        Product product = productRepository.findByProductId(100000L).get();

        // then
        Assertions.assertThat(product.getCustomer().getUserKey()).isEqualTo(customer.getUserKey());
        Assertions.assertThat(product.getName()).isEqualTo(productDto.getName());
    }

    @Test
    void findByCategory() {
        //give
        Category category = Category.getCategory("취미/게임/음반");
        ProductStatus status = ProductStatus.판매중;

        //when
        List<Product> products = productRepository.findByCategory(category,status);

        //then
        products.forEach(product -> {
            Assertions.assertThat(product.getCategory()).isEqualTo(category);
            Assertions.assertThat(product.getStatus()).isEqualTo(status);
        });
    }
}