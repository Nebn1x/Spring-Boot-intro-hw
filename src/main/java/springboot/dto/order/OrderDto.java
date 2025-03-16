package springboot.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import springboot.dto.orderitem.OrderItemDto;
import springboot.model.Order;

public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
