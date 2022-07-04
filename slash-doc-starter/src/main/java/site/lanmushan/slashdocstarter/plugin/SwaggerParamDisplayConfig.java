package site.lanmushan.slashdocstarter.plugin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

/**
 * @author Administrator
 */
@Slf4j
@Configuration
@Primary
public class SwaggerParamDisplayConfig implements ParameterBuilderPlugin {

    @Override
    public void apply(ParameterContext parameterContext) {
        log.info(parameterContext.toString());
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
