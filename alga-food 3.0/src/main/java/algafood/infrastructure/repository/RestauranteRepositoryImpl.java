package algafood.infrastructure.repository;

import algafood.domain.models.Restaurante;
import algafood.domain.repositories.RestauranteRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Restaurante> consultar(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {


        var jpql = new StringBuilder();
        jpql.append("from Restaurante where 0 = 0");

        var parametros = new HashMap<String, Object>();

        if (StringUtils.hasLength(nome)) {
            jpql.append(" and nome like :nome");
            parametros.put("nome", "%" + nome + "%");
        }

        System.out.println(taxaFreteInicial);
        if (taxaFreteInicial != null) {
            jpql.append(" and taxaFrete >= :taxaInicial");
            parametros.put("taxaInicial", taxaFreteInicial);
        }

        if (taxaFreteInicial != null) {
            jpql.append(" and taxaFrete <= :taxaFinal");
            parametros.put("taxaFinal", taxaFreteFinal);
        }

        TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
        parametros.forEach(query::setParameter);
        return query.getResultList();
    }
}
