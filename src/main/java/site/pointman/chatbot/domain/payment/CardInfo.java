package site.pointman.chatbot.domain.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class CardInfo {

    private String interestFreeInstall;
    private String bin;
    private String cardType;
    private String cardMid;
    private String approvedId;
    private String installMonth;
    private String purchaseCorp;
    private String purchaseCorpCode;
    private String issuerCorp;
    private String issuerCorpCode;
    private String kakaopayPurchaseCorp;
    private String kakaopayPurchaseCorpCode;
    private String kakaopayIssuerCorp;
    private String kakaopayIssuerCorpCode;

    /**
     * kakaopay_purchase_corp	String	카카오페이 매입사명
     * kakaopay_purchase_corp_code	String	카카오페이 매입사 코드
     * kakaopay_issuer_corp	String	카카오페이 발급사명
     * kakaopay_issuer_corp_code	String	카카오페이 발급사 코드
     * bin	String	카드 BIN
     * card_type	String	카드 타입
     * install_month	String	할부 개월 수
     * approved_id	String	카드사 승인번호
     * card_mid	String	카드사 가맹점 번호
     * interest_free_install	String	무이자할부 여부(Y/N)
     * card_item_code	String	카드 상품 코드
     */

    @Builder
    public CardInfo(String interestFreeInstall, String bin, String cardType, String cardMid, String approvedId, String installMonth, String purchaseCorp, String purchaseCorpCode, String issuerCorp, String issuerCorpCode, String kakaopayPurchaseCorp, String kakaopayPurchaseCorpCode, String kakaopayIssuerCorp, String kakaopayIssuerCorpCode) {
        this.interestFreeInstall = interestFreeInstall;
        this.bin = bin;
        this.cardType = cardType;
        this.cardMid = cardMid;
        this.approvedId = approvedId;
        this.installMonth = installMonth;
        this.purchaseCorp = purchaseCorp;
        this.purchaseCorpCode = purchaseCorpCode;
        this.issuerCorp = issuerCorp;
        this.issuerCorpCode = issuerCorpCode;
        this.kakaopayPurchaseCorp = kakaopayPurchaseCorp;
        this.kakaopayPurchaseCorpCode = kakaopayPurchaseCorpCode;
        this.kakaopayIssuerCorp = kakaopayIssuerCorp;
        this.kakaopayIssuerCorpCode = kakaopayIssuerCorpCode;
    }
}
