package algafood.infrastructure.repository.spec;

import algafood.domain.models.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestauranteSpec {

    public static Specification<Restaurante> comFreteGratis() {
        return ((root, query, builder) -> builder.equal(root.get("taxaFrete"), BigDecimal.ZERO));
    }
    public static Specification<Restaurante> comNomeSemelhante(String nome) {
        return ((root, query, builder) -> builder.like(root.get("nome"), "%" + nome + "%"));
    }
}
