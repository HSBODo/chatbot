package site.pointman.chatbot.controller.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.product.ProductStatus;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.service.ProductService;

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
    public Object getProducts(){
        List<Product> productsAll = productService.getProductsAll();
        if (productsAll.isEmpty()) return new Response(ResultCode.FAIL,"상품이 존재하지 않습니다.");

        return productsAll;
    }

    @ResponseBody
    @GetMapping(value = "")
    public Object getProductsByUserKey(@RequestParam("userKey") String userKey){
        List<Product> memberProducts = productService.getMemberProducts(userKey);
        if (memberProducts.isEmpty()) return new Response(ResultCode.FAIL,"상품이 존재하지 않습니다.");
        return memberProducts;
    }

    @ResponseBody
    @GetMapping(value = "{productId}")
    public Object getProduct(@PathVariable("productId") Long productId){
        Optional<Product> maybeProduct = productService.getProduct(productId);
        if (maybeProduct.isEmpty()) return new Response(ResultCode.FAIL,"상품이 존재하지 않습니다");

        return maybeProduct.get();
    }

    @ResponseBody
    @PatchMapping(value = "{productId}")
    public Response updateProductStatus(@PathVariable Long productId, @RequestParam String status){
        ProductStatus productStatus = ProductStatus.getProductStatus(status);
        return productService.updateProductStatus(productId,productStatus);
    }
}
