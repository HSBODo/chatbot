package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.dto.response.ResponseDto;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
class KakaoProductServiceTest {

    @Autowired
    KakaoProductService kakaoProductService;
    @Autowired
    ProductService productService;
    @Autowired
    AuthService authService;
    @Test
    void createProductsInfo() {
        OAuthTokenDto token = authService.createToken();

        ProductListDto products = productService.getProducts(token);

        ResponseDto productsInfo = kakaoProductService.createProductsInfo(products);

        log.info("response = {}",productsInfo.getTemplate());

    }
}