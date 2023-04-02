package site.pointman.chatbot.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LocationXY {
    String x;
    String y;
    int mode;

    public LocationXY(String x, String y, int mode) {
        this.x = x;
        this.y = y;
        this.mode = mode;
    }
}
