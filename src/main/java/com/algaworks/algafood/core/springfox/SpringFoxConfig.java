package com.algaworks.algafood.core.springfox;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.CidadeModel;
import com.algaworks.algafood.api.v1.model.CozinhaModel;
import com.algaworks.algafood.api.v1.model.EstadoModel;
import com.algaworks.algafood.api.v1.model.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.GrupoModel;
import com.algaworks.algafood.api.v1.model.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.PermissaoModel;
import com.algaworks.algafood.api.v1.model.ProdutoModel;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.model.CidadesModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.CozinhasModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.EstadosModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.FormasPagamentoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.GruposModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.LinksModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PageableModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PedidosResumoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.PermissoesModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.ProdutosModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.RestaurantesBasicoModelOpenApi;
import com.algaworks.algafood.api.v1.openapi.model.UsuariosModelOpenApi;
import com.fasterxml.classmate.TypeResolver;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {

	@Bean
	public Docket apiDocket() {
		var typeResolver = new TypeResolver();
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
				.build()
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
				.globalResponseMessage(RequestMethod.PUT, globalPostPutResponseMessages())
				.globalResponseMessage(RequestMethod.POST, globalPostPutResponseMessages())
				.globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
				.additionalModels(typeResolver.resolve(Problem.class))
				.ignoredParameterTypes(ServletWebRequest.class)
				.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
				.directModelSubstitute(Links.class, LinksModelOpenApi.class)
				.alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(PagedModel.class, CozinhaModel.class),
						CozinhasModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(PagedModel.class, PedidoResumoModel.class),
					    PedidosResumoModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
	                    typeResolver.resolve(CollectionModel.class, CidadeModel.class),
	                    CidadesModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
	                    typeResolver.resolve(CollectionModel.class, EstadoModel.class),
	                    EstadosModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
	                    typeResolver.resolve(CollectionModel.class, FormaPagamentoModel.class),
	                    FormasPagamentoModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, GrupoModel.class),
					    GruposModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					        typeResolver.resolve(CollectionModel.class, PermissaoModel.class),
					        PermissoesModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, ProdutoModel.class),
					    ProdutosModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					    typeResolver.resolve(CollectionModel.class, RestauranteBasicoModel.class),
					    RestaurantesBasicoModelOpenApi.class))
				.alternateTypeRules(AlternateTypeRules.newRule(
					        typeResolver.resolve(CollectionModel.class, UsuarioModel.class),
					        UsuariosModelOpenApi.class))
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as Cidades"),
						new Tag("Grupos", "Gerencia os Grupos"),
						new Tag("Cozinhas", "Gerencia as cozinhas"),
						new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
						new Tag("Pedidos", "Gerencia os pedidos"),
						new Tag("Restaurantes", "Gerencia os restaurantes"),
						new Tag("Estados", "Gerencia os estados"),
						new Tag("Produtos", "Gerencia os produtos de restaurantes"),
						new Tag("Usuários", "Gerencia os usuários"),
						new Tag("Estatísticas", "Estatísticas da AlgaFood"),
						new Tag("Permissões", "Gerencia as permissões"));
	}
	
	private List<ResponseMessage> globalGetResponseMessages(){
		return Arrays.asList(
					new ResponseMessageBuilder()
					.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.message("Erro interno do Servidor")
					.build(),
					
					new ResponseMessageBuilder()
					.code(HttpStatus.NOT_ACCEPTABLE.value())
					.message("Recurso não possui representação que poderia ser aceita pelo consumidor")
					.build()
				);
	}
		
		private List<ResponseMessage> globalPostPutResponseMessages(){
			return Arrays.asList(
					   new ResponseMessageBuilder()
					   .code(HttpStatus.BAD_REQUEST.value())
					   .message("Requisição Inválida (Erro Cliente)")
					   .build(),
					   
						new ResponseMessageBuilder()
						.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.message("Erro interno do Servidor")
						.build(),
						
						new ResponseMessageBuilder()
						.code(HttpStatus.NOT_ACCEPTABLE.value())
						.message("Recurso não possui representação que poderia ser aceita pelo consumidor")
						.build(),
					
						new ResponseMessageBuilder()
						.code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
						.message("Requisição não suportada porque o corpo está em um formato não suportado")
						.build()
					);
		}
			private List<ResponseMessage> globalDeleteResponseMessages() {
			    return Arrays.asList(
			            new ResponseMessageBuilder()
			                .code(HttpStatus.BAD_REQUEST.value())
			                .message("Requisição inválida (erro do cliente)")
			                .build(),
			            new ResponseMessageBuilder()
			                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			                .message("Erro interno no servidor")
			                .build()
			        );
			}

	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("Algafood API")
			.description("Api para restaurantes")
			.version("1")
			.contact(new Contact("AlgaFood", "www.algafood.com.br"	, "gabrielllloliveira1998@gmail.com"))
			.build();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		
		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
