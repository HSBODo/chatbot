package site.pointman.chatbot.dto.response.property.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonInclude()
@Getter
public class Carousel<T> {
    private String type;
    private List<T> items = new ArrayList<>();

    public void addComponent(T component){
        this.type = convertClassNameToCamelcase(component.getClass().getSimpleName());
        this.items.add(component);
    }

    private String convertClassNameToCamelcase(String className){
        String first = className.substring(0,1).toLowerCase();
        className = first+className.substring(1);
        return className;
    }
}
