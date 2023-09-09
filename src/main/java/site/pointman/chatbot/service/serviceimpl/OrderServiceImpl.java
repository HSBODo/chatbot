package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.item.ItemOption;
import site.pointman.chatbot.dto.item.ItemDto;
import site.pointman.chatbot.dto.item.ItemOptionDto;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    ItemRepository itemRepository;

    public OrderServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public int calculateTotalPrice(Long itemId, Long optionId, int quantity) {
        Optional<Item> maybeItem = itemRepository.findByItem(itemId);
        if(maybeItem.isEmpty()) throw new NullPointerException("결제금액을 계산할 상품이 없습니다.");
        ItemDto itemDto = maybeItem.get().toItemDto();

        Optional<ItemOption> maybeItemOption = itemRepository.findByItemOption(optionId);
        if (maybeItemOption.isEmpty()) throw new NullPointerException("결제금액을 계산할 상품의 옵션이 없습니다.");
        ItemOptionDto itemOptionDto = maybeItemOption.get().toItemOptionDto();

        int itemPrice = itemDto.getDiscountedPrice();
        int optionPrice = itemOptionDto.getOptionPrice();

        int totalPrice =(itemPrice+optionPrice)*quantity;


        return totalPrice;
    }


    @Override
    public List<Item> findBySalesRank() {
        return null;
    }
}