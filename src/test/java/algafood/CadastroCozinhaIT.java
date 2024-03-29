package algafood;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import algafood.api.dtos.input.CozinhaInputDTO;
import algafood.api.dtos.output.CozinhaOutputDTO;
import algafood.domain.exception.EntidadeNaoEncontradaException;
import algafood.domain.models.Cozinha;
import algafood.domain.repositories.CozinhaRepository;
import algafood.domain.service.CozinhaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CadastroCozinhaIT {

	@Autowired
	private CozinhaService cozinhaService;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Test
	public void deveTestarCadastroCozinhaComSucesso() {
		// Cenário
		CozinhaInputDTO novaCozinha = new CozinhaInputDTO("Brasileira");

		// Ação
		CozinhaOutputDTO novaCozinhaCadastrada = cozinhaService.adicionar(novaCozinha);

		// Validação
		assertThat(novaCozinhaCadastrada).isNotNull();
		assertThat(novaCozinhaCadastrada.getId()).isNotNull();

		// Verificar se a cozinha foi persistida no banco de dados
		Cozinha cozinhaPersistida = cozinhaRepository.findById(novaCozinhaCadastrada.getId()).orElse(null);
		assertThat(cozinhaPersistida).isNotNull();
		assertThat(cozinhaPersistida.getNome()).isEqualTo(novaCozinha.getNome());
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void deveFalharAoCadastrarCozinhaQuandoSemNome() {
		// Cenário
		CozinhaInputDTO novaCozinha = new CozinhaInputDTO(null);

		// Ação
		CozinhaOutputDTO novaCozinhaCadastrada = cozinhaService.adicionar(novaCozinha);

	}

	@Test(expected = DataIntegrityViolationException.class)
	public void devefalharQuandoExcluirCozinhaEmUso() {

		cozinhaService.remover(1L);
	}

	@Test(expected = EntidadeNaoEncontradaException.class)
	public void devefalharQuandoExcluirCozinhaInexistente() {

		cozinhaService.remover(10L);
	}
}
