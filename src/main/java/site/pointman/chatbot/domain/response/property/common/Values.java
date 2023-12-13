package site.pointman.chatbot.domain.response.property.common;


import lombok.Getter;
import site.pointman.chatbot.domain.response.property.Context;

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
