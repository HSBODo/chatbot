package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.dto.kakaopay.PaymentReadyRequestDto;
import site.pointman.chatbot.dto.kakaopay.PaymentReadyResponseDto;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.utill.HttpUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${kakao.admin.key}")
    private String SERVICE_APP_ADMIN_KEY;

    @Value("${kakao.pay.cid}")
    private String CID;

    @Value("${kakao.pay.approval_url}")
    private String KAKAO_APPROVAL_URL;

    @Value("${kakao.pay.cancel_url}")
    private String KAKAO_CANCEL_URL;

    @Value("${kakao.pay.fail_url}")
    private String KAKAO_FAIL_URL;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getKakaoPaymentReadyUrl(Product product, Member member) {
        try {
            Map<String,Object> headers = new HashMap<>();
            Map<String, Object> body = new HashMap<>();

            headers.put("Authorization","KakaoAK "+SERVICE_APP_ADMIN_KEY);
            headers.put("Content-type","application/x-www-form-urlencoded;charset=utf-8");
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

            String productId = String.valueOf(product.getId());
            String productName = product.getName();
            int productPrice = product.getPrice().intValue();

            String buyerMemberName = member.getName();

            PaymentReadyRequestDto paymentReadyRequestDto = PaymentReadyRequestDto.builder()
                    .cid(CID)
                    .partnerOrderId(productId)
                    .partnerUserId(buyerMemberName)
                    .itemName(productName)
                    .quantity(1)
                    .totalAmount(productPrice)
                    .taxFreeAmount(0)
                    .approvalUrl(KAKAO_APPROVAL_URL)
                    .cancelUrl(KAKAO_CANCEL_URL)
                    .failUrl(KAKAO_FAIL_URL)
                    .build();

            String apiURL = "https://kapi.kakao.com/v1/payment/ready";
            StringBuilder urlBuilder = new StringBuilder(apiURL); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("cid","UTF-8") + "="+ paymentReadyRequestDto.getCid());
            urlBuilder.append("&" + URLEncoder.encode("partner_order_id","UTF-8") + "="+ paymentReadyRequestDto.getPartnerOrderId());
            urlBuilder.append("&" + URLEncoder.encode("partner_user_id","UTF-8") + "="+ paymentReadyRequestDto.getPartnerUserId());
            urlBuilder.append("&" + URLEncoder.encode("item_name","UTF-8") + "="+paymentReadyRequestDto.getItemName());
            urlBuilder.append("&" + URLEncoder.encode("quantity","UTF-8") + "="+ paymentReadyRequestDto.getQuantity());
            urlBuilder.append("&" + URLEncoder.encode("total_amount","UTF-8") + "="+ paymentReadyRequestDto.getTotalAmount());
            urlBuilder.append("&" + URLEncoder.encode("tax_free_amount","UTF-8") + "="+ paymentReadyRequestDto.getTaxFreeAmount());
            urlBuilder.append("&" + URLEncoder.encode("approval_url","UTF-8") + "="+ "https://www.pointman.shop/kakaochatbot/order/kakaopay-approve/"+productId);
            urlBuilder.append("&" + URLEncoder.encode("fail_url","UTF-8") + "="+ "https://www.pointman.shop/kakaochatbot/order/kakaopay-fail/"+productId);
            urlBuilder.append("&" + URLEncoder.encode("cancel_url","UTF-8") + "="+ "https://www.pointman.shop/kakaochatbot/order/kakaopay-cancel/"+productId);

            String response = HttpUtils.post(urlBuilder.toString(), headers,body,mediaType);

            PaymentReadyResponseDto paymentReadyResponseDto = mapper.readValue(response, PaymentReadyResponseDto.class);

            String next_redirect_mobile_url = paymentReadyResponseDto.getNext_redirect_mobile_url();

            return next_redirect_mobile_url;
        }catch (Exception e) {
            throw new IllegalArgumentException("결제 준비하기 실패 e ="+e.getMessage());
        }
    }
}
