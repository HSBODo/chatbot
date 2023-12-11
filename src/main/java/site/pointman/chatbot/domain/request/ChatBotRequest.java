package site.pointman.chatbot.domain.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import site.pointman.chatbot.domain.request.propery.*;

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
    public ProductImg getProductImg(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String ProductImgStr = this.action.getParams().getProductImg();
            ProductImg productImg = mapper.readValue(ProductImgStr, ProductImg.class);
            return productImg;
        }catch (Exception e){
            return null;
        }
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
}
