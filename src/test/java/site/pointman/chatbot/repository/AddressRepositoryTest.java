package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.address.Address;
import site.pointman.chatbot.domain.address.AddressRepositoryDataJpa;


import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
class AddressRepositoryTest {
//    @Autowired
//    AddressRepositoryDataJpa addressRepository;
    @Autowired
    AddressRepository addressRepository;

    @Test
    void findByAddress() {
        Long id = 2L;
        Optional<Address> byId = addressRepository.findByAddress(id);

    }

    @Test
    void findByMemberAddressAll() {
        String userkey = "QFERwysZbO77";
        List<Address> byMemberAddressAll = addressRepository.findByMemberAddressAll(userkey);
        if(byMemberAddressAll.isEmpty()) throw new NullPointerException("배송지가 없습니다");
        byMemberAddressAll.stream().forEach(address -> {
            log.info("address={}",address.getCreateDate());
        });
    }
}