package site.lanmushan.slashdocstarter.plugin.validators;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Configuration;
import site.lanmushan.slashdocstarter.annotations.ApiModelEnumMapping;
import site.lanmushan.slashdocstarter.api.ApiMapping;
import site.lanmushan.slashdocstarter.context.SlashDocContextThreadLocal;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author dy
 */
@Configuration
public class SlashApiModelEnumMappingAnnotationPlugin1 implements ModelPropertyBuilderPlugin, BaseValidatorsAnnotationPlugin {
    @Override
    public void apply(ModelPropertyContext context) {
        SlashDocContextThreadLocal.get().ifPresent(ctx -> {
            Optional<ApiModelProperty> apiModelProperty = extractAnnotation(context, ApiModelProperty.class);
            Optional<ApiModelEnumMapping> apiModelEnumMapping = extractAnnotation(context, ApiModelEnumMapping.class);
            if (!apiModelEnumMapping.isPresent() || apiModelEnumMapping.get().value() == null) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (apiModelProperty.isPresent()) {
                stringBuilder.append(apiModelProperty.get().value());
            }
            Class aClass = apiModelEnumMapping.get().value();

            List<ApiMapping> apiMappings = create(aClass);
            if(apiMappings!=null&&apiMappings.size()>0)
            {
                stringBuilder.append(",字典:"+apiMappings.toString());
                context.getSpecificationBuilder()
                        .description(stringBuilder.toString());
                context.getBuilder().description(stringBuilder.toString());
            }

        });
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    public List<ApiMapping> create(Class aClass) {
        try {
            Object o = null;
            Method method = aClass.getMethod("readMapping");
            if (aClass.isEnum()) {
                Object[] oo = aClass.getEnumConstants();
                o = oo[0];
            } else {
                o = aClass.newInstance();
            }
            return (List<ApiMapping>) method.invoke(o, null);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
