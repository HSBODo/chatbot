package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.constant.product.Category;
import site.pointman.chatbot.constant.product.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
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
    private boolean isUse = true;

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
        productImageDto.setImageUrls(imageUrls);


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
        productImageDto.setImageUrls(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        productRepository.updateStatus(123123L, ProductStatus.숨김, isUse);

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
        productImageDto.setImageUrls(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        productRepository.deleteProduct(123123L, isUse);

        // then
    }

    @Test
    void findByUserKey() {
        // given
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("213213213");
        Member member = Member.builder()
                .userKey("QFJSyeIZbO77")
                .build();
        ProductDto productDto = ProductDto.builder()
                .id(123123L)
                .member(member)
                .name("상품")
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setImageUrls(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        List<Product> Products = productRepository.findByUserKey("QFJSyeIZbO77", isUse);

        // then

    }

    @Test
    void findByProductId() {
        // given
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://");
        Member member = Member.builder()
                .userKey("QFJSyeIZbO77")
                .build();
        ProductDto productDto = ProductDto.builder()
                .id(100000L)
                .member(member)
                .name("상품")
                .build();
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setImageUrls(imageUrls);
        productRepository.insertProduct(productDto,productImageDto);

        // when
        Product product = productRepository.findByProductId(100000L, isUse).get();

        // then
        Assertions.assertThat(product.getMember().getUserKey()).isEqualTo(member.getUserKey());
        Assertions.assertThat(product.getName()).isEqualTo(productDto.getName());
    }

    @Test
    void findByCategory() {
        //give
        Category category = Category.getCategory("취미/게임/음반");
        ProductStatus firstStatus = ProductStatus.판매중;
        ProductStatus secondStatus = ProductStatus.예약;

        //when
        Page<Product> products = productRepository.findByCategory(isUse, category, firstStatus, secondStatus, PageRequest.of(0, 10));


        //then
        products.getContent().forEach(product -> {
            Assertions.assertThat(product.getCategory()).isEqualTo(category);
            Assertions.assertThat(product.getStatus()).isIn(firstStatus, secondStatus);
        });
    }

    @Test
    void findBySearchWord() {
        //give
        Sort sort = Sort.by("createDate").descending();
        String searchWord = "상품";
        ProductStatus firstStatus = ProductStatus.판매중;
        ProductStatus secondStatus = ProductStatus.예약;
        //when
        Page<Product> products = productRepository.findBySearchWord(isUse, searchWord, firstStatus, secondStatus, PageRequest.of(0, 10, sort));

        //then
        products.getContent().forEach(product -> {
            Assertions.assertThat(product.getStatus()).isIn(firstStatus,secondStatus);
        });
    }

    @Test
    void findByStatus() {
        Sort sort = Sort.by("createDate").descending();
        Page<Product> main = productRepository.findMain(isUse, ProductStatus.판매중, ProductStatus.예약, PageRequest.of(0, 10, sort));

        main.getContent().forEach(product -> {
            log.info("{}",product.getName());
            log.info("{}",product.getStatus());
            log.info("{}",product.getCreateDate());

        });
    }

    @Test
    void findMain() {
        Page<Product> main = productRepository.findMain(true, ProductStatus.판매중, ProductStatus.예약, PageRequest.of(0, 10));
        main.getContent().forEach(product -> {
            log.info("{}",product.getId());
            log.info("{}",product.getProductImages().getImageUrls());
        });

    }
}