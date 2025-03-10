package springboot.dto.shoppingcart;

import java.util.Set;
import springboot.dto.cartitem.CartItemDto;

public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> items;
}
