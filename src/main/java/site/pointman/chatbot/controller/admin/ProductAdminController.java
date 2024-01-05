package site.pointman.chatbot.controller.admin;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.domain.product.constatnt.ProductStatus;
import site.pointman.chatbot.domain.response.constant.ResultCode;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.product.service.ProductService;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@RequestMapping(value = "admin/products")
@Controller
public class ProductAdminController {

    ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseBody
    @GetMapping(value = "all")
    public ResponseEntity getProducts(){
        HttpHeaders headers = getHeaders();

        List<Product> productsAll = productService.getProductsAll();
        if (productsAll.isEmpty()) return new ResponseEntity(new Response(ResultCode.FAIL,"상품이 존재하지 않습니다."),headers, HttpStatus.OK);

        return new ResponseEntity(productsAll,headers, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "")
    public ResponseEntity getProductsByUserKey(@RequestParam("userKey") String userKey){
        HttpHeaders headers = getHeaders();

        List<Product> memberProducts = productService.getMemberProducts(userKey);
        if (memberProducts.isEmpty()) return new ResponseEntity(new Response(ResultCode.FAIL,"상품이 존재하지 않습니다."),headers, HttpStatus.OK);

        return new ResponseEntity(memberProducts,headers, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "{productId}")
    public ResponseEntity getProduct(@PathVariable("productId") Long productId){
        HttpHeaders headers = getHeaders();

        Optional<Product> maybeProduct = productService.getProduct(productId);
        if (maybeProduct.isEmpty()) return new ResponseEntity(new Response(ResultCode.FAIL,"상품이 존재하지 않습니다."),headers, HttpStatus.OK);
        Product product = maybeProduct.get();

        return new ResponseEntity(product,headers, HttpStatus.OK);
    }

    @ResponseBody
    @PatchMapping(value = "{productId}")
    public ResponseEntity updateProductStatus(@PathVariable Long productId, @RequestParam String status){
        HttpHeaders headers = getHeaders();

        ProductStatus productStatus = ProductStatus.getProductStatus(status);
        Response response = productService.updateProductStatus(productId, productStatus);
        return new ResponseEntity(response,headers, HttpStatus.OK);
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
