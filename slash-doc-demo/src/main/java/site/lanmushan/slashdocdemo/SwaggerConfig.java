package site.lanmushan.slashdocdemo;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

/**
 * @author dy
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig {
    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public Docket createRestApi() {
       return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .apiInfo(apiInfo())
                .groupName("默认分组")
                .select()
                .apis(RequestHandlerSelectors.basePackage("site.lanmushan.slashdocdemo"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("SlashDocDemo")
                .contact(new Contact("哈哈哈", "https://github.com/lanmushan/slash-doc", "lanmushan@163.com"))
                .version("v1.1.0")
                .title("API测试文档")
                .build();
    }
}
