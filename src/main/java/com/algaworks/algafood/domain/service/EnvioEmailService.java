package com.algaworks.algafood.domain.service;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

public interface EnvioEmailService {

	void enviar(Mensagem mensagem);

	
	@Builder
	@Getter
	@Setter
	class Mensagem {
		
		@Singular
		private Set<String> destinatarios;
		private String assunto;
		private String corpo;
		
		@Singular("variavel")
		private Map<String, Object> variaveis;
	}
}
