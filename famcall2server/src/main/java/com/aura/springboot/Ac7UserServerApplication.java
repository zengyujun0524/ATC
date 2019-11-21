package com.aura.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.aura.springboot.utils.ApnsPushUtils;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Ac7UserServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		// 离线推送初始化
		ApnsPushUtils.getSingleInstance().init();
		SpringApplication.run(Ac7UserServerApplication.class, args);
	}

	// 打包部署要继承 extends SpringBootServletInitializer
	@Override // 为了打包springboot项目
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}
}
