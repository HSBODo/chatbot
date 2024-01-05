package site.pointman.chatbot.globalservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.payment.constant.PaymentStatus;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.service.ProductService;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.domain.response.constant.ResultCode;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class ProductServiceTest {
    @Autowired
    ProductService productService;

    private ObjectMapper mapper = new ObjectMapper();
    private ChatBotRequest chatBotRequest;
    private String userKey;
    private Long productId;

    @BeforeEach
    public void setUp() {
        userKey = "QFJSyeIZbO77";
        productId = 19528L;
    }

    @Test
    @Transactional
    void addProduct() {
        //give
        String imageUrl = "https://item.kakaocdn.net/do/a7fd7c0630f8aea8419a565fb2773bbc82f3bd8c9735553d03f6f982e10ebe70";
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add(imageUrl);

        Member member = Member.builder()
                .userKey(userKey)
                .name("테스트")
                .build();
        ProductDto productDto = ProductDto.builder()
                .member(member)
                .name("상품이름")
                .description("상품설명")
                .price(10000L)
                .build();

        //when
        Response response = productService.addProduct(productDto, userKey);


        //then
        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK.getValue());
    }

    @Test
    @Transactional
    void getProductsByCategory() {
        Category category = Category.패션_잡화;
        int pageNumber = 1;

        Page<Product> productsByCategory = productService.getProductsByCategory(category, pageNumber);

        productsByCategory.getContent().forEach(product -> {
            Assertions.assertThat(product.getCategory()).isEqualTo(category);
        });
    }

    @Test
    @Transactional
    void getProduct() {

        Optional<Product> product = productService.getProduct(productId);

        Assertions.assertThat(product).isNotEmpty();
        Assertions.assertThat(product.get().getId()).isEqualTo(productId);
    }

    @Test
    @Transactional
    void getProductsAll() {

        List<Product> productsAll = productService.getProductsAll();

        productsAll.forEach(product -> {
            Assertions.assertThat(product.getId()).isNotNull();
        });
    }

    @Test
    @Transactional
    void getMemberProducts() {

        List<Product> memberProducts = productService.getMemberProducts(userKey);

        memberProducts.forEach(product -> {
            Assertions.assertThat(product.getMember().getUserKey()).isEqualTo(userKey);
        });
    }

    @Test
    @Transactional
    void getMemberProductsByStatus() {
        ProductStatus productStatus= ProductStatus.판매중;
        int pageNumber = 1;


        Page<Product> memberProductsByStatus = productService.getMemberProductsByStatus(userKey, productStatus, pageNumber);

        memberProductsByStatus.getContent().forEach(product -> {
            Assertions.assertThat(product.getStatus()).isEqualTo(productStatus);
        });
    }

    @Test
    @Transactional
    void getMainProducts() {
        int pageNumber = 1;

        Page<Product> mainProducts = productService.getMainProducts(pageNumber);

        mainProducts.getContent().forEach(product -> {
            Assertions.assertThat(product.getStatus()).isIn(ProductStatus.판매중, ProductStatus.예약);
        });
    }

    @Test
    @Transactional
    void getProductsBySearchWord() {
        String searchWord = "판매";
        int pageNumber = 1;

        Page<Product> searchProducts = productService.getProductsBySearchWord(searchWord,pageNumber);

        searchProducts.getContent().forEach(product -> {
            Assertions.assertThat(product.getStatus()).isIn(ProductStatus.판매중, ProductStatus.예약);
        });

    }

    @Test
    @Transactional
    void getSalesContractProducts() {
        int pageNumber = 1;
        Page<Product> salesContractProducts = productService.getSalesContractProducts(userKey, pageNumber);
        salesContractProducts.getContent().forEach(product -> {
            Assertions.assertThat(product.getMember().getUserKey()).isEqualTo(userKey);
            Assertions.assertThat(product.getStatus()).isEqualTo(ProductStatus.판매대기);
        });
    }

    @Test
    @Transactional
    void getSalesContractProduct() {
        Long orderId = 512054L;

        Optional<Order> salesContractProduct = productService.getSalesContractProduct(userKey, orderId);

        Assertions.assertThat(salesContractProduct).isNotEmpty();
        Assertions.assertThat(salesContractProduct.get().getProduct().getStatus()).isEqualTo(ProductStatus.판매대기);
        Assertions.assertThat(salesContractProduct.get().getStatus()).isEqualTo(OrderStatus.주문체결);
        Assertions.assertThat(salesContractProduct.get().getPaymentInfo().getStatus()).isEqualTo(PaymentStatus.결제완료);
    }

    @Test
    @Transactional
    void getPurchaseProducts() {
        int pageNumber = 1;

        Page<Order> purchaseProducts = productService.getPurchaseProducts(userKey, pageNumber);

        purchaseProducts.getContent().forEach(order -> {
            Assertions.assertThat(order.getProduct().getStatus()).isEqualTo(ProductStatus.판매대기);
            Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.주문체결);
            Assertions.assertThat(order.getPaymentInfo().getStatus()).isEqualTo(PaymentStatus.결제완료);
        });
    }

    @Test
    @Transactional
    void getPurchaseProduct() {
        Long orderId = 512054L;
        Optional<Order> purchaseProduct = productService.getPurchaseProduct(userKey, orderId);

        Assertions.assertThat(purchaseProduct).isNotEmpty();
        Assertions.assertThat(purchaseProduct.get().getProduct().getStatus()).isEqualTo(ProductStatus.판매대기);
        Assertions.assertThat(purchaseProduct.get().getStatus()).isEqualTo(OrderStatus.주문체결);
        Assertions.assertThat(purchaseProduct.get().getPaymentInfo().getStatus()).isEqualTo(PaymentStatus.결제완료);

    }

    @Test
    @Transactional
    void updateProductStatus() {
        Response response = productService.updateProductStatus(productId, ProductStatus.숨김);

        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK.getValue());
    }

    @Test
    @Transactional
    void deleteProduct() {
        Response response = productService.deleteProduct(productId);

        Assertions.assertThat(response.getCode()).isEqualTo(ResultCode.OK.getValue());
    }
}