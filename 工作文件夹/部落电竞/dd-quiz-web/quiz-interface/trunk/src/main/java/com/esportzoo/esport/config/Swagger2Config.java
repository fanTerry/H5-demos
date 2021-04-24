package com.esportzoo.esport.config;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;

@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = "com.esportzoo.esport.config")
public class Swagger2Config {

	@Autowired
	private TypeResolver typeResolver;

	@Bean
	public Docket petApi() {

		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("quiz-interface")
				.apiInfo(apiInfo())
				.produces(Sets.newHashSet("application/json"))
				.forCodeGeneration(true)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.esportzoo.esport.controller"))
				.paths(PathSelectors.any())
				.build()
				.genericModelSubstitutes(ResponseEntity.class) // 4
				.alternateTypeRules( // 5
						newRule(typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
								typeResolver.resolve(WildcardType.class)));

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("esport API")
				.description("此文档收录quiz-interface所有与交互的接口")
				.contact(new Contact("jing.wu", "", "jing.wu@aicai.com"))
				.license("Apache License Version 2.0")
				.licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
				.version("2.0")
				.build();
	}

}
