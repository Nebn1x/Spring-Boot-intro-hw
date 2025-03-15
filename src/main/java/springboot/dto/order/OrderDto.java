package springboot.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import springboot.model.OrderItem;

public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItem> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
}
