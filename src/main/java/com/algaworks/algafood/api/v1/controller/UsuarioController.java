package com.algaworks.algafood.api.v1.controller;

import java.util.List;

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

import com.algaworks.algafood.api.v1.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.model.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(path = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;

	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;

	@Autowired
	private UsuarioInputDisassembler usuarioInputDisassembler;

	@Override
	@GetMapping
	public CollectionModel<UsuarioModel> listar() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		return usuarioModelAssembler.toCollectionModel(usuarios);
	}
	

	@Override
	@GetMapping("/{usuarioId}")
	public UsuarioModel buscar(@PathVariable Long usuarioId) {
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		return usuarioModelAssembler.toModel(usuario);
	}

	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {

		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput);
		usuario = cadastroUsuarioService.salvar(usuario);
		return usuarioModelAssembler.toModel(usuario);
	}


	@Override
	@PutMapping("/{usuarioId}")
	public UsuarioModel atualizar(@PathVariable Long usuarioId,
			@RequestBody @Valid UsuarioInput usuarioInput) {
		Usuario usuarioAtual = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		usuarioInputDisassembler.copyDomainObject(usuarioInput, usuarioAtual);
		usuarioAtual = cadastroUsuarioService.salvar(usuarioAtual);
		
		return usuarioModelAssembler.toModel(usuarioAtual);
	}

	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover (@PathVariable Long usuarioId) {
		cadastroUsuarioService.remover(usuarioId);
	}

	@Override
	@PutMapping("/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha (@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senha) {
		cadastroUsuarioService.atualizarSenha(usuarioId, senha.getNovaSenha(), senha.getSenhaAtual());
	}

	
}
