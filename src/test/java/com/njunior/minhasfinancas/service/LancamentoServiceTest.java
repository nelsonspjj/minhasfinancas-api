package com.njunior.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.njunior.minhasfinancas.exception.RegraNegocioExcepction;
import com.njunior.minhasfinancas.model.entity.Lancamento;
import com.njunior.minhasfinancas.model.entity.Usuario;
import com.njunior.minhasfinancas.model.enums.StatusLancamento;
import com.njunior.minhasfinancas.model.repository.LancamentoRepository;
import com.njunior.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.njunior.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverUmErroDeValidacao() {
		//cenario
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioExcepction.class).when(service).validar(lancamentoASalvar);
		
		//exucucao e verificacao
		Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioExcepction.class);
		
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		//cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execucao
		service.atualizar(lancamentoSalvo);
		
		//verificacao
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarumLancamentoQueAindaNaoFoiSalvo() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		//exucucao e verificacao
		Assertions.catchThrowableOfType( () -> service.atualizar(lancamento), NullPointerException.class);
		
		Mockito.verify(repository, Mockito.never()).save(lancamento);
		
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//exucucao
		service.deletar(lancamento);
		
		//verificacao
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void deveLancarUmErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				
		//exucucao
		Assertions.catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class);
				
		//verificacao
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
		
	}

	@Test
	public void deveFiltrarLancamentos() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//exucucao
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificacoes
		Assertions
		.assertThat(resultado)
		.isNotEmpty()
		.hasSize(1)
		.contains(lancamento);
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		//exucucao
		service.atualizarStatus(lancamento, novoStatus);
		
		//verificacoes
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		//cenario
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//exucucao
		Optional<Lancamento> reusultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(reusultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		//cenario
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//exucucao
		Optional<Lancamento> reusultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(reusultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setDescricao("");
		
		Throwable erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe uma Descrição válida.");
		
		lancamento.setDescricao("salario");
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(0);
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(13);
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Mês válido.");
		
		lancamento.setMes(1);
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(202);
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Ano válido.");
		
		lancamento.setAno(2021);
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Usuário.");
		
		lancamento.setUsuario(new Usuario());
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Usuário.");
		
		lancamento.getUsuario().setId(1l);
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Valor válido.");
		
		lancamento.setValor(BigDecimal.ZERO);
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Valor válido.");
		
		lancamento.setValor(BigDecimal.valueOf(1));
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioExcepction.class).hasMessage("Informe um Tipo de Lançamento.");
	}
}
