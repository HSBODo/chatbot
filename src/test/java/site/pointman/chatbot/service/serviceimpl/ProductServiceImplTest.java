package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductImgDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.utill.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class ProductServiceImplTest {
    @Autowired
    AuthService authService;

    @Autowired
    ProductService productService;

    HttpUtils httpUtils;


    @Test
    void getProducts() {
        OAuthTokenDto token = authService.createToken();
        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://api.commerce.naver.com/external/v1/products/search";
        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token.getAccessToken());
        headers.put("Content-type","application/json");

        Map<String,Object> body = new HashMap<>();

        body.put("searchKeywordType","SELLER_CODE");
//        body.put("channelProductNos","");
//        body.put("originProductNos","");
        body.put("sellerManagementCode","1");
        body.put("productStatusTypes","SALE");
        body.put("page","1");
        body.put("size","10");
        body.put("orderType","NO");
        body.put("periodType","SALE_START_DAY");
        body.put("fromDate","2023-01-01");
        body.put("toDate","2023-10-03");

        String httpResponse = httpUtils.post(url,headers, body, mediaType);
        log.info("result = {}",httpResponse);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            ProductListDto productDto = mapper.readValue(httpResponse, ProductListDto.class);
            log.info("productNo= {}",productDto.getProduct(0).getImageUrl());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test
    void getProduct() {
        OAuthTokenDto token = authService.createToken();
        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://api.commerce.naver.com/external/v2/products/origin-products/9231360506";
        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token.getAccessToken());
        headers.put("Content-type","application/json");

        String httpResponse = httpUtils.get(url,headers);
        log.info("result = {}",httpResponse);
    }

    @Test
    void getProductsImage() {
        OAuthTokenDto token = authService.createToken();
        ProductListDto products = productService.getProducts(token);
        for(int i = 0 ; i < products.getProductListDto().size(); i++){
            String url = "https://api.commerce.naver.com/external/v2/products/origin-products/"+products.getProduct(i).getOriginProductNo();
            Map<String,Object> headers = new HashMap<>();
            headers.put("Authorization","Bearer "+token.getAccessToken());
            headers.put("Content-type","application/json");

            String httpResponse = httpUtils.get(url,headers);
            log.info("result = {}",httpResponse);

            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
                ProductImgDto productDto = mapper.readValue(httpResponse, ProductImgDto.class);

                Assertions.assertThat(productDto.getImageUrl()).isNotNull();

                products.getProduct(i).setImageUrl(productDto.getImageUrl());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }

        }
        log.info("ImageUrl= {}",products.getProduct(1).getImageUrl());

    }

}