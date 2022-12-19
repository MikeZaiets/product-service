package ua.com.zmike.product.service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.com.zmike.product.service.config.DbQueryProperty;
import ua.com.zmike.product.service.converter.DtoConverter;
import ua.com.zmike.product.service.dto.ProductDto;
import ua.com.zmike.product.service.model.Product;
import ua.com.zmike.product.service.repository.ProductRepository;
import ua.com.zmike.product.service.util.TestEntityFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static ua.com.zmike.product.service.util.TestConstants.FIRST_PAGE_NUMBER;
import static ua.com.zmike.product.service.util.TestConstants.GLOBAL_PATTERN;
import static ua.com.zmike.product.service.util.TestConstants.QUERY_PAGE_SIZE;
import static ua.com.zmike.product.service.util.TestConstants.TEST_PATTERN;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private DtoConverter<ProductDto, Product> converter;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DbQueryProperty dbQueryProperty;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getAllFilteredProducts_shouldReturnList_whenSuitableItemsExistInDb() {
        var pageable = PageRequest.of(FIRST_PAGE_NUMBER, QUERY_PAGE_SIZE);

        var product = TestEntityFactory.getProduct(1L);
        var productDto = TestEntityFactory.getProductDto(1L);

        var productsByDb = List.of(product);
        var productPage = new PageImpl<>(productsByDb);

        var expectedResult = Set.of(productDto);

        when(dbQueryProperty
                .getPageSize())
                .thenReturn(QUERY_PAGE_SIZE);
        when(productRepository
                .findAll(pageable))
                .thenReturn(productPage);
        when(converter
                .convertToDto(product))
                .thenReturn(productDto);

        var result = productService.getAllFilteredProducts(TEST_PATTERN);

        assertThat(result).isEqualTo(expectedResult);
        verify(dbQueryProperty, only()).getPageSize();
        verify(productRepository, only()).findAll(pageable);
        verify(converter, only()).convertToDto(product);
    }

    @Test
    void getAllFilteredProducts_shouldReturnEmptyList_whenDbReturnNoItems() {
        var pageable = PageRequest.of(FIRST_PAGE_NUMBER, QUERY_PAGE_SIZE);

        var productPage = new PageImpl<Product>(Collections.emptyList());

        when(dbQueryProperty
                .getPageSize())
                .thenReturn(QUERY_PAGE_SIZE);
        when(productRepository
                .findAll(pageable))
                .thenReturn(productPage);

        var result = productService.getAllFilteredProducts(GLOBAL_PATTERN);

        assertThat(result).isEqualTo(Collections.emptySet());
        verify(dbQueryProperty, only()).getPageSize();
        verify(productRepository, only()).findAll(pageable);
        verifyNoInteractions(converter);
    }
}
