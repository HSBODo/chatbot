package site.pointman.chatbot.service.serviceimpl;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.service.OrderService;
@Service
public class OrderServiceImpl implements OrderService {

    private KakaoJsonUiService kakaoJsonUiService;

    public OrderServiceImpl(KakaoJsonUiService kakaoJsonUiService) {
        this.kakaoJsonUiService = kakaoJsonUiService;
    }

    @Override
    public JSONObject createItemOption(String kakaoUserkey, Long itemCode, int optionNum) throws Exception {

        return null;
    }
}
