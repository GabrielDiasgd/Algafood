package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointController {
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping
	public RootEntryPointModel root() {
		var rootEntryPoinModel = new RootEntryPointModel();
		
		rootEntryPoinModel.add(algaLinks.linkToCozinhas("cozinhas"));
		rootEntryPoinModel.add(algaLinks.linkToPedidos("pedidos"));
		rootEntryPoinModel.add(algaLinks.linktoRestaurantes("restaurantes"));
		rootEntryPoinModel.add(algaLinks.linkToGrupos("grupos"));
		rootEntryPoinModel.add(algaLinks.linkToUsuarios("usuarios"));
		rootEntryPoinModel.add(algaLinks.linkToPermissoes("permissoes"));
		rootEntryPoinModel.add(algaLinks.linkToFormasPagamento("forma-pagamento"));
		rootEntryPoinModel.add(algaLinks.linkToEstados("estados"));
		rootEntryPoinModel.add(algaLinks.linkToCidades("cidades"));
		rootEntryPoinModel.add(algaLinks.linkToEstatisticas("estatisticas"));
		
		
		
		return rootEntryPoinModel;
	}
	
	
	private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel>{
	}

}
