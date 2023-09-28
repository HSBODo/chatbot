package site.pointman.chatbot.dto.response.property.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.dto.response.property.Context;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Values {
    private List<Context> values;

    public Values() {
        this.values = new ArrayList<>();
    }

    public void addContext(Context context){
        this.values.add(context);
    }
}
