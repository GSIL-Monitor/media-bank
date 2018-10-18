package com.syswin.temail.media.bank.configuration;

import io.swagger.annotations.Api;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerFilterConfiguration {

    @Bean
    public Docket api() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("stoken").description("权限令牌").
                modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()  // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class)) // 对所有api进行监控
                .paths(PathSelectors.any()) // 对所有路径进行监控
                .build().apiInfo(apiInfo()).useDefaultResponseMessages(false).globalOperationParameters(aParameters);
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "MediaBank REST API",
                "",
                "version 1.0",
                "",
                new Contact("scloud", "", "scloud@syswin.com"),
                "",
                ""
        );
        return apiInfo;
    }

}
