package com.ddd.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfiguration {

    @Value("${swagger.enable:false}")
    private Boolean enable;

    @Bean
    public Docket defaultDocket() {
        return DocketBuilder.builder()
            .groupName("全部API")
            .title("默认API接口文档")
            .description("默认分组的API接口文档")
            .basePackage("com.ddd.interfaces.controller")
            .enable(enable)
            .build()
            .newDocket();
    }

    @Bean
    public Docket userDocket() {
        return DocketBuilder.builder()
            .groupName("系统内部—用户管理API")
            .title("系统内部—用户管理API")
            .description("用户相关接口文档")
            .basePackage("com.ddd.interfaces.controller.user")
            .enable(enable)
            .build()
            .newDocket();
    }

    @Bean
    public Docket openDocket() {
        return DocketBuilder.builder()
            .groupName("开放平台—用户管理API")
            .title("开放平台—用户管理API")
            .description("对外开放的接口文档")
            .basePackage("com.ddd.interfaces.controller.open")
            .enable(enable)
            .build()
            .newDocket();
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocketBuilder {
        private String groupName;
        private String title;
        private String description;
        private String basePackage;
        private Boolean enable;

        public Docket newDocket() {
            return new Docket(DocumentationType.SWAGGER_2)
                .groupName(this.groupName)
                .apiInfo(apiInfo(this.title, this.description))
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
        }

        private ApiInfo apiInfo(String title, String description) {
            return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(new Contact("", "", ""))
                .build();
        }

    }
}
