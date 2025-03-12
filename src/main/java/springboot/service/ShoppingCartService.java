package springboot.service;

import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.UpdateCartItemRequestDto;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getCartById(Long userId);

    ShoppingCartDto addToCart(Long userId, AddCartItemRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long cartItemId);

    void createShoppingCart(User user);
}
