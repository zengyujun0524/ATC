package com.aura.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Carry
 * @ClassName cn.saytime.Swgger2
 * @Description
 * @date 2017-07-10 22:12:31
 */

//用@Configuration注解该类，等价于XML中配置beans；用@Bean标注方法等价于XML中配置bean。
@Configuration
public class Swagger2 {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.aura.springboot.web")) // controller/web包的路径
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("AC7用户模块及设备模块接口提供") // api文档标题
				.description("用户模块及设备模块") // api文档副标题
				.termsOfServiceUrl("http://192.168.1.238:8080/aura/helloWorld.html").version("1.0").build();
	}

}
