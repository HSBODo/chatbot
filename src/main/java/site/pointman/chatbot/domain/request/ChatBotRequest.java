package site.pointman.chatbot.domain.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import site.pointman.chatbot.domain.request.propery.*;
import site.pointman.chatbot.domain.response.property.common.Extra;
import site.pointman.chatbot.dto.product.ProductDto;

import java.util.List;


@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatBotRequest {
    private Intent intent;
    private UserRequest userRequest;
    private Bot bot;
    private Action action;
    private Value value;
    private List<Context> contexts;

    public ProductDto createProductDto(){
        return ProductDto.builder()
                .name(getProductName())
                .price(Long.parseLong(getProductPrice()))
                .description(getProductDescription())
                .tradingLocation(getTradingLocation())
                .kakaoOpenChatUrl(getKakaoOpenChatUrl())
                .build();
    }
    public String getUserKey(){
        return userRequest.getUser().getProperties().getPlusfriendUserKey();
    }
    public String getCustomerName(){
        return action.getParams().getCustomerName();
    }
    public String getCustomerPhone(){
        return action.getParams().getCustomerPhone();
    }
    public String getProductName(){
        return action.getParams().getProductName();
    }
    public String getProductDescription(){
        return action.getParams().getProductDescription();
    }
    public String getProductPrice(){
        return action.getParams().getProductPrice();
    }
    public List<String> getProductImages(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String ProductImgStr = this.action.getParams().getProductImg();
            ProductImg productImg = mapper.readValue(ProductImgStr, ProductImg.class);
            return productImg.getImgUrlList();
        }catch (Exception e){
            return null;
        }
    }
    public String getTradingLocation(){
        return action.getParams().getTradingLocation();
    }
    public String getKakaoOpenChatUrl(){
        return action.getParams().getKakaoOpenChatUrl();
    }
    public String getProductId(){
        action.getClientExtra().getProductId();
        if(!action.getClientExtra().getProductId().isEmpty()){
            return action.getClientExtra().getProductId();
        }
        Extra extraObj = new Extra(action.getClientExtra().getExtra());
        return extraObj.getProductId();
    }
    public String getChoiceParam(){
        return action.getClientExtra().getChoice();
    }
    public String getValidationData(){
        return value.getOrigin();
    }
    public String getAccessToken() {
        try {
            return getContexts().get(0).getParams().get("accessToken").getValue();
        }catch (Exception e){
            return null;
        }
    }
    public String getUtterance(){
        return userRequest.getUtterance();
    }
}
