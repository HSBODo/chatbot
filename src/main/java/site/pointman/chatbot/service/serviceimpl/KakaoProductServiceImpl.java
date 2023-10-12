package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.property.common.Buttons;
import site.pointman.chatbot.dto.response.property.common.Thumbnail;
import site.pointman.chatbot.dto.response.property.components.Carousel;
import site.pointman.chatbot.dto.response.property.components.CommerceCard;
import site.pointman.chatbot.service.KakaoProductService;

@Slf4j
@Service
public class KakaoProductServiceImpl implements KakaoProductService {

    @Override
    public ResponseDto createProductsInfo(ProductListDto productListDto) {
        ResponseDto responseDto = new ResponseDto();
        Carousel<CommerceCard> carousel = new Carousel<>();

        for(int i = 0 ; i < productListDto.getProductListDto().size() ; i++){

            ProductDto product = productListDto.getProduct(i);

            CommerceCard commerceCard = new CommerceCard();
            Thumbnail thumbnail = new Thumbnail(product.getImageUrl());
            Buttons buttons = new Buttons();
            buttons.addWebLinkButton("구매하러가기","https://smartstore.naver.com/heyfruit");

            int discountRate = (int)((((float) product.getSalePrice()-(float)product.getDiscountedPrice()) / (float)product.getSalePrice())*100);

            commerceCard.setThumbnails(thumbnail);
            commerceCard.setTitle(product.getName());
            commerceCard.setPrice(product.getSalePrice());
            commerceCard.setDiscountRate(discountRate);
            commerceCard.setDiscountedPrice(product.getDiscountedPrice());
            commerceCard.setButtons(buttons);

            carousel.addComponent(commerceCard);
        }

        responseDto.addCarousel(carousel);

        return responseDto;
    }
}
