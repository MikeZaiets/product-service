package ua.com.zmike.product.service.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ua.com.zmike.product.service.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

}
