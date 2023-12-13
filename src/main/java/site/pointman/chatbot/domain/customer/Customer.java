package site.pointman.chatbot.domain.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.pointman.chatbot.domain.BaseEntity;
import site.pointman.chatbot.domain.product.Product;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "tb_customer")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@IdClass(CustomerId.class)
public class Customer extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq ;

    @Id
    @Column(name = "user_key")
    private String userKey;

    private String name;
    private String phone;

    @OneToMany(mappedBy = "customer")
    private List<Product> products = new ArrayList<>();


    @Builder
    public Customer(String userKey, String name, String phone) {
        this.userKey = userKey;
        this.name = name;
        this.phone = phone;
    }

    public void changePhone(String updatePhone){
        phone = updatePhone;
    }
}
