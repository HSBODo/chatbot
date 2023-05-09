package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.dto.kakaoui.BlockDto;

public interface BlockService {
    JSONObject createBlock(BlockDto blockDto) throws Exception;
    JSONObject findByService(String kakaoUserkey, site.pointman.chatbot.dto.BlockDto blockDto, JSONObject buttonParams) throws Exception;
}
