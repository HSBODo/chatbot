package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.dto.block.BlockDto;

public interface BlockService {
    JSONObject createBlock(BlockDto blockDto) throws Exception;
    JSONObject findByService(String kakaoUserkey, BlockDto blockDto, JSONObject buttonParams) throws Exception;
}
