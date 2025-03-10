package springboot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import springboot.config.MapperConfig;
import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.CartItemDto;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.model.CartItem;
import springboot.model.ShoppingCart;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    CartItemDto toDto(CartItem cartItem);

    ShoppingCart toModel(ShoppingCartDto requestDto);

    CartItem toModel(AddCartItemRequestDto requestDto);

    void updateCartItemFromDto(AddCartItemRequestDto requestDto,
                               @MappingTarget CartItem item);
}
