package site.pointman.chatbot.domain.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.dto.ProductCondition;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    String userKey = "QFJSyeIZbO77";
    Long productId = 1L;

    @Test
    void addProduct() {
        String productName = "상품이름";
        Long price = 10000L;
        String description = "상품설명";
        String tradingLocation = "거래희망장소";
        String kakaoOpenChatUrl = "https://www.";
        Category category = Category.패션_잡화;
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://t1.kakaocdn.net/kakaocorp/corp_thumbnail/Kakao.png");
        ProductDto productDto = ProductDto.builder()
                .userKey(userKey)
                .name(productName)
                .price(price)
                .description(description)
                .tradingLocation(tradingLocation)
                .kakaoOpenChatUrl(kakaoOpenChatUrl)
                .category(category)
                .imageUrls(imageUrls)
                .build();

        productService.addProduct(productDto);

    }

    @Test
    void 상품_상태_변경() {
        ProductStatus productStatus = ProductStatus.숨김;


        productService.updateProductStatus(productId,productStatus);
        Product product = productService.getProduct(productId);

        assertThat(product.getStatus()).isEqualTo(productStatus);
    }

    @Test
    void 상품_삭제() {

        productService.deleteProduct(productId);

        Optional<Product> deleteProduct = productRepository.findByProductId(productId, false);

        assertThat(deleteProduct).isNotEmpty();
        assertThat(deleteProduct.get().getIsUse()).isFalse();
        assertThat(deleteProduct.get().getProductImages().getIsUse()).isFalse();
    }

    @Test
    void 상품_전체_조회_조건() {
        int page = 0;
        Category category = Category.패션_잡화;
        ProductCondition productCondition = ProductCondition.builder()
                .productCategory(category)
                .build();

        Page<Product> products = productService.getProducts(productCondition, page);

        assertThat(products.getSize()).isLessThanOrEqualTo(10);
        products.getContent().forEach(product -> {
            assertThat(product.getCategory()).isEqualTo(category);
            assertThat(product.getIsUse()).isTrue();
        });
    }

    @Test
    void 상품Dto_전체_조회_조건() {
        int page = 0;
        ProductStatus status = ProductStatus.판매중;
        ProductCondition productCondition = ProductCondition.builder()
                .firstProductStatus(status)
                .build();

        Page<ProductDto> productDtos = productService.getProductDtos(productCondition, page);

        assertThat(productDtos.getSize()).isLessThanOrEqualTo(10);
        productDtos.getContent().forEach(product -> {
            assertThat(product.getStatus()).isEqualTo(status);
        });
    }

    @Test
    void 결제체결된_상품_전체_조회() {
        int page = 0;

        Page<Product> salesContractProducts = productService.getSalesContractProducts(userKey, page);

        assertThat(salesContractProducts.getSize()).isLessThanOrEqualTo(10);
        salesContractProducts.getContent().forEach(product -> {
            assertThat(product.getStatus()).isEqualTo(ProductStatus.판매대기);
        });
    }

    @Test
    void 상품_조회() {
        Product product = productService.getProduct(productId);

        assertThat(product.getId()).isEqualTo(productId);
        assertThat(product.getIsUse()).isTrue();
    }

    @Test
    void 상품Dto_조회() {

        ProductDto productDto = productService.getProductDto(productId);

        assertThat(productDto.getId()).isEqualTo(productId);
    }
}