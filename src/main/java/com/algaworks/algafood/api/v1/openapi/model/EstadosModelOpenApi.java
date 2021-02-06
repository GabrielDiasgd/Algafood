package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.CidadeModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("EstadosModel")
@Data
public class EstadosModelOpenApi {

	private EstadoEmbeddedModelOpenApi _embedded;
	private Links _links;
	
	
	@Data
	@ApiModel("EstadosEmbeddedModel")
	public class EstadoEmbeddedModelOpenApi{
		
		private List<CidadeModel> cidades;
		
	}
}
