package com.algaworks.algafood.api.v1.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/testes")
public class TesteController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@GetMapping("/cozinhas/por-nome")
	public List<Cozinha> consutaPorNome(String nome) {
		return cozinhaRepository.findByNome(nome);
	}

	@GetMapping("/cozinhas/por-nome-containing")
	public List<Cozinha> consutaPorNomeContaining(String nome) {
		return cozinhaRepository.findByNomeContaining(nome);
	}

	@GetMapping("/restaurantes/por-taxaFrete")
	public List<Restaurante> consultaPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {
		return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}

	@GetMapping("/restaurantes/por-nome-cozinha")
	public List<Restaurante> consultaPorNomeAndCozinha(String nome, Long cozinhaId) {
		return restauranteRepository.consultarPorNome(nome, cozinhaId);
	}

	@GetMapping("/cozinhas/por-nome-restauranteTop")
	public List<Restaurante> nomeTop2(String nome) {
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}

	@GetMapping("/restaurantes/por-nome-primeiro")
	public Optional<Restaurante> primeiroNomeContaining(String nome) {
		return restauranteRepository.findFirstByNomeContaining(nome);
	}

	@GetMapping("/cozinhas/por-nome-exist")
	public boolean consultaNomeExist(String nome) {
		return cozinhaRepository.existsByNome(nome);
	}

	@GetMapping("/restaurantes/count-cozinha")
	public int consultaCountCozinhda(Long cozinhaId) {
		return restauranteRepository.countByCozinhaId(cozinhaId);
	}
	
	
	
	@GetMapping("/restaurantes/por-Frete")
	public List<Restaurante> consultaPorFrete(String nome,BigDecimal taxaInicial, BigDecimal taxaFinal) {
		return restauranteRepository.find(nome, taxaInicial, taxaFinal);
	}

	
	@GetMapping("/restaurantes/por-freteGratis")
	public List<Restaurante> porFreteGratis(String nome) {
		return restauranteRepository.findComFreteGratis(nome);
	}
	
	
	@GetMapping("/restaurantes/primeiroRestaurante")
	public Optional<Restaurante> primeiraCozinha() {
		return restauranteRepository.buscarPrimeiro();
	}
}
