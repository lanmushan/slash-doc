package site.lanmushan.slashdocstarter.plugin;

import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.util.Optional;

import static springfox.bean.validators.plugins.Validators.extractAnnotation;

//@Component
@Slf4j
//@Order(Ordered.LOWEST_PRECEDENCE)
public class ApiModelHiddenProperty implements ModelPropertyBuilderPlugin {
    @Override
    public void apply(ModelPropertyContext context) {
        Optional<ApiModelProperty> apiModelProperty = extractAnnotation(context, ApiModelProperty.class);
        log.info(context.getSpecificationBuilder().build().toString());
        if (apiModelProperty.isPresent() && apiModelProperty.get().hidden()) {
            context.getSpecificationBuilder().isHidden(true);
        }
        context.getDocumentationType().getName();
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
