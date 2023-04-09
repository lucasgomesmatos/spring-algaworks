package algafood.domain.service;

import algafood.domain.exception.ApiRequestException;
import algafood.domain.models.Cozinha;
import algafood.domain.repositories.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CozinhaService {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    private Cozinha buscarPorId(Long id) {
        return cozinhaRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Cozinha nao encontado para o id: "+ id));
    }
    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    @Transactional
    public Cozinha adicionar(Cozinha cidade) {
        return cozinhaRepository.save(cidade);
    }

    public Cozinha buscar(Long id) {
        return buscarPorId(id);
    }

    @Transactional
    public void remover(Long id) {
        buscarPorId(id);
        cozinhaRepository.deleteById(id);
    }
}
