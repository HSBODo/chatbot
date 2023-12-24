package site.pointman.chatbot.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpecialProduct {
    private String productThumbnailImageUrl;
    private String brandName;
    private String brandImageUrl;
    private String title;
    private String price;
    private String category;
    private String detailInfoUrl;
    private String purchaseUrl;
    private String status;

    @Builder
    public SpecialProduct(String productThumbnailImageUrl, String brandName, String brandImageUrl, String title, String price, String category, String detailInfoUrl, String purchaseUrl, String status) {
        this.productThumbnailImageUrl = productThumbnailImageUrl;
        this.brandName = brandName;
        this.brandImageUrl = brandImageUrl;
        this.title = title;
        this.price = price;
        this.category = category;
        this.detailInfoUrl = detailInfoUrl;
        this.purchaseUrl = purchaseUrl;
        this.status = status;
    }

    public int getFormatPrice() {
        int first = price.indexOf(" ")+1;
        int last = price.indexOf(" ",first);
        int formatPrice = Integer.parseInt(price.substring(first, last).replaceAll(",",""));

        if (formatPrice == 0) formatPrice = 1;
        return formatPrice;
    }

    public String getBrandNameAndStatus(){
        if (status.equals("인기")) return brandName+"("+status+")";
        return brandName;
    }

    public String getCurrency(){
        int first = price.lastIndexOf(" ");
        return price.substring(first + 1).replaceAll("\\(", "").replaceAll("\\)","");
    }

}
