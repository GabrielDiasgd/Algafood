package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEncontradoException  extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public ProdutoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public ProdutoNaoEncontradoException (Long produtoId, Long restauranteId) {
		super(String.format("Não existe um cadastro de produto com códido %d para o restaurante de código %d", produtoId, restauranteId));
	}
}
