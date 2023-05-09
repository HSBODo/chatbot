package site.pointman.chatbot.domain.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.pointman.chatbot.domain.address.Address;

import java.util.Optional;

public interface AddressRepositoryDataJpa extends JpaRepository<Address,Long>{

}
