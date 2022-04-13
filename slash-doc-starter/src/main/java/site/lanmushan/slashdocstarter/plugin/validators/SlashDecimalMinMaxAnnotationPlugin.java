package site.lanmushan.slashdocstarter.plugin.validators;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import site.lanmushan.slashdocstarter.context.SlashDocContextThreadLocal;
import springfox.bean.validators.plugins.schema.DecimalMinMaxAnnotationPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Optional;

/**
 * @author dy
 */
@Configuration
public class SlashDecimalMinMaxAnnotationPlugin extends DecimalMinMaxAnnotationPlugin implements BaseValidatorsAnnotationPlugin {
    @Override
    public void apply(ModelPropertyContext context) {
        SlashDocContextThreadLocal.get().ifPresent(ctx -> {
            Optional<DecimalMin> min = extractAnnotation(context, DecimalMin.class);
            Optional<DecimalMax> max = extractAnnotation(context, DecimalMax.class);
            Optional<Valid> optionalValid = ctx.findAnnotation(Valid.class);
            Optional<Validated> optionalValidated = ctx.findAnnotation(Validated.class);
            Boolean exist = false;
            if (optionalValid.isPresent() && min.isPresent()) {
                exist = true;
            } else if (optionalValidated.isPresent() && min.isPresent() && intersectClass(optionalValidated.get().value(), min.get().groups())) {
                exist = true;
            } else if (optionalValidated.isPresent() && max.isPresent() && intersectClass(optionalValidated.get().value(), max.get().groups())) {
                exist = true;
            }
            if (exist) {
                super.apply(context);
            }
        });
    }
}
