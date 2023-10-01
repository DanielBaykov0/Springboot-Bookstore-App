package baykov.daniel.springbootlibraryapp.payload.mapper;

import baykov.daniel.springbootlibraryapp.entity.Cart;
import baykov.daniel.springbootlibraryapp.entity.CartItem;
import baykov.daniel.springbootlibraryapp.entity.Order;
import baykov.daniel.springbootlibraryapp.entity.Product;
import baykov.daniel.springbootlibraryapp.payload.dto.request.OrderRequestDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.response.OrderResponseDTO;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Map;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponseDTO entityToDTO(Order order);

    @Mapping(target = "status", constant = "COMPLETED")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    Order dtoToEntity(OrderRequestDTO orderRequestDTO, @Context Cart cart);

    default BigDecimal calculatePrice(Cart cart) {
        BigDecimal sum = BigDecimal.ZERO;

        for (Map.Entry<Product, CartItem> entry : cart.getProducts().entrySet()) {
            Product product = entry.getKey();
            CartItem cartItem = entry.getValue();
            BigDecimal productPrice = product.getPrice();
            int quantity = cartItem.getQuantity();
            sum = sum.add(productPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        return sum;
    }
}
