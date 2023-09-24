package site.pointman.chatbot.dto.response.property.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import site.pointman.chatbot.dto.response.property.common.Link;


@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Thumbnail {
    private String imageUrl;
    private Link link;
    private boolean fixedRatio;

    public Thumbnail(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Thumbnail(String imageUrl, Link link) {
        this.imageUrl = imageUrl;
        this.link = link;
    }

    public Thumbnail(String imageUrl, Link link, boolean fixedRatio) {
        this.imageUrl = imageUrl;
        this.link = link;
        this.fixedRatio = fixedRatio;
    }
}
