package com.algaworks.algafood.domain.exception;

public class PermissaoNaoEncontraException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public PermissaoNaoEncontraException(String mensagem) {
		super(mensagem);
	}
	
	public PermissaoNaoEncontraException(Long permissaoId) {
		super(String.format("Não exite cadastro de permissão com códido %d", permissaoId));
	}

}
