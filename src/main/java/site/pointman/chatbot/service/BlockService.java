package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.dto.BlockDto;
import site.pointman.chatbot.vo.kakaoui.BlockVo;

public interface BlockService {
    JSONObject createBlock(BlockVo blockVo) throws Exception;
    JSONObject findByService(String kakaoUserkey, BlockDto blockDto, JSONObject buttonParams) throws Exception;
}
