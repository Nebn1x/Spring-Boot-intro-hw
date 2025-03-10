package springboot.service;

import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.CartItemDto;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getCartById(Long userId);

    CartItemDto addToCart(AddCartItemRequestDto requestDto);

    CartItemDto updateCartItem(Long cartItemId, AddCartItemRequestDto requestDto);

    void deleteCartItem(Long cartItemId);

    void createShoppingCart(User user);
}
