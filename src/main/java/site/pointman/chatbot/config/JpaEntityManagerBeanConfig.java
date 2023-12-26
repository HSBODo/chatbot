package site.pointman.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.repository.*;
import site.pointman.chatbot.repository.custom.MemberCustomRepository;
import site.pointman.chatbot.repository.custom.OrderCustomRepository;
import site.pointman.chatbot.repository.custom.PaymentCustomRepository;
import site.pointman.chatbot.repository.custom.ProductCustomRepository;
import site.pointman.chatbot.repository.custom.impl.MemberCustomRepositoryImpl;
import site.pointman.chatbot.repository.custom.impl.OrderCustomRepositoryImpl;
import site.pointman.chatbot.repository.custom.impl.PaymentCustomRepositoryImpl;
import site.pointman.chatbot.repository.custom.impl.ProductCustomRepositoryImpl;
import site.pointman.chatbot.repository.impl.*;

import javax.persistence.EntityManager;

@Configuration
public class JpaEntityManagerBeanConfig {
//    private DataSource dataSource;
    private EntityManager em;

    public JpaEntityManagerBeanConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberCustomRepository memberCustomRepository(){
        return new MemberCustomRepositoryImpl(em) ;
    }
    @Bean
    public ProductCustomRepository productCustomRepository(){
        return new ProductCustomRepositoryImpl(em);
    }
    @Bean
    public LogRepository logRepository(){
        return new LogRepositoryImpl(em);
    }
    @Bean
    public NoticeRepository noticeRepository(){
        return new NoticeRepositoryImpl(em);
    }
    @Bean
    public OrderCustomRepository orderCustomRepository(){
        return new OrderCustomRepositoryImpl(em);
    }
    @Bean
    public PaymentCustomRepository paymentCustomRepository(){
        return new PaymentCustomRepositoryImpl(em);
    }
}
