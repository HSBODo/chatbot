package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.KakaoProductService;
import site.pointman.chatbot.service.ProductService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/*")
public class KakaoRestAPI {

    @Value("${kakao.channel.url}")
    private String kakaoChannelUrl;

    AuthService authService;
    ProductService productService;
    KakaoProductService kakaoProductService;

    public KakaoRestAPI(AuthService authService, ProductService productService, KakaoProductService kakaoProductService) {
        this.authService = authService;
        this.productService = productService;
        this.kakaoProductService = kakaoProductService;
    }

    @ResponseBody
    @RequestMapping(value = "productsInfo" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto productsInfo(@RequestBody ChatBotRequest request) throws Exception {
        ResponseDto response = new ResponseDto();

        try {
            log.info("request ={}", request.getUserRequest().getUser().getProperties().getPlusfriendUserKey());

            OAuthTokenDto token = authService.createToken();
            ProductListDto products = productService.getProducts(token);
            ResponseDto productsInfo = kakaoProductService.createProductsInfo(products);

            return productsInfo;
        }catch (Exception e){

            response.addSimpleText("시스템 오류");
            return response;
        }
    }

}
