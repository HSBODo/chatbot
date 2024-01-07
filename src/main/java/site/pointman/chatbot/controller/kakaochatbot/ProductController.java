package site.pointman.chatbot.controller.kakaochatbot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.service.OrderService;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.product.dto.ProductCondition;
import site.pointman.chatbot.domain.product.service.ProductService;
import site.pointman.chatbot.exception.NotFoundMember;
import site.pointman.chatbot.exception.NotFoundOrder;
import site.pointman.chatbot.exception.NotFoundProduct;
import site.pointman.chatbot.handler.annotation.ValidateMember;
import site.pointman.chatbot.domain.product.constatnt.Category;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.product.dto.ProductDto;
import site.pointman.chatbot.view.kakaochatobotview.ProductChatBotView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/kakaochatbot/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductChatBotView productChatBotView;
    private final ProductService productService;
    private final OrderService orderService;
    private final ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();


    /**
     *  카카오 챗봇 특성상 HTTP Request의 HTTP Method가 POST로 고정되어 변경이 불가능하다.
     *  REST API를 구현하기 위해서 URL의 구성을 "자원(Resource)/행위(HTTP Method)"로 구성하였다.
     */

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "POST/verificationCustomer" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse verificationCustomer(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();

        return productChatBotView.addProductReconfirmPage();
    }

    @ResponseBody
    @PostMapping(value = "GET/category" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getCategory(@RequestBody ChatBotRequest chatBotRequest) {
        String requestBlockId = chatBotRequest.getRequestBlockId();

        return productChatBotView.productCategoryListPage(requestBlockId);
    }

    @ResponseBody
    @PostMapping(value = "GET/preview" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse preview(@RequestBody ChatBotRequest chatBotRequest) {
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productName = chatBotRequest.getProductName();
        String productDescription = chatBotRequest.getProductDescription();
        String productPrice = chatBotRequest.getProductPrice();
        String tradingLocation = chatBotRequest.getTradingLocation();
        String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();
        String category = chatBotRequest.getChoiceParam();

        return productChatBotView.addProductInfoPreviewPage(imageUrls,category,productName,productDescription,productPrice,tradingLocation,kakaoOpenChatUrl);
    }

    @ResponseBody
    @PostMapping(value = "GET/byCategory" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductsByCategory(@RequestBody ChatBotRequest chatBotRequest) {
        Category category = Category.getCategory(chatBotRequest.getChoiceParam());
        int pageNumber = chatBotRequest.getPageNumber();

        ProductCondition productCondition = ProductCondition.builder()
                .productCategory(category)
                .build();

        Page<Product> products = productService.getProducts(productCondition, pageNumber);

        return productChatBotView.productListByCategoryPage(products,category,pageNumber);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "POST" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse add(@RequestBody ChatBotRequest chatBotRequest){
        try {
            ProductDto productDto = chatBotRequest.createProductDto();

            productService.addProduct(productDto);

            return productChatBotView.addProductResultPage();
        }catch (NotFoundMember e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
     }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/myProductsByStatus" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMyProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        String searchProductStatus = chatBotRequest.getProductStatus();
        ProductStatus status = ProductStatus.getProductStatus(searchProductStatus);
        int pageNumber = chatBotRequest.getPageNumber();


        ProductCondition productCondition = ProductCondition.builder()
                .userKey(userKey)
                .firstProductStatus(status)
                .build();

        Page<Product> products = productService.getProducts(productCondition, pageNumber);

        return productChatBotView.myProductListByStatusPage(products,status,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/mainProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getMainProducts(@RequestBody ChatBotRequest chatBotRequest) {
        int pageNumber = chatBotRequest.getPageNumber();

        ProductCondition productCondition = ProductCondition.builder()
                .firstProductStatus(ProductStatus.판매중)
                .secondProductStatus(ProductStatus.예약)
                .build();

        Page<Product> products = productService.getProducts(productCondition, pageNumber);

        return productChatBotView.mainSaleProductListPage(products,pageNumber);
    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "GET/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            Long productId = Long.parseLong(chatBotRequest.getProductId());
            String userKey = chatBotRequest.getUserKey();

            Product product = productService.getProduct(productId);

            return productChatBotView.productDetailInfoPage(userKey, product);
        }catch (NotFoundProduct e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }

    }

    @ValidateMember
    @ResponseBody
    @PostMapping(value = "PATCH/status" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse updateProductStatus(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            Long productId = Long.parseLong(chatBotRequest.getProductId());
            String utterance = chatBotRequest.getUtterance();
            ProductStatus updateProductStatus = ProductStatus.getProductStatus(utterance);

            productService.updateProductStatus(productId, updateProductStatus);

            return productChatBotView.updateProductStatusResultPage(updateProductStatus);
        }catch (NotFoundProduct e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

    @ValidateMember
    @PostMapping(value = "DELETE" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse delete(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            Long productId = Long.parseLong(chatBotRequest.getProductId());

            productService.deleteProduct(productId);

            return productChatBotView.deleteProductResultPage();
        }catch (NotFoundProduct e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

    @ResponseBody
    @PostMapping(value = "GET/search" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse search(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();
        int pageNumber = chatBotRequest.getPageNumber();

        ProductCondition productCondition = ProductCondition.builder()
                .searchWord(searchWord)
                .build();

        Page<Product> products = productService.getProducts(productCondition, pageNumber);

        return productChatBotView.ProductListBySearchWordPage(products,searchWord,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/searchNext" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse searchNext(@RequestBody ChatBotRequest chatBotRequest) {
        String searchWord = chatBotRequest.getSearchWord();
        int pageNumber = chatBotRequest.getPageNumber();


        ProductCondition productCondition = ProductCondition.builder()
                .searchWord(searchWord)
                .build();

        Page<Product> products = productService.getProducts(productCondition, pageNumber);

        return productChatBotView.ProductListBySearchWordPage(products,searchWord,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getPurchaseProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        int pageNumber = chatBotRequest.getPageNumber();

        Page<Order> purchaseProductOrders = orderService.getPurchaseProducts(userKey, pageNumber);

        return productChatBotView.myPurchaseProductOrderListPage(purchaseProductOrders,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/purchaseProductProfile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getPurchaseProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        try {
            String userKey = chatBotRequest.getUserKey();
            Long orderId = Long.parseLong(chatBotRequest.getOrderId());

            Order purchaseProductOrder = orderService.getPurchaseProduct(userKey, orderId);

            return productChatBotView.myPurchaseProductOrderDetailInfoPage(purchaseProductOrder);
        }catch (NotFoundOrder e) {
            return chatBotExceptionResponse.createException(e.getMessage());
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProducts" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getContractProducts(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        int pageNumber = chatBotRequest.getPageNumber();

        return productChatBotView.mySalesContractProductListPage(userKey,pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/contractProduct/profile" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getContractProductProfile(@RequestBody ChatBotRequest chatBotRequest) {
        String userKey = chatBotRequest.getUserKey();
        Long orderId = Long.parseLong(chatBotRequest.getOrderId());

        Order salesContractProductOrder = orderService.getSalesContractProduct(userKey, orderId);

        return productChatBotView.mySalesContractProductOrderDetailInfoPage(salesContractProductOrder);
    }

    @ResponseBody
    @PostMapping(value = "GET/special" , headers = {"Accept=application/json; UTF-8"})
    public ChatBotResponse getSpecialProducts(@RequestBody ChatBotRequest chatBotRequest) {
        int currentPage = chatBotRequest.getPageNumber();
        int firstNumber = chatBotRequest.getFirstNumber();

        return productChatBotView.specialProductListPage(currentPage,firstNumber);
    }

}
