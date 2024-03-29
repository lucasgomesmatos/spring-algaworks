package algafood.api.dtos.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParametrosCidadeDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private Long idEstado;
}
