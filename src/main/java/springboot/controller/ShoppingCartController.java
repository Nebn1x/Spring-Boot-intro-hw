package springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.UpdateCartItemRequestDto;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.model.User;
import springboot.service.ShoppingCartService;

@Tag(name = "ShoppingCart", description = "Operations related to shopping_carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Add item to cart",
            description = "Add new item to cart. Accessible to USER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Item added successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ShoppingCartDto addToCart(Authentication authentication,
                                     @RequestBody @Valid AddCartItemRequestDto requestDto) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return shoppingCartService.addToCart(userId, requestDto);
    }

    @Operation(
            summary = "Get a cart by ID",
            description = "Return a cart by user ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Cart retrieved successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Cart not found")
            }
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ShoppingCartDto getCart(Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return shoppingCartService.getCartById(userId);
    }

    @Operation(
            summary = "Update a item in cart",
            description = "Updates a item with the given ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Item updated successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Item not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    public ShoppingCartDto updateCartItem(Authentication authentication, @PathVariable Long id,
                                                @RequestBody UpdateCartItemRequestDto requestDto) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return shoppingCartService.updateCartItem(userId, id, requestDto);
    }

    @Operation(
            summary = "Delete a item from cart",
            description = "Deletes a item by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Item deleted successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Item not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{id}")
    public void deleteItem(Authentication authentication, @PathVariable Long id) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        shoppingCartService.deleteCartItem(userId, id);
    }
}
