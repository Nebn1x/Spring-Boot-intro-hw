package springboot.dto.order;

import lombok.Data;
import springboot.model.Order;

@Data
public class OrderStatusRequestDto {
    private Order.Status status;
}
