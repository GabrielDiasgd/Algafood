package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(path = "/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;

	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;

	@Autowired
	private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;
	
	@Autowired
	private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;

	// @Autowired private SmartValidator validator;

	
	@GetMapping
	public CollectionModel<RestauranteBasicoModel> listar() {
		return restauranteBasicoModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}

	@GetMapping("/nome")
	public List<RestauranteModel> buscarPorNome(String nome) {
		System.out.println(nome);
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findByNomeContaining(nome));
	}

	@GetMapping(params = "projecao=apenas-nome")
	public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
		return restauranteApenasNomeModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}

	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		return restauranteModelAssembler.toModel(restaurante);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);

			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));

		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}

	}

	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

		restauranteInputDisassembler.copyDomainObject(restauranteInput, restauranteAtual);

		/*
		 * BeanUtils.copyProperties(restaurante, restauranteAtual, "id",
		 * "formasPagamento", "endereco", "dataCadastro", "produtos");
		 */
		try {
			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}

	}

	@DeleteMapping("/{restauranteId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long restauranteId) {
		cadastroRestaurante.remover(restauranteId);
	}

	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.ativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}

	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.inativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}

	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
		cadastroRestaurante.abertura(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechamento(restauranteId);
		return ResponseEntity.noContent().build();
	}

}

/*
 * @GetMapping public MappingJacksonValue listar(@RequestParam(required = false)
 * String projecao) { List<Restaurante> restaurantes =
 * restauranteRepository.findAll(); List<RestauranteModel>todosRestaurantes =
 * restauranteModelAssembler.toCollectionModel(restaurantes);
 * 
 * MappingJacksonValue restauranteWrapper = new
 * MappingJacksonValue(todosRestaurantes);
 * 
 * restauranteWrapper.setSerializationView(RestauranteView.Resumo.class);
 * 
 * if ("apenas-nome".equals(projecao)) {
 * restauranteWrapper.setSerializationView(RestauranteView.ApenasNome.class); }
 * else if ("completo".equals(projecao)) {
 * restauranteWrapper.setSerializationView(null); }
 * 
 * return restauranteWrapper; }
 */

/*
 * @PatchMapping("/{restauranteId}") public RestauranteModel
 * atualizarParcial(@PathVariable Long restauranteId,
 * 
 * @RequestBody Map<String, Object> campos, HttpServletRequest request) {
 * Restaurante restauranteAtual =
 * cadastroRestaurante.buscarOuFalhar(restauranteId);
 * 
 * merge(campos, restauranteAtual, request);
 * 
 * validate(restauranteAtual, "restaurante");
 * 
 * return atualizar(restauranteId, restauranteAtual); }
 * 
 * private void validate(Restaurante restaurante, String objectName) {
 * BeanPropertyBindingResult bindingResult = new
 * BeanPropertyBindingResult(restaurante, objectName);
 * 
 * validator.validate(restaurante, bindingResult);
 * 
 * if (bindingResult.hasErrors()) { throw new ValidacaoException(bindingResult);
 * }
 * 
 * }
 * 
 * private void merge(Map<String, Object> campos, Restaurante
 * restauranteDestino, HttpServletRequest request) { ServletServerHttpRequest
 * serverHttpRequest = new ServletServerHttpRequest(request);
 * 
 * try { ObjectMapper objectMapper = new ObjectMapper();
 * objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
 * true);
 * objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
 * true);
 * 
 * Restaurante restauranteOrigem = objectMapper.convertValue(campos,
 * Restaurante.class);
 * 
 * campos.forEach((nomePropriedade, valorPropriedade) -> { Field field =
 * ReflectionUtils.findField(Restaurante.class, nomePropriedade);
 * field.setAccessible(true);
 * 
 * Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
 * 
 * ReflectionUtils.setField(field, restauranteDestino, novoValor); });
 * 
 * } catch (IllegalArgumentException e) { Throwable rootCause =
 * ExceptionUtils.getRootCause(e); throw new
 * HttpMessageNotReadableException(e.getMessage(), rootCause,
 * serverHttpRequest); } }
 */
