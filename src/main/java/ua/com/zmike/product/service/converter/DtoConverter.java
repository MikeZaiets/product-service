package ua.com.zmike.product.service.converter;

public interface DtoConverter<T, S> {

    T convertToDto(S s);

    S convertFromDto(T t);
}
