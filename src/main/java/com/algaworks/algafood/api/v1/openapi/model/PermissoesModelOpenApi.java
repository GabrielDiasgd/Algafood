package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.PermissaoModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("PermissoesModel")
@Data
public class PermissoesModelOpenApi {

	private PermissaoesEmbeddedModelOpenApi _embedded;
	private Links _links;
	
	
	@Data
	@ApiModel("PermissoesEmbeddedModel")
	public class PermissaoesEmbeddedModelOpenApi{
		
		private List<PermissaoModel> permissoes;
		
	}
}
