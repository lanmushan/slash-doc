package site.lanmushan.slashdocstarter.plugin.validators;

import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static springfox.bean.validators.plugins.Validators.annotationFromBean;
import static springfox.bean.validators.plugins.Validators.annotationFromField;

/**
 * @author dy
 */
public interface BaseValidatorsAnnotationPlugin {
    /**
     *
     * @param classL
     * @param classR
     * @return
     */
    default boolean intersectClass(Class classL[], Class classR[]) {
        if (classL == null || classL.length == 0 || classR == null || classR.length == 0) {
            return false;
        }
        List classLList = Arrays.asList(classL);
        List classRList = Arrays.asList(classR);
        List collect1 = (List) classLList.stream().filter(num -> classRList.contains(num))
                .collect(Collectors.toList());
        return collect1.size() > 0;
    }
    default <T extends Annotation> Optional<T> extractAnnotation(ModelPropertyContext context, Class<T> aClass) {
        return annotationFromBean(context, aClass)
                .map(Optional::of)
                .orElse(annotationFromField(context, aClass));
    }
}
