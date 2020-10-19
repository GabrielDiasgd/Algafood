package com.algaworks.algafood.domain.exception;

public class FotoProdutoNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public FotoProdutoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public FotoProdutoNaoEncontradaException(Long restauranteId, Long produtoId) {
		super(String.format("Não existe cadastro de foto do produto de código  %d para o restaurante de código %d" , restauranteId, produtoId));
	}

	
}
