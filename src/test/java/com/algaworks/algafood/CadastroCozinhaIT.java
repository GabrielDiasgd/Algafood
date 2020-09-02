package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {

	//Teste de API
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner dataBaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	
	private static final int COZINHA_ID_INEXISTENTE = 100;

	private int quantidadeCozinhasCadastrados;
	
	Cozinha cozinhaAmericana = new Cozinha();
	
	private String jsonCorretoCozinhaChinesa;

	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = this.port;
		RestAssured.basePath = "/cozinhas";
		
		
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource("/json/Cozinha.json");
		
		dataBaseCleaner.clearTables();
		prepararDados();
		
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
			 given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(200);
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {
			 given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(quantidadeCozinhasCadastrados));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body(jsonCorretoCozinhaChinesa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
		 given()
		 	.pathParam("cozinhaId", cozinhaAmericana.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(200)
			.body("nome", equalTo(cozinhaAmericana.getNome()));
		
		 
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		 given()
		 	.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
		 
		 System.out.println("Cozinha Inexistente" + COZINHA_ID_INEXISTENTE);
		 
	}
	
	private void prepararDados() {
	
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhaRepository.save(cozinha1);
		
		this.cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(this.cozinhaAmericana);
		
		quantidadeCozinhasCadastrados = (int) cozinhaRepository.count();
		
	}
	
}

































	
	/* Teste de interagração
	 * @Autowired private CadastroCozinhaService cadastroCozinha;
	 * 
	 * @Test public void deveAtribuirId_QuandoCozinhaCadastradaComDadosCorretos() {
	 * //Cénario Cozinha cozinhaNova = new Cozinha();
	 * cozinhaNova.setNome("Chinesa");
	 * 
	 * //Ação cadastroCozinha.salvar(cozinhaNova);
	 * 
	 * //Validação assertThat(cozinhaNova).isNotNull();
	 * assertThat(cozinhaNova.getId()).isNotNull(); }
	 * 
	 * 
	 * @Test public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
	 * 
	 * Cozinha cozinhaNova = new Cozinha(); cozinhaNova.setNome(null);
	 * 
	 * assertThrows(ConstraintViolationException.class, () ->
	 * cadastroCozinha.salvar(cozinhaNova)); }
	 * 
	 * @Test public void deveFalhar_QuandoExcluirCozinhaEmUso() {
	 * assertThrows(EntidadeEmUsoException.class, () ->
	 * cadastroCozinha.remover(1L));
	 * 
	 * }
	 * 
	 * @Test public void deveFalhar_QuandoExcluirCozinhaInexistente() { Long
	 * cozinhaId = 999L; assertThrows(EntidadeNaoEncontradaException.class, () ->
	 * cadastroCozinha.remover(cozinhaId)); }
	 */
	

