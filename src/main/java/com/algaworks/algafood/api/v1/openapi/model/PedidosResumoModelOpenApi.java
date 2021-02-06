package com.algaworks.algafood.api.v1.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.v1.model.PedidoModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@ApiOperation("PedidosResumoModel")
@Getter
@Setter
public class PedidosResumoModelOpenApi {

	private PedidosEmbeddedModelOpenApi _embedded;
	private Links _links;
	private PageModelOpenApi page;
	
	
	@Data
	@ApiModel("PedidosEmbeddedModel")
	public class PedidosEmbeddedModelOpenApi{
		
		private List<PedidoModel> pedidos;
		
	}
}
