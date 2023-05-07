package site.pointman.chatbot.service.serviceimpl;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.item.ItemOption;
import site.pointman.chatbot.dto.ItemDto;
import site.pointman.chatbot.dto.ItemOptionDto;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.service.KakaoJsonUiService;
import site.pointman.chatbot.service.OrderService;

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
}
