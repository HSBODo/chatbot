package site.pointman.chatbot.dto.product;

import lombok.Getter;

import java.util.List;

@Getter
public class Images {
    private imgUrl representativeImage;
    private List<imgUrl> optionalImages;
}
