package site.lanmushan.slashdocstarter.plugin.validators;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import site.lanmushan.slashdocstarter.context.SlashDocContextThreadLocal;
import springfox.bean.validators.plugins.schema.PatternAnnotationPlugin;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

/**
 * @author dy
 */
@Configuration
public class SlashPatternAnnotationPlugin extends PatternAnnotationPlugin implements BaseValidatorsAnnotationPlugin {
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
    public void apply(ModelPropertyContext context) {
        SlashDocContextThreadLocal.get().ifPresent(ctx -> {
            Optional<Pattern> notBlank = extractAnnotation(context,Pattern.class);
            Optional<Valid> optionalValid = ctx.findAnnotation(Valid.class);
            Optional<Validated> optionalValidated = ctx.findAnnotation(Validated.class);
            Boolean exist = false;
            if (optionalValid.isPresent() && notBlank.isPresent()) {
                exist = true;
            } else if (optionalValidated.isPresent() && notBlank.isPresent() && intersectClass(optionalValidated.get().value(), notBlank.get().groups())) {
                exist = true;
            }
            if (exist) {
                super.apply(context);
            }

        });

    }
}
