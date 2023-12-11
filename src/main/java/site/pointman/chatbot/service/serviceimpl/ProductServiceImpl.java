package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductImgDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.dto.request.RequestDto;
import site.pointman.chatbot.dto.request.propery.ProductImg;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.property.Context;
import site.pointman.chatbot.dto.response.property.common.Buttons;
import site.pointman.chatbot.dto.response.property.common.Extra;
import site.pointman.chatbot.dto.response.property.common.QuickReplyButtons;
import site.pointman.chatbot.dto.response.property.common.Thumbnail;
import site.pointman.chatbot.dto.response.property.components.BasicCard;
import site.pointman.chatbot.dto.response.property.components.Carousel;
import site.pointman.chatbot.dto.response.property.components.SimpleImage;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.utill.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    AuthService authService;

    public ProductServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    HttpUtils httpUtils;

    @Override
    public ProductListDto getProducts(OAuthTokenDto tokenDto) {
        ProductListDto productDto = null;

        MediaType mediaType = MediaType.parse("application/json");
        String productsUrl = "https://api.commerce.naver.com/external/v1/products/search";

        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+tokenDto.getAccessToken());
        headers.put("Content-type","application/json");

        Map<String,Object> body = new HashMap<>();
        body.put("searchKeywordType","SELLER_CODE");
//      body.put("channelProductNos","");
//      body.put("originProductNos","");
        body.put("sellerManagementCode","1");
        body.put("productStatusTypes","SALE");
        body.put("page","1");
        body.put("size","10");
        body.put("orderType","NO");
        body.put("periodType","SALE_START_DAY");
        body.put("fromDate","2023-01-01");
        body.put("toDate","2023-10-03");

        String httpResponse = httpUtils.post(productsUrl,headers, body, mediaType);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            productDto = mapper.readValue(httpResponse, ProductListDto.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        // 상품 이미지 세팅
        for(int i = 0 ; i < productDto.getProductListDto().size(); i++){
            String url = "https://api.commerce.naver.com/external/v2/products/origin-products/"+productDto.getProduct(i).getOriginProductNo();
            httpResponse = httpUtils.get(url,headers);
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
                ProductImgDto productImgDto = mapper.readValue(httpResponse, ProductImgDto.class);
                productDto.getProduct(i).setImageUrl(productImgDto.getImageUrl());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        return productDto;
    }


    @Override
    public ResponseDto createAddValidation(RequestDto requestDto) {
        ResponseDto responseDto = new ResponseDto();
        Buttons buttons = new Buttons();
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();
        QuickReplyButtons quickReplyButtons = new QuickReplyButtons();
        Extra extra = new Extra();

        quickReplyButtons.addBlockQuickButton("취소","65262b36ddb57b43495c18f8",null);
        quickReplyButtons.addBlockQuickButton("등록","652e659087e33b27c8ba3a4a",extra);

        String accessToken = requestDto.getAccessToken();
        String productName = requestDto.getProductName();
        String productDescription = requestDto.getProductDescription();
        int productPrice = Integer.parseInt(requestDto.getProductPrice());
        ProductImg productImg = requestDto.getProductImg();
        List<String> imgUrlList = productImg.getImgUrlList();

        imgUrlList.forEach(imgUrl -> {
            BasicCard basicCard = new BasicCard();
            Thumbnail thumbnail = new Thumbnail(imgUrl,null,true);
            basicCard.setThumbnail(thumbnail);
            basicCardCarousel.addComponent(basicCard);
        });


        Context productContext = new Context("product",1,600);
        productContext.addParam("accessToken",accessToken);


        responseDto.addCarousel(basicCardCarousel);
        responseDto.addCommerceCard(productName,productDescription,productPrice,0,imgUrlList.get(0),"","",buttons);
        responseDto.addQuickButton(quickReplyButtons);

        responseDto.addContext(productContext);

        return responseDto;
    }
}
