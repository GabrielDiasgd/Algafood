package com.algaworks.algafood.api.v2.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CidadeModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(path ="/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CidadeModelAssemblerV2 CidadeModelV2Assembler;
	
	@Autowired
	private CidadeInputDisassemblerV2 CidadeInputV2Disassembler;
	
	@GetMapping
	public CollectionModel<CidadeModelV2> listar() {
		return CidadeModelV2Assembler.toCollectionModel(cidadeRepository.findAll());
		
	}

	@GetMapping("/{cidadeId}")
	public CidadeModelV2 buscar(@PathVariable Long cidadeId) {
		CidadeModelV2 CidadeModelV2 = CidadeModelV2Assembler.toModel(cadastroCidade.buscarOuFalhar(cidadeId));
		return CidadeModelV2;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModelV2 adicionar(@RequestBody @Valid CidadeInputV2 cidadeInputV2) {
		try {
			Cidade cidade = CidadeInputV2Disassembler.toDomainObject(cidadeInputV2);
			
			cidade = cadastroCidade.salvar(cidade);
			CidadeModelV2 cidadeModelV2 = CidadeModelV2Assembler.toModel(cidade);
			
			ResourceUriHelper.addUriInResponseHeader(cidadeModelV2.getIdCidade());
			return cidadeModelV2;
	
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping("/{cidadeId}")
	public CidadeModelV2 atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInputV2 CidadeInputV2) {
		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
			CidadeInputV2Disassembler.copyDomainObject(CidadeInputV2, cidadeAtual);
			cidadeAtual = cadastroCidade.salvar(cidadeAtual);
			
			return CidadeModelV2Assembler.toModel(cidadeAtual);
			

		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidade.remover(cidadeId);
	}

}
