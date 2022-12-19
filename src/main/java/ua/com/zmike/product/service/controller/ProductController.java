package ua.com.zmike.product.service.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.com.zmike.product.service.dto.ProductDto;
import ua.com.zmike.product.service.service.ProductService;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Slf4j
@RestController
@Validated
@RequestMapping("/shop/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Set<ProductDto> getProducts(@RequestParam("nameFilter") @NotBlank String nameFilter) {
        log.info("Get all Products with filter value: {}", nameFilter);
        return productService.getAllFilteredProducts(nameFilter);
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable("id") Long id) {
        log.info("Get Product by id: {}", id);
        return productService.getOneById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody ProductDto product) {
        log.info("Add Product with params: {}", product);
        return productService.addOne(product);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable("id") Long id,
                                    @RequestBody ProductDto product) {
        log.info("Update Product by id: {} for params: {}", id, product);
        return productService.updateOne(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        log.info("Delete Product by id: {}", id);
        productService.deleteOneById(id);
    }
}
