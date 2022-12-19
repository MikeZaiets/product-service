package ua.com.zmike.product.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ua.com.zmike.product.service.config.DbQueryProperty;
import ua.com.zmike.product.service.converter.DtoConverter;
import ua.com.zmike.product.service.dto.ProductDto;
import ua.com.zmike.product.service.exception.TargetNotFoundException;
import ua.com.zmike.product.service.model.Product;
import ua.com.zmike.product.service.repository.ProductRepository;
import ua.com.zmike.product.service.service.ProductService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final int FIRST_PAGE_NUMBER = 0;

    private final DtoConverter<ProductDto, Product> converter;
    private final ProductRepository productRepository;
    private final DbQueryProperty dbQueryProperty;

    @Override
//    @Cacheable("filtered-products")
    public Set<ProductDto> getAllFilteredProducts(String nameFilter) {
        var filteredProductDtos = new HashSet<ProductDto>();
        var pageNumber = FIRST_PAGE_NUMBER;
        var queryPageSize = dbQueryProperty.getPageSize();

        Page<Product> currentPage;

        do {
            currentPage = productRepository.findAll(PageRequest.of(pageNumber, queryPageSize));
            log.info("Current page: {}/{}", pageNumber++, currentPage.getTotalPages());

            filteredProductDtos.addAll(filterPage(currentPage, nameFilter));
            log.info("Overall filtered count: {}", filteredProductDtos.size());

        } while (currentPage.getNumberOfElements() == queryPageSize);

        return filteredProductDtos;
    }

    private Set<ProductDto> filterPage(Page<Product> products, String nameFilter) {
        return products.stream()
                .filter(product -> applyFilter(nameFilter, product))
                .map(converter::convertToDto)
                .collect(Collectors.toSet());
    }

    private boolean applyFilter(String nameFilter, Product product) {
        return !product.getName().matches(nameFilter);
    }

    @Override
    public ProductDto addOne(ProductDto productDto) {
        var product = converter.convertFromDto(productDto);
        return converter.convertToDto(productRepository.save(product));
    }

    @Override
    public ProductDto getOneById(Long id) {
        return converter.convertToDto(getExistingProductById(id));
    }

    @Override
    public ProductDto updateOne(Long id, ProductDto productDto) {
        var productById = getExistingProductById(id);
        productById.setName(productDto.getName());
        productById.setDescription(productDto.getDescription());
        return converter.convertToDto(productRepository.save(productById));
    }

    @Override
    public void deleteOneById(Long id) {
        productRepository.deleteById(id);
    }

    private Product getExistingProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new TargetNotFoundException("Product", "id", id));
    }
}
