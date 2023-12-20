package site.pointman.chatbot.domain.payment.kakaopay;


import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class PaymentCancelRequest {
    private String cid;
    private String tid;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
    private int cancelVatAmount;
    private int cancelAvailableAmount;

    public PaymentCancelRequest(String cid, String tid, int cancelAmount, int cancelTaxFreeAmount, int cancelVatAmount, int cancelAvailableAmount) {
        this.cid = cid;
        this.tid = tid;
        this.cancelAmount = cancelAmount;
        this.cancelTaxFreeAmount = cancelTaxFreeAmount;
        this.cancelVatAmount = cancelVatAmount;
        this.cancelAvailableAmount = cancelAvailableAmount;
    }

    /**
     * cid	String	가맹점 코드, 10자	O
     * cid_secret	String	가맹점 코드 인증키, 24자, 숫자+영문 소문자 조합	X
     * tid	String	결제 고유번호	O
     * cancel_amount	Integer	취소 금액	O
     * cancel_tax_free_amount	Integer	취소 비과세 금액	O
     * cancel_vat_amount	Integer	취소 부가세 금액
     * 요청 시 값을 전달하지 않을 경우, (취소 금액 - 취소 비과세 금액)/11, 소숫점이하 반올림	X
     * cancel_available_amount	Integer	취소 가능 금액(결제 취소 요청 금액 포함)	X
     * payload	String	해당 요청에 대해 저장하고 싶은 값, 최대 200자	X
     */

    public MultiValueMap<String, String> getConvertRequestEntity(){
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("cid", cid);
        requestEntity.add("tid", tid);
        requestEntity.add("cancel_amount", String.valueOf(cancelAmount));
        requestEntity.add("cancel_tax_free_amount", String.valueOf(cancelTaxFreeAmount));
        requestEntity.add("cancel_vat_amount", String.valueOf(cancelVatAmount));
        return requestEntity;
    }
}
