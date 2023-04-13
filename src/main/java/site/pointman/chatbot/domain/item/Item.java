package site.pointman.chatbot.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.member.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tb_item")
public class Item extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx ;
    @Id
    @Column(name = "item_code")
    private int itemCode;
    private String description; //최대 40자
    private String thumbnailImgUrl;
    private String thumbnailLink;
    private String profileImgUrl;
    private String ProfileNickname;
    private int price;
    private int discount;
    private int discountedPrice;
    private int discountRate;
    private String currency = "won";
    private String is_use;
    private String is_display;
}
