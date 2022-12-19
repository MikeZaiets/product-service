package ua.com.zmike.product.service.converter;

import org.springframework.stereotype.Component;
import ua.com.zmike.product.service.dto.ProductDto;
import ua.com.zmike.product.service.model.Product;

@Component
public class ProductConverter implements DtoConverter<ProductDto, Product> {

    @Override
    public ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }

    @Override
    public Product convertFromDto(ProductDto productDto) {
        var product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        return product;
    }
}
