package site.lanmushan.slashdocstarter.plugin.validators;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import site.lanmushan.slashdocstarter.context.SlashDocContextThreadLocal;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static springfox.bean.validators.plugins.Validators.annotationFromBean;
import static springfox.bean.validators.plugins.Validators.annotationFromField;

/**
 * @author dy
 */
@Configuration
public class SlashNotBlankAnnotationPlugin implements ModelPropertyBuilderPlugin, BaseValidatorsAnnotationPlugin {

    /**
     * support all documentationTypes
     */
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    /**
     * read NotBlank annotation
     */
    @Override
    @SuppressWarnings("deprecation")
    public void apply(ModelPropertyContext context) {
        SlashDocContextThreadLocal.get().ifPresent(ctx -> {
            Optional<NotBlank> notBlank = extractAnnotation(context);
            Optional<Valid> optionalValid = ctx.findAnnotation(Valid.class);
            Optional<Validated> optionalValidated = ctx.findAnnotation(Validated.class);
            Boolean exist = false;
            if (optionalValid.isPresent() && notBlank.isPresent()) {
                exist = true;
            } else if (optionalValidated.isPresent() && notBlank.isPresent() && intersectClass(optionalValidated.get().value(), notBlank.get().groups())) {
                exist = true;
            }
            if (exist) {
                context.getBuilder().required(true);
                context.getSpecificationBuilder().required(true);
            }

        });

    }


    private Optional<NotBlank> extractAnnotation(ModelPropertyContext context) {
        return annotationFromBean(context, NotBlank.class)
                .map(Optional::of)
                .orElse(annotationFromField(context, NotBlank.class));
    }
}
