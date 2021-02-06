package com.algaworks.algafood.api.v1.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	public RestauranteModelAssembler() {
		super(RestauranteController.class, RestauranteModel.class);
	}

	public RestauranteModel toModel(Restaurante restaurante) {
		RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
		modelMapper.map(restaurante, restauranteModel);
		
		restauranteModel.add(algaLinks.linktoRestaurantes("restaurantes"));
	
    	restauranteModel.getCozinha().add(algaLinks.linkToCozinha(restaurante.getCozinha().getId(), "cozinha"));
    	
    	if (restaurante.getEndereco() != null 
				&& restaurante.getEndereco().getCidade() != null){
    		restauranteModel.getEndereco().getCidade().add(
    				algaLinks.linkToCidade(restauranteModel.getEndereco().getCidade().getId(), "cidade"));
				}
    	
    	restauranteModel.add(algaLinks.linkToProdutos(restaurante.getId(), "produtos"));
    	
		restauranteModel.add(algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(), "formas-pagamento"));
	
    	restauranteModel.add(algaLinks.linkToResponsaveisRestaurante(restaurante.getId(), "responsaveis"));
    	
    	if (restaurante.ativacaoPermitida()) {
    		restauranteModel.add(
    				algaLinks.linkToAtivar(restauranteModel.getId(), "ativar"));
    	}

    	if (restaurante.inativacaoPermitida()) {
    		restauranteModel.add(
    				algaLinks.linkToInativar(restauranteModel.getId(), "inativar"));
    	}

    	if (restaurante.aberturaPermitida()) {
    		restauranteModel.add(
    				algaLinks.linkToAbrir(restauranteModel.getId(), "abrir"));
    	}

    	if (restaurante.fechamentoPermitido()) {
    		restauranteModel.add(
    		algaLinks.linkToFechar(restauranteModel.getId(), "fechar"));
    	}
    		return restauranteModel;
	}
	
	public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
		 return restaurantes.stream()
				 .map(restaurante -> toModel(restaurante))
				 .collect(Collectors.toList());
	}
}
