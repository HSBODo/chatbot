package site.pointman.chatbot.domain.payment.kakaopay.response;


import lombok.Getter;

@Getter
public class KakaoPaymentReadyResponse {
    private String tid;
    private String tms_result;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String ios_app_scheme;
    private String created_at;
}
