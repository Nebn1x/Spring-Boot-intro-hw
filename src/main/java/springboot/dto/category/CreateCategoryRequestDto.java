package springboot.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;
}
