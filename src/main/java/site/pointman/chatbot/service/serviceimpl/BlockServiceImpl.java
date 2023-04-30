package site.pointman.chatbot.service.serviceimpl;

import org.json.simple.JSONObject;
import site.pointman.chatbot.dto.BlockDto;
import site.pointman.chatbot.service.BlockService;

public class BlockServiceImpl implements BlockService {




    @Override
    public JSONObject createBlock(String kakaoUserkey, BlockDto blockDto) throws Exception {
        blockDto.getService();

        return null;
    }
}
