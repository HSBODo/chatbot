package site.pointman.chatbot.vo.kakaoui;

import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.block.BlockServiceType;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ButtonParamsVo {
    String blockId;
    BlockServiceType blockService;
    Map<String,String> buttonParams;

    public ButtonParamsVo(String blockId, BlockServiceType blockService) {
        this.blockId = blockId;
        this.blockService = blockService;
        this.buttonParams = new HashMap<>();
    }
    public void addButtonParam(String key, String value){
        buttonParams.put(key,value);
    }
    public Map<String,String> createButtonParams(){
        buttonParams.put("blockId",blockId);
        buttonParams.put("blockService", blockService.name());
        return buttonParams;
    }


}
