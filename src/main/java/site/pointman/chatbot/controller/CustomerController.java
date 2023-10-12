package site.pointman.chatbot.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.property.common.Extra;
import site.pointman.chatbot.dto.response.property.common.QuickReplyButtons;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.KakaoProductService;
import site.pointman.chatbot.service.ProductService;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/customer")
public class CustomerController {

    @Value("${kakao.channel.url}")
    private String kakaoChannelUrl;

    AuthService authService;
    ProductService productService;
    KakaoProductService kakaoProductService;

    public CustomerController(AuthService authService, ProductService productService, KakaoProductService kakaoProductService) {
        this.authService = authService;
        this.productService = productService;
        this.kakaoProductService = kakaoProductService;
    }
    @ResponseBody
    @RequestMapping(value = "join" , headers = {"Accept=application/json; UTF-8"})
    public ResponseDto join(@RequestBody RequestDto request) throws Exception {
        ResponseDto response = new ResponseDto();

        try {
            log.info("req = {}", request.getAction().getParams().getJoinName());
            QuickReplyButtons quickReplyButtons = new QuickReplyButtons();
            Extra extra = new Extra();
            quickReplyButtons.addBlockQuickButton("처음으로","65262b36ddb57b43495c18f8",extra);
            response.addSimpleText("회원가입을 완료하였습니다.");
            response.addQuickButton(quickReplyButtons);
            return response;
        }catch (Exception e){
            response.addSimpleText("시스템 오류");
            return response;
        }
    }

}
