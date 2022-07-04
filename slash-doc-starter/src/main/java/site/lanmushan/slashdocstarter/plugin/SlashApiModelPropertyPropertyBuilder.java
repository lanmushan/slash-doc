package site.lanmushan.slashdocstarter.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import site.lanmushan.slashdocstarter.context.SlashDocContext;
import site.lanmushan.slashdocstarter.context.SlashDocContextThreadLocal;
import springfox.documentation.schema.Annotations;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.schema.property.ModelSpecificationFactory;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.schema.ApiModelProperties;
import springfox.documentation.swagger.schema.ApiModelPropertyPropertyBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import static springfox.bean.validators.plugins.Validators.extractAnnotation;

/**
 * @author dy
 */
@Configuration
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SlashApiModelPropertyPropertyBuilder extends ApiModelPropertyPropertyBuilder {
    @Autowired
    public SlashApiModelPropertyPropertyBuilder(DescriptionResolver descriptions,
                                                ModelSpecificationFactory modelSpecifications) {
        super(descriptions, modelSpecifications);
    }

    @Override
    public void apply(ModelPropertyContext context) {
//        Optional<SlashDocContext> slashDocContext= SlashDocContextThreadLocal.get();
//        Optional<ApiModelProperty> apiModelProperty = extractAnnotation(context, ApiModelProperty.class);
//        if(apiModelProperty.isPresent()&&apiModelProperty.get().hidden())
//        {
//            context.getSpecificationBuilder().isHidden(true);
//        }
//        context.getDocumentationType().getName();

    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

}
