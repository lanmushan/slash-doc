package site.lanmushan.slashdocstarter.plugin.validators;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import site.lanmushan.slashdocstarter.context.SlashDocContextThreadLocal;
import springfox.bean.validators.plugins.schema.MinMaxAnnotationPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

/**
 * @author dy
 */
@Configuration
public class SlashMinMaxAnnotationPlugin extends MinMaxAnnotationPlugin implements BaseValidatorsAnnotationPlugin {
    @Override
    public void apply(ModelPropertyContext context) {
        SlashDocContextThreadLocal.get().ifPresent(ctx -> {
            Optional<Min> min = extractAnnotation(context, Min.class);
            Optional<Max> max = extractAnnotation(context, Max.class);
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
