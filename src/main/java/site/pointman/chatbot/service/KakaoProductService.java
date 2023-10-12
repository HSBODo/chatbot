package site.pointman.chatbot.service;

import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.dto.response.ResponseDto;

public interface KakaoProductService {
    ResponseDto createProductsInfo(ProductListDto productListDto);
}
