package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Cozinha toDomainobject(CozinhaInput cozinhaInput) {
		return modelMapper.map(cozinhaInput, Cozinha.class);
	}
	
	public void copyDomainObject (CozinhaInput cozinhaInput, Cozinha cozinha) {
		modelMapper.map(cozinhaInput, cozinha);
	}
	
}
