package site.pointman.chatbot.domain.chatbot.request;


import antlr.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import site.pointman.chatbot.domain.chatbot.request.propery.*;
import site.pointman.chatbot.domain.member.dto.MemberJoinDto;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.product.dto.ProductDto;

import java.util.List;
import java.util.Objects;


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
                .userKey(getUserKey())
                .name(getProductName())
                .price(Long.parseLong(getProductPrice()))
                .description(getProductDescription())
                .tradingLocation(getTradingLocation())
                .kakaoOpenChatUrl(getKakaoOpenChatUrl())
                .category(getCategory())
                .imageUrls(getProductImages())
                .build();
    }

    public Category getCategory(){
        if (Objects.nonNull(getContexts().get(0).getParams().get("productCategory").getValue())) {
            String productCategory =getContexts().get(0).getParams().get("productCategory").getValue();

            return Category.getCategory(productCategory);
        }
        return null;
    }

    public String getUserKey(){
        return userRequest.getUser().getProperties().getPlusfriendUserKey();
    }

    public String getCustomerName(){
        if (Objects.isNull(action.getParams().getCustomerName())) return null;
        return action.getParams().getCustomerName();
    }

    public String getCustomerPhone(){
        if (Objects.isNull(action.getParams().getCustomerPhone())) return null;
        return action.getParams().getCustomerPhone();
    }

    public String getProductName(){
        if (Objects.isNull(action.getParams().getProductName())) return null;
        return action.getParams().getProductName();
    }

    public String getProductDescription(){
        if (Objects.isNull(action.getParams().getProductDescription())) return null;
        return action.getParams().getProductDescription();
    }

    public String getProductPrice(){
        if (Objects.isNull(action.getParams().getProductPrice())) return null;
        return action.getParams().getProductPrice();
    }

    public List<String> getProductImages(){
        try {
            if (Objects.isNull(action.getParams().getProductImg())) return null;

            ObjectMapper mapper = new ObjectMapper();
            String productImage = this.action.getParams().getProductImg();
            KakaoPluginSecureImage kakaoPluginSecureImage = mapper.readValue(productImage, KakaoPluginSecureImage.class);
            return kakaoPluginSecureImage.getImgUrlList();
        }catch (Exception e){
            return null;
        }
    }

    public MemberJoinDto getMemberJoinDto(){
        return MemberJoinDto.builder()
                .userKey(getUserKey())
                .name(getCustomerName())
                .phoneNumber(getCustomerPhone())
                .build();
    }

    public String getCustomerProfileImage(){
        try {
            if (Objects.isNull(action.getParams().getCustomerProfileImage())) return null;

            ObjectMapper mapper = new ObjectMapper();
            String customerProfileImage = this.action.getParams().getCustomerProfileImage();
            KakaoPluginSecureImage kakaoPluginSecureImage = mapper.readValue(customerProfileImage, KakaoPluginSecureImage.class);
            return kakaoPluginSecureImage.getImgUrlList().get(0);
        }catch (Exception e){
            return null;
        }
    }

    public String getTradingLocation(){
        if (Objects.isNull(action.getParams().getTradingLocation())) return null;
        return action.getParams().getTradingLocation();
    }

    public String getKakaoOpenChatUrl(){
        if (Objects.isNull(action.getParams().getKakaoOpenChatUrl())) return null;
        return action.getParams().getKakaoOpenChatUrl();
    }

    public String getReservationCustomerName(){
        if (Objects.isNull(action.getParams().getReservationCustomerName())) return null;
        return action.getParams().getReservationCustomerName();
    }

    public String getTrackingNumber(){
        if (Objects.isNull(action.getParams().getTrackingNumber())) return null;
        return action.getParams().getTrackingNumber();
    }

    public String getSearchWord(){
        if (Objects.isNull(action.getParams().getSearchWord())){
            if (Objects.isNull(action.getClientExtra().getSearchWord())) return null;
            return action.getClientExtra().getSearchWord();
        }
        return action.getParams().getSearchWord();
    }

    public String getChoiceParam(){
        if (Objects.isNull(action.getClientExtra().getChoice())) return null;
        return action.getClientExtra().getChoice();
    }

    public String getNoticeId(){
        if (Objects.isNull(action.getClientExtra().getNoticeId())) return null;
        return action.getClientExtra().getNoticeId();
    }

    public String getProductId(){
        if (Objects.isNull(action.getClientExtra().getProductId())) return null;
        return action.getClientExtra().getProductId();
    }

    public String getOrderId(){
        if (Objects.isNull(action.getClientExtra().getOrderId())) return null;
        return action.getClientExtra().getOrderId();
    }

    public String getProductStatus(){
        if (Objects.isNull(action.getClientExtra().getProductStatus())) return null;
        return action.getClientExtra().getProductStatus();
    }

    public int getPageNumber(){
        if (Objects.isNull(action.getClientExtra().getPageNumber())) return 0;
        return Integer.parseInt(action.getClientExtra().getPageNumber());
    }

    public int getFirstNumber(){
        if (Objects.isNull(action.getClientExtra().getFirstNumber())) return 0;
        return Integer.parseInt(action.getClientExtra().getFirstNumber());
    }

    public int getLastNumber(){
        if (Objects.isNull(action.getClientExtra().getLastNumber())) return 0;
        return Integer.parseInt(action.getClientExtra().getLastNumber());
    }

    public String getValidationData(){
        if (Objects.isNull(value.getOrigin())) return null;
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

    public String getRequestBlockId(){
        return userRequest.getBlock().getId();
    }
}
