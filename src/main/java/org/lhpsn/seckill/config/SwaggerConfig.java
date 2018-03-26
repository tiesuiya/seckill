package org.lhpsn.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类
 * 在与spring boot集成时，放在与Application.java同级的目录下。
 * 通过@Configuration注解，让Spring来加载该类配置。
 * 再通过@EnableSwagger2注解来启用Swagger2。
 * <p>
 * 参考:https://blog.csdn.net/SuperMenyII/article/details/78995822
 *
 * @author lh
 * @since 1.0.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 创建API应用
     * 采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return Swagger Docket
     */
    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.lhpsn.seckill.controller.rest"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 接口基本信息
     * 这些信息将会在对外swagger-ui.html中展示
     *
     * @return 基本信息对象
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("秒杀系统Restful Api")
                .description("前端秒杀ajax请求接口")
                .termsOfServiceUrl("https://github.com/tiesuiya/seckill")
                .version("1.0")
                .build();
    }

}
