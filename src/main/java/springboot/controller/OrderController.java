package springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.dto.order.CreateOrderRequestDto;
import springboot.dto.order.OrderDto;
import springboot.dto.order.OrderStatusRequestDto;
import springboot.dto.orderitem.OrderItemDto;
import springboot.model.User;
import springboot.service.OrderService;

@Tag(name = "Orders", description = "Operations related to Orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order. Accessible to USER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Order created successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody @Valid CreateOrderRequestDto requestDto) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return orderService.createOrder(userId, requestDto);
    }

    @Operation(
            summary = "Get all orders",
            description = "Return list of all orders. Accessible to USER and ADMIN roles.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Orders retrieved successfully"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Page<OrderDto> getAllOrders(Pageable pageable, Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return orderService.getAllOrders(pageable, userId);
    }

    @Operation(
            summary = "Update a status for order",
            description = "Updates a status with the given order ID."
                    + " Only available to ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Status updated successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Order not found"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden")
            }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody OrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @Operation(
            summary = "Get items by order Id",
            description = "Return a items by order ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Items retrieved successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Order not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/items")
    public List<OrderItemDto> getItemsByOrderId(@PathVariable Long id) {
        return orderService.getItemsByOrderId(id);
    }

    @Operation(
            summary = "Get item by order Id and item Id",
            description = "Return a item by order ID and item ID.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Item retrieved successfully"),
                    @ApiResponse(responseCode = "404",
                            description = "Item not found"),
                    @ApiResponse(responseCode = "404",
                            description = "Order not found")
            }
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{Id}/items/{itemId}")
    public OrderItemDto getItemByIdFromOrderId(@PathVariable Long orderId,
                                               @PathVariable Long itemId) {
        return orderService.getItemByIdFromOrderId(orderId, itemId);
    }
}
