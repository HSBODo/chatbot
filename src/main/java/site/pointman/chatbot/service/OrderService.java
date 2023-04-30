package site.pointman.chatbot.service;

import org.json.simple.JSONObject;

public interface OrderService {

    JSONObject createItemOption(String kakaoUserkey,Long itemCode, int optionNum) throws Exception;
}
