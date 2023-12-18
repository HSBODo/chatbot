package site.pointman.chatbot.domain.response.property.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.domain.response.property.Component;

@Setter
@Getter
public class SimpleText extends Component {
    private String text;

    public SimpleText(String text) {
        this.text = text;
    }
    /**
     * text(필수값)가 500자가 넘는 경우, 500자 이후의 글자는 생략되고 전체 보기 버튼을 통해서 전체 내용을 확인할 수 있습니다.
     */
}
