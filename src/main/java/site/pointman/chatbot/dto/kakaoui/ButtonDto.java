package site.pointman.chatbot.dto.kakaoui;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @NoArgsConstructor
public class ButtonDto {
    /**
     * 필드명	        타입	                필수 여부	                설명	                                                                        제한
     * label	        string	                O	                    버튼에 적히는 문구입니다.	                                                    버튼 14자(가로배열 2개 8자)
     * action	        string	                O	                    버튼 클릭시 수행될 작업입니다.
     *
     * webLinkUrl	    string	            action: webLink	            웹 브라우저를 열고 webLinkUrl 의 주소로 이동합니다.	                            URL
     * messageText	    string	            action: message or block    message: 사용자의 발화로 messageText를 내보냅니다. (바로가기 응답의 메세지 연결 기능과 동일)
     *                                                                  block: 블록 연결시 사용자의 발화로 노출됩니다.
     *
     * phoneNumber	    string	            action: phone	            phoneNumber에 있는 번호로 전화를 겁니다.	        전화번호
     * blockId	        string	            action: block	            blockId를 갖는 블록을 호출합니다. (바로가기 응답의 블록 연결 기능과 동일)	            존재하는 블록 id
     * extra	        Map<String, Any>		                        block이나 message action으로 블록 호출시, 스킬 서버에 추가적으로 제공하는 정보
     */

    private ButtonAction action;
    private String label;
    private String webLinkUrl;
    private String blockId;
    private String messageText;
    private Map<String,String> extra;


    public ButtonDto(ButtonAction action, String label, String webLinkUrl) {
        this.action = action;
        this.label = label;
        this.webLinkUrl = webLinkUrl;
    }
    public ButtonDto(ButtonAction action, String label, Map<String,String> extra) {
        this.action = action;
        this.label = label;
        this.extra = extra;
    }
    public ButtonDto createQuickButton(String label,String blockId,Map<String,String> params){
        this.action = ButtonAction.block;
        this.label = label;
        this.blockId= blockId;
        this.extra = params;
        return this;
    }
    public ButtonDto createQuickButton(String label,String blockId){
        this.action = ButtonAction.block;
        this.label = label;
        this.blockId= blockId;

        return this;
    }



}
