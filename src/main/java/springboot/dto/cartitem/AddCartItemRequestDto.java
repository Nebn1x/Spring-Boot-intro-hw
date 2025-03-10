package springboot.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartItemRequestDto {
    @NotNull
    private Long bookId;
    @NotNull
    private int quantity;
}
