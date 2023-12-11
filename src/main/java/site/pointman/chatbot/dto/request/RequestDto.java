package site.pointman.chatbot.dto.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import site.pointman.chatbot.dto.request.propery.*;

import java.util.List;


@Getter
public class RequestDto {
    private Intent intent;
    private UserRequest userRequest;
    private Bot bot;
    private Action action;
    private List<Context> contexts;

    public String getUserKey(){
        return this.userRequest.getUser().getProperties().getPlusfriendUserKey();
    }
    public String getEnterName(){
        return this.action.getParams().getEnterName();
    }
    public String getEnterPhone(){
        return this.action.getParams().getEnterPhone();
    }
    public String getEnterAccount(){
        return this.action.getParams().getEnterAccount();
    }
    public String getProductName(){
        return this.action.getParams().getProductName();
    }
    public String getProductDescription(){
        return this.action.getParams().getProductDescription();
    }
    public String getProductPrice(){
        return this.action.getParams().getProductPrice();
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

    public String getAccessToken() {
        try {
            return getContexts().get(0).getParams().get("accessToken").getValue();
        }catch (Exception e){
            return null;
        }
    }
}
