package site.pointman.chatbot.dto.product;

import lombok.Getter;

@Getter
public class ProductDto {
    private String originProductNo;
    private String channelProductNo;
    private String channelServiceType;
    private String categoryId;
    private String name;
    private String sellerManagementCode;
    private String statusType;
    private String channelProductDisplayStatusType;
    private int salePrice;
    private int discountedPrice;
    private int mobileDiscountedPrice;
    private int stockQuantity;
    private Boolean knowledgeShoppingProductRegistration;
    private String deliveryAttributeType;
    private int deliveryFee;
    private int returnFee;
    private int exchangeFee;
    private int sellerPurchasePoint;
    private String sellerPurchasePointUnitType;
    private int managerPurchasePoint;
    private int textReviewPoint;
    private int photoVideoReviewPoint;
    private String regDate;
    private String modifiedDate;
    private String imageUrl;

    public void setImageUrl(String imgUrl){
        this.imageUrl = imgUrl;
    };
}
