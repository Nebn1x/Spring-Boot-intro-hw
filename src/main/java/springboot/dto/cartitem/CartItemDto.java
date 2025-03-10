package springboot.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDto {
    @NotNull
    private Long id;
    @NotNull
    private Long bookId;
    @NotBlank
    private String bookTitle;
    private int quantity;
}
