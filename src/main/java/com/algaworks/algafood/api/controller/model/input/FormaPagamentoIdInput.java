package com.algaworks.algafood.api.controller.model.input;

import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoIdInput {

	@ApiModelProperty(example = "Cartão de crédito", required = true)
	@NotNull
	private Long id;
	
}
