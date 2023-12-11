package site.pointman.chatbot.service.domain.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.service.domain.BaseEntity;


import javax.persistence.*;


@Getter
@Entity
@Table(name = "tb_customer")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@IdClass(CustomerId.class)
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq ;

    @Id
    @Column(name = "user_key")
    private String userKey;

    private String name;
    private String phone;

    @Builder
    public Customer(String userKey, String name, String phone) {
        this.userKey = userKey;
        this.name = name;
        this.phone = phone;
    }
}
