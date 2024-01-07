package site.pointman.chatbot.repository.customrepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.constant.MemberRole;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.dto.ProductCondition;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProductCustomRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    boolean isUse = true;
    String userKey = "QFJSyeIZbO77";
    Long productId = 1L;

    @Test
    void 상품등록() {
        Member member = Member.builder()
                .userKey("Test11")
                .name("테스트11")
                .phoneNumber("01011112222")
                .memberRole(MemberRole.ADMIN)
                .build();

        memberRepository.save(member);

        Product product = Product.builder()
                .id(123123213L)
                .member(member)
                .build();


        productRepository.saveProduct(product);
    }

    @Test
    void 상품조회_유저키() {

        List<Product> products = productRepository.findByUserKey(userKey, isUse);

        products.forEach(product -> {
            assertThat(product.getMember().getUserKey()).isEqualTo(userKey);
            assertThat(product.getIsUse()).isEqualTo(isUse);
        });

    }

    @Test
    void 상품조회_상품아이디() {
        Optional<Product> findProduct = productRepository.findByProductId(productId, isUse);

        assertThat(findProduct).isNotEmpty();
        assertThat(findProduct.get().getId()).isEqualTo(productId);
    }

    @Test
    void 상품전체_조회_조건() {
        ProductStatus productStatus = ProductStatus.판매중;
        int pageNumber = 0;
        int size = 5;

        ProductCondition productCondition = ProductCondition.builder()
                .firstProductStatus(productStatus)
                .build();

        Page<Product> productPage = productRepository.findAll(productCondition, PageRequest.of(pageNumber, size));

        assertThat(productPage.getSize()).isLessThanOrEqualTo(size);

        productPage.getContent().forEach(product -> {
            assertThat(product.getStatus()).isEqualTo(productStatus);
            assertThat(product.getIsUse()).isEqualTo(isUse);
        });
    }

    @Test
    void 상품Dto_조회_조건() {
        ProductStatus productStatus = ProductStatus.판매중;
        int pageNumber = 0;
        int size = 5;

        ProductCondition productCondition = ProductCondition.builder()
                .firstProductStatus(productStatus)
                .build();

        Page<ProductDto> productDtoPage = productRepository.findDtoAll(productCondition, PageRequest.of(pageNumber, size));

        assertThat(productDtoPage.getSize()).isLessThanOrEqualTo(size);
        productDtoPage.getContent().forEach(product -> {
            assertThat(product.getStatus()).isEqualTo(productStatus);
        });
    }

}