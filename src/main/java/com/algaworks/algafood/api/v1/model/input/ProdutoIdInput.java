package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoIdInput {
	
	@NotBlank
	private Long id;
	

}
