package com.algaworks.algafood.api.v1.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.v1.assembler.FormaPagamentoAssembler;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoDisassembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping(path = "/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)	
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamentoService;
	
	@Autowired
	private FormaPagamentoAssembler formaPagamentoAssembler;
	
	@Autowired
	private FormaPagamentoDisassembler formaPagamentoDisassembler;
	
	
	@GetMapping
	public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar (ServletWebRequest request) {
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag  = "0";
		
		OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		if (request.checkNotModified(eTag)) {
			return null;
		}
		
		List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
		
		CollectionModel<FormaPagamentoModel> formasPagamentosModel = 
			    formaPagamentoAssembler.toCollectionModel(todasFormasPagamentos);
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
				.body(formasPagamentosModel);
	}

	@GetMapping("/{idFormaPagamento}")
	public ResponseEntity<FormaPagamentoModel> buscar (@PathVariable Long idFormaPagamento, ServletWebRequest request){
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataAtualizacaoById(idFormaPagamento);
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		if (request.checkNotModified(eTag)) {
			return null;
		}
			
		FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(idFormaPagamento);
		FormaPagamentoModel  formaPagamentoModel = formaPagamentoAssembler.toModel(formaPagamento);
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.body(formaPagamentoModel);
	}
	

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar (@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		
		FormaPagamento formaPagamento = formaPagamentoDisassembler.toDomainObject(formaPagamentoInput);
		
		cadastroFormaPagamentoService.salvar(formaPagamento);
		
		return formaPagamentoAssembler.toModel(formaPagamento);
	}
	
	@PutMapping("/{idFormaPagamento}")
	public FormaPagamentoModel atualizar (@PathVariable Long idFormaPagamento, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoService.buscarOuFalhar(idFormaPagamento);
		
		formaPagamentoDisassembler.copyDomainObject(formaPagamentoInput, formaPagamentoAtual);
		
		formaPagamentoAtual = cadastroFormaPagamentoService.salvar(formaPagamentoAtual);
		
		return formaPagamentoAssembler.toModel(formaPagamentoAtual);
	}
	
	
	@DeleteMapping("/{idFormaPagamento}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long idFormaPagamento) {
		cadastroFormaPagamentoService.remover(idFormaPagamento);
	}

}
