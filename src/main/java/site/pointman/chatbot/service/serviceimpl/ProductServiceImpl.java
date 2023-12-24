package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.dto.product.SpecialProduct;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.CrawlingService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.S3FileService;
import site.pointman.chatbot.service.chatbot.ProductChatBotResponseService;
import site.pointman.chatbot.utill.CustomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    CrawlingService crawlingService;
    S3FileService s3FileService;

    ProductRepository productRepository;
    MemberRepository memberRepository;
    OrderRepository orderRepository;
    ProductChatBotResponseService productChatBotResponseService;

    ChatBotExceptionResponse chatBotExceptionResponse;

    @Value("${host.url}")
    private String HOST_URL;

    public ProductServiceImpl(CrawlingService crawlingService, S3FileService s3FileService, ProductRepository productRepository, MemberRepository memberRepository, OrderRepository orderRepository, ProductChatBotResponseService productChatBotResponseService) {
        this.crawlingService = crawlingService;
        this.s3FileService = s3FileService;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.productChatBotResponseService = productChatBotResponseService;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    @Override
    public ChatBotResponse addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory) {
        try {
            Category category = Category.getCategory(productCategory);
            Member member = memberRepository.findByUserKey(userKey).get();
            String productName = productDto.getName();

            productDto.setStatus(ProductStatus.판매중);
            productDto.setCategory(category);
            productDto.setMember(member);
            productDto.setId(productId);

            ProductImageDto productImageDto = s3FileService.uploadImages(imageUrls, userKey,productName,"image");

            productRepository.insertProduct(productDto,productImageDto);

            return productChatBotResponseService.addProductSuccessChatBotResponse();
        }catch (Exception e){
            return  chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getProductCategory(String requestBlockId) {
        if(requestBlockId.equals(BlockId.PRODUCT_ADD_INFO.getBlockId()))  return productChatBotResponseService.getCategoryChatBotResponse(BlockId.PRODUCT_PROFILE_PREVIEW);

        return productChatBotResponseService.getCategoryChatBotResponse(BlockId.FIND_PRODUCTS_BY_CATEGORY);
    }

    @Override
    public ChatBotResponse getProductsByCategory(Category category) {
        try {
            List<Product> products = productRepository.findByCategory(category,ProductStatus.판매중);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다.");

            return productChatBotResponseService.createProductListChatBotResponse(products,ButtonName.이전으로,BlockId.PRODUCT_GET_CATEGORIES);
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category) {
        try {
            String formatPrice = CustomStringUtils.formatPrice(Integer.parseInt(productPrice));

            return productChatBotResponseService.getProductInfoPreviewSuccessChatBotResponse(imageUrls, category, productName,productDescription,formatPrice,tradingLocation,kakaoOpenChatUrl);
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getMyProducts(String userKey, String productStatus) {
        try {
            ProductStatus status = ProductStatus.getProductStatus(productStatus);

            List<Product> products = productRepository.findByUserKey(userKey,status);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다");

            return productChatBotResponseService.createMyProductListChatBotResponse(products,ButtonName.처음으로,BlockId.MAIN);
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getProductProfile(String productId, String userKey) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(Long.parseLong(productId));

            if(mayBeProduct.isEmpty()) return chatBotExceptionResponse.createException("상품이 존재하지 않습니다.");
            
            Product product = mayBeProduct.get();

            return productChatBotResponseService.getProductProfileSuccessChatBotResponse(userKey, product);
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse updateProductStatus(String productId, String utterance) {
        try {
            long parseProductId = Long.parseLong(productId);
            ProductStatus productStatus = ProductStatus.getProductStatus(utterance);
            if (productStatus.equals(ProductStatus.예약취소)) productStatus = ProductStatus.판매중;

            Optional<Product> mayBeProduct = productRepository.findByProductId(parseProductId);
            if(mayBeProduct.isEmpty()) return chatBotExceptionResponse.createException("상품이 존재하지 않습니다.");

            productRepository.updateStatus(parseProductId,productStatus);

            return productChatBotResponseService.updateStatusSuccessChatBotResponse(productStatus);
        }catch (Exception e){
            return chatBotExceptionResponse.createException("상태변경을 실패하였습니다.");
        }
    }

    @Override
    public ChatBotResponse deleteProduct(String productId, String utterance) {
        try {
            long parseProductId = Long.parseLong(productId);

            Optional<Product> mayBeProduct = productRepository.findByProductId(parseProductId);
            if(mayBeProduct.isEmpty()) return chatBotExceptionResponse.createException("상품이 존재하지 않습니다.");

            if(ProductStatus.삭제.name().equals(utterance)){
                productRepository.deleteProduct(parseProductId);
                return productChatBotResponseService.deleteProductSuccessChatBotResponse();
            }

            return chatBotExceptionResponse.createException("상품 삭제를 실패하였습니다.");
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse verificationCustomerSuccessResponse() {
       return productChatBotResponseService.verificationCustomerSuccessChatBotResponse();
    }

    @Override
    public ChatBotResponse getProductsBySearchWord(String searchWord) {
        try {
            List<Product> products = productRepository.findBySearchWord(searchWord, ProductStatus.판매중);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없어 상품을 찾을수 없습니다.");

            return productChatBotResponseService.createProductListChatBotResponse(products,ButtonName.처음으로,BlockId.MAIN);
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getContractProducts(String userKey) {
        List<Product> contractProducts = productRepository.findByUserKey(userKey, ProductStatus.판매대기);
        if(contractProducts.isEmpty()) return chatBotExceptionResponse.createException("결제가 체결된 상품이 없습니다.");

        return productChatBotResponseService.getContractProductsSuccessChatBotResponse(contractProducts);
    }

    @Override
    public ChatBotResponse getContractProductProfile(String userKey, String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId),OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return chatBotExceptionResponse.createException("체결된 주문이 없습니다.");

        Order order = mayBeOrder.get();
        if(!userKey.equals(order.getProduct().getMember().getUserKey())) return chatBotExceptionResponse.createException();

        return productChatBotResponseService.getContractProductProfileSuccessChatBotResponse(order);
    }

    @Override
    public Object getProducts() {
        List<Product> products = productRepository.findByAll();
        if (products.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"상품이 존재하지 않습니다.");
        return products;
    }

    @Override
    public Object getProducts(String userKey) {
        List<Product> products = productRepository.findByUserKey(userKey);
        if (products.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"상품이 존재하지 않습니다.");
        return products;
    }

    @Override
    public Object getProduct(Long productId) {
        Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
        if (mayBeProduct.isEmpty()) return new HttpResponse(ApiResultCode.FAIL,"상품이 존재하지 않습니다.");
        Product product = mayBeProduct.get();
        return product;
    }

    @Override
    public Object updateProductStatus(Long productId, ProductStatus status) {
        productRepository.updateStatus(productId,status);
        return new HttpResponse(ApiResultCode.OK,"상품 "+productId+"을 "+status+"상태로 변경하였습니다.");
    }

    @Override
    public ChatBotResponse getSpecialProducts(int startPage) {
        try {
            List<Element> filterElements = new ArrayList<>();
            List<SpecialProduct> specialProducts = new ArrayList<>();
            String url = "https://quasarzone.com/bbs/qb_saleinfo";
            String cssQuery = "#frmSearch > div > div.list-board-wrap > div.market-type-list.market-info-type-list.relative > table > tbody > tr";

            Elements elements = crawlingService.getJsoupElements(url, cssQuery);

            int fistPage = startPage;
            int lastPage = fistPage+5;

            for (Element element : elements){
                String status = element.select("p.tit > span").text();
                String title = element.select("p.tit > a").text();
                if (status.equals("종료")) continue;
                if (title.contains("블라인드 처리된 글입니다.")) continue;
                filterElements.add(element);
            }

            if (lastPage > filterElements.size()) lastPage = filterElements.size();

            for (int i = fistPage ; i < lastPage ; i++){
                String imageUrl = "";
                String brandImageUrl = "";
                String detailUrl = "";

                String status = filterElements.get(i).select("p.tit > span").text();
                if(!filterElements.get(i).select("td:nth-child(2) > div > div.thumb-wrap > a > img").isEmpty()) {
                    imageUrl = filterElements.get(i).select("td:nth-child(2) > div > div.thumb-wrap > a > img").get(0).attr("src");
                }
                String title = filterElements.get(i).select("td:nth-child(2) > div > div.market-info-list-cont > p > a > span").text();
                String price = filterElements.get(i).select("td:nth-child(2) > div > div.market-info-list-cont > div > p > span:nth-child(2) > span").text();
                String category = filterElements.get(i).select("td:nth-child(2) > div > div.market-info-list-cont > div.market-info-sub > p > span.category").text();
                if(!filterElements.get(i).select("td:nth-child(2) > div > div.market-info-list-cont > div.market-info-sub > p > span.brand > img").isEmpty()){
                    brandImageUrl = filterElements.get(i).select("td:nth-child(2) > div > div.market-info-list-cont > div.market-info-sub > p > span.brand > img").get(0).attr("src");
                }
                if (!filterElements.get(i).select("td:nth-child(2) > div > div.thumb-wrap > a").isEmpty()) {
                    detailUrl = "https://quasarzone.com/"+filterElements.get(i).select("td:nth-child(2) > div > div.thumb-wrap > a").get(0).attr("href");
                }

                Document productInfoDetail = Jsoup.connect(detailUrl).get();

                String purchaseUrl = productInfoDetail.select("table.market-info-view-table > tbody > tr:nth-child(1) > td > a").text();
                String brandName = productInfoDetail.select("table.market-info-view-table > tbody > tr:nth-child(2) > td").text();

                SpecialProduct specialProduct = SpecialProduct.builder()
                        .productThumbnailImageUrl(imageUrl)
                        .brandName(brandName)
                        .brandImageUrl(brandImageUrl)
                        .title(title)
                        .price(price)
                        .category(category)
                        .detailInfoUrl(detailUrl)
                        .purchaseUrl(purchaseUrl)
                        .status(status)
                        .build();
                specialProducts.add(specialProduct);
            }

            if (lastPage >= filterElements.size()) lastPage = 0;
            return productChatBotResponseService.getSpecialProductsSuccessChatBotResponse(specialProducts, lastPage);
        }catch (Exception e) {
            log.info("e={}",e.getMessage());
            return chatBotExceptionResponse.createException();
        }
    }
}
