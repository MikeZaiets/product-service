package ua.com.zmike.product.service.service;

import ua.com.zmike.product.service.dto.ProductDto;

import java.util.Set;

public interface ProductService {

    Set<ProductDto> getAllFilteredProducts(String nameFilter);

    ProductDto getOneById(Long id);

    ProductDto addOne(ProductDto product);

    ProductDto updateOne(Long id, ProductDto product);

    void deleteOneById(Long id);
}
