package site.lanmushan.slashdocstarter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import site.lanmushan.slashdocstarter.configuration.properties.SlashDocProperties;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

/**
 * @author dy
 */

@ConditionalOnProperty(prefix = "slashdoc", value = "enabled", havingValue = "true", matchIfMissing = false)
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    SlashDocProperties slashDocProperties;

    @Bean
    @ConditionalOnMissingBean(name = "createRestApi")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .apiInfo(apiInfo())
                .groupName("默认分组")
                .select()
                .apis(createApiSelectorBuilder())
                .paths(PathSelectors.any())
                .build();
    }

    Predicate<RequestHandler> createApiSelectorBuilder() {
        if (StringUtils.isEmpty(slashDocProperties.getBasePackage())) {
            return RequestHandlerSelectors.withClassAnnotation(Controller.class)
                    .or(RequestHandlerSelectors.withClassAnnotation(RestController.class));
        }
        return RequestHandlerSelectors.basePackage(slashDocProperties.getBasePackage());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description(slashDocProperties.getDescription())
                .contact(new Contact(slashDocProperties.getContactName(), slashDocProperties.getContactUrl(), slashDocProperties.getContactEmail()))
                .version(slashDocProperties.getVersion())
                .title(slashDocProperties.getTitle())
                .build();
    }
}
