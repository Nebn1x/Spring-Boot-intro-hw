package springboot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import springboot.config.MapperConfig;
import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.CartItemDto;
import springboot.model.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    AddCartItemRequestDto toDto(CartItem cartItem);

    CartItem toModel(CartItemDto requestDto);

    void updateCartItemFromDto(CartItemDto requestDto,
                           @MappingTarget CartItem item);
}
