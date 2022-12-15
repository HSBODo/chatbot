package site.pointman.chatbot.domain;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

@Getter
public class WeatherElementVo {

    private String sky;
    private String pop;
    private String uuu;
    private String pty;
    private String reh;
    private String vec;
    private String tmp;
    private String vvv;
    private String wsd;
    private String pcp;

    public void setSky(String sky) { this.sky = sky; }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public void setUuu(String uuu) {
        this.uuu = uuu;
    }

    public void setPty(String pty) { this.pty = pty; }

    public void setReh(String reh) {
        this.reh = reh;
    }

    public void setVec(String vec) {
        this.vec = vec;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public void setVvv(String vvv) {
        this.vvv = vvv;
    }

    public void setWsd(String wsd) {
        this.wsd = wsd;
    }

    public void setPcp(String pcp) {
        this.pcp = pcp;
    }
}
