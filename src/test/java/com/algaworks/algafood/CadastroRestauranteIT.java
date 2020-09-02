package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;

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
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroRestauranteIT {

	//Teste de API
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner dataBaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";
	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	private static final int RESTAURANTE_ID_INEXISTENTE = 100;
	

	private int quantidadeRestauranteCadastrados;
	
	private Restaurante burgerTopRestaurante;

	
	private String jsonCorretoRestaurante;
	private String jsonRestauranteSemCozinha;
	private String jsonRestauranteSemTaxaFrete;
	private String jsonRestauranteComCozinhaInexistente;

	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = this.port;
		RestAssured.basePath = "/restaurantes";
		
		
		jsonCorretoRestaurante = ResourceUtils.getContentFromResource("/json/correto/restaurante-new-york-barbecue.json");
		jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-sem-cozinha.json");
		jsonRestauranteSemTaxaFrete = ResourceUtils.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-sem-frete.json");
		jsonRestauranteComCozinhaInexistente = ResourceUtils.getContentFromResource("/json/incorreto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");
		
		dataBaseCleaner.clearTables();
		prepararDados();
		
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarRestaurantes() {
			 given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(200);
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeRestaurantes_QuandoConsultarRestaurantes() {
			 given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(quantidadeRestauranteCadastrados));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
		given()
			.body(jsonCorretoRestaurante)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	
	}
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
		 given()
		 	.pathParam("restauranteId", burgerTopRestaurante.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{restauranteId}")
		.then()
			.statusCode(200)
			.body("nome", equalTo(burgerTopRestaurante.getNome()));
		
		 
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarRestaranteInexistente() {
		 given()
		 	.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{restauranteId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
		 
		 System.out.println("Restaurante Inexistente" + RESTAURANTE_ID_INEXISTENTE);
		 
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
		given()
			.body(jsonRestauranteSemCozinha)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
		
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
		given()
			.body(jsonRestauranteSemTaxaFrete)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}
	
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
		given()
			.body(jsonRestauranteComCozinhaInexistente)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}
	
	
	private void prepararDados() {
		
		 	Cozinha cozinhaBrasileira = new Cozinha();
	        cozinhaBrasileira.setNome("Brasileira");
	        cozinhaRepository.save(cozinhaBrasileira);

	        Cozinha cozinhaAmericana = new Cozinha();
	        cozinhaAmericana.setNome("Americana");
	        cozinhaRepository.save(cozinhaAmericana);
	        
	        burgerTopRestaurante = new Restaurante();
	        burgerTopRestaurante.setNome("Burger Top");
	        burgerTopRestaurante.setTaxaFrete(new BigDecimal(10));
	        burgerTopRestaurante.setCozinha(cozinhaAmericana);
	        restauranteRepository.save(burgerTopRestaurante);
	        
	        Restaurante comidaMineiraRestaurante = new Restaurante();
	        comidaMineiraRestaurante.setNome("Comida Mineira");
	        comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
	        comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
	        restauranteRepository.save(comidaMineiraRestaurante);
	
		quantidadeRestauranteCadastrados = (int) restauranteRepository.count();
		
	}
	
}
