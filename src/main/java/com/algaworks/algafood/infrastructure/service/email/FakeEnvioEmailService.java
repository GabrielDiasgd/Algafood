package com.algaworks.algafood.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.core.email.EmailProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeEnvioEmailService extends SmtpEnvioEmailService {

	@Autowired
	private EmailProperties emailProperties;

	
	@Override
	public void enviar(Mensagem mensagem) {
	
			String corpo = processarTemplate(mensagem);

			log.info(emailProperties.getRemetente());
			log.info(mensagem.getAssunto());
			log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
	}
}


