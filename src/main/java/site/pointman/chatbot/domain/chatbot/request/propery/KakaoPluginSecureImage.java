package site.pointman.chatbot.domain.chatbot.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class KakaoPluginSecureImage {
    private String privacyAgreement;
    private int imageQuantity;
    private String secureUrls;
    private String expire;

    public List getImgUrlList(){
        String secureUrls = this.secureUrls;
        secureUrls = secureUrls.substring(5, secureUrls.length() - 1);
        return Arrays.asList(secureUrls.split(","));
    }
}
