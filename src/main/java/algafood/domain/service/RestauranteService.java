package algafood.domain.service;

import algafood.api.dtos.input.ParametrosRestauranteDTO;
import algafood.api.dtos.output.RestauranteOutputDTO;
import algafood.common.mapper.Mapper;
import algafood.domain.common.MensagensDeException;
import algafood.domain.exception.EntidadeEmUsoException;
import algafood.domain.exception.NegocioException;
import algafood.domain.exception.RestauranteNaoEncontradoException;
import algafood.domain.models.Cozinha;
import algafood.domain.models.Endereco;
import algafood.domain.models.Restaurante;
import algafood.domain.repositories.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaService cozinhaService;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    Mapper mapper;

    private Restaurante buscarPorId(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
    }

    public List<RestauranteOutputDTO> listar() {
        return mapper.mapCollection(restauranteRepository.findAll(), RestauranteOutputDTO.class);
    }

    @Transactional
    public RestauranteOutputDTO adicionar(ParametrosRestauranteDTO parametrosRestauranteDTO) {
        var cozinhaOutput = cozinhaService.buscar(parametrosRestauranteDTO.getCozinha().getId());
        var cozinha = mapper.generalMapper(cozinhaOutput, Cozinha.class);

        var cidade = cidadeService.buscar(parametrosRestauranteDTO.getEndereco().getCidade().getId());

        var endereco = Endereco.builder()
                .cep(parametrosRestauranteDTO.getEndereco().getCep())
                .numero(parametrosRestauranteDTO.getEndereco().getNumero())
                .logradouro(parametrosRestauranteDTO.getEndereco().getLogradouro())
                .complemento(parametrosRestauranteDTO.getEndereco().getComplemento())
                .bairro(parametrosRestauranteDTO.getEndereco().getBairro())
                .cidade(cidade)
                .build();

        var restaurante = Restaurante.builder()
                .nome(parametrosRestauranteDTO.getNome())
                .taxaFrete(parametrosRestauranteDTO.getPrecoFrete())
                .cozinha(cozinha)
                .endereco(endereco)
                .ativo(true)
                .aberto(true)
                .build();

        return mapper.generalMapper(restauranteRepository.save(restaurante), RestauranteOutputDTO.class);
    }

    public RestauranteOutputDTO buscar(Long id) {
        return mapper.generalMapper(buscarPorId(id), RestauranteOutputDTO.class);
    }

    public Restaurante buscarRestaurante(Long id) {
        return buscarPorId(id);
    }

    @Transactional
    public void remover(Long id) {

        try {
            buscarPorId(id);
            restauranteRepository.deleteById(id);
            restauranteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MensagensDeException.MENSAGEM_RESTAURANTE_EM_USO.getMensagem(), id));
        }

    }

    @Transactional
    public RestauranteOutputDTO atualizar(ParametrosRestauranteDTO parametrosRestauranteDTO, Restaurante restaurante) {

        var cozinhaOutput = cozinhaService.buscar(parametrosRestauranteDTO.getCozinha().getId());
        var cozinha = mapper.generalMapper(cozinhaOutput, Cozinha.class);

        var cidade = cidadeService.buscar(parametrosRestauranteDTO.getEndereco().getCidade().getId());

        var endereco = Endereco.builder()
                .cep(parametrosRestauranteDTO.getEndereco().getCep())
                .numero(parametrosRestauranteDTO.getEndereco().getNumero())
                .logradouro(parametrosRestauranteDTO.getEndereco().getLogradouro())
                .complemento(parametrosRestauranteDTO.getEndereco().getComplemento())
                .bairro(parametrosRestauranteDTO.getEndereco().getBairro())
                .cidade(cidade)
                .build();


        restaurante.setNome(parametrosRestauranteDTO.getNome());
        restaurante.setTaxaFrete(parametrosRestauranteDTO.getPrecoFrete());
        restaurante.setCozinha(cozinha);
        restaurante.setEndereco(endereco);


        return mapper.generalMapper(restauranteRepository.save(restaurante), RestauranteOutputDTO.class);
    }

    @Transactional
    public void ativar(Long id) {
        var restaurante = buscarRestaurante(id);
        restaurante.ativar();
    }

    @Transactional
    public void ativar(List<Long> restauranteIds) {
        try {
            restauranteIds.forEach(this::ativar);
        } catch (RestauranteNaoEncontradoException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }


    @Transactional
    public void inativar(Long id) {
        var restaurante = buscarRestaurante(id);
        restaurante.inativar();
    }

    @Transactional
    public void inativar(List<Long> restauranteIds) {
        try {
            restauranteIds.forEach(this::inativar);
        } catch (RestauranteNaoEncontradoException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }

    @Transactional
    public void abertura(Long id) {
        var restaurante = buscarRestaurante(id);
        restaurante.abertura();
    }

    @Transactional
    public void fechamento(Long id) {
        var restaurante = buscarRestaurante(id);
        restaurante.fechamento();
    }


    @Transactional
    public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
        var restaurante = buscarPorId(restauranteId);
        var formaPagamento = formaPagamentoService.buscarFormaPagamento(formaPagamentoId);
        restaurante.associarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
        var restaurante = buscarPorId(restauranteId);
        var formaPagamento = formaPagamentoService.buscarFormaPagamento(formaPagamentoId);
        restaurante.desassociarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void associarUsuario(Long restauranteId, Long usuarioId) {
        var restaurante = buscarPorId(restauranteId);
        var usuario = usuarioService.buscarUsuario(usuarioId);
        restaurante.associarUsuario(usuario);
    }

    @Transactional
    public void desassociarUsuario(Long restauranteId, Long usuarioId) {
        var restaurante = buscarPorId(restauranteId);
        var usuario = usuarioService.buscarUsuario(usuarioId);
        restaurante.desassociarUsuario(usuario);
    }
}
