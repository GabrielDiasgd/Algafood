package com.algaworks.algafood.api.controller.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoModel {

	private Long id;
	private String produtoNome;
	private Integer quantidade;
	private BigDecimal precounitario;
	private BigDecimal precoTotal;
	private String observacao;
}
