package algafood.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany
    @JoinTable(
            name = "grupo_permissao",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();


    public void associar(Permissao permissao) {
        getPermissoes().add(permissao);
    }

    public void desassociar(Permissao permissao) {
        getPermissoes().remove(permissao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Grupo grupo = (Grupo) o;
        return getId() != null && Objects.equals(getId(), grupo.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
