package site.pointman.chatbot.domain.request.propery;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ProductImg {
    private String privacyAgreement;
    private int imageQuantity;
    private String secureUrls;
    private String expire;

    public List getImgUrlList(){
        String secureUrls = this.secureUrls;
        secureUrls = secureUrls.substring(5, secureUrls.length() - 1);

        List<String> imgUrlList = new ArrayList<>();
        imgUrlList = Arrays.asList(secureUrls.split(","));

        return imgUrlList;
    }
}
