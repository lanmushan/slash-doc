package site.lanmushan.slashdocstarter.context;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

/**
 * @author dy
 */
@Data
@Slf4j
public class SlashDocContext {
    private RequestMappingContext requestMappingContext;
    private RequestParameter requestParameter;
    private boolean request;


    public boolean isRequest(){
        return request;
    }

    /**
     * 查找方法注解
     *
     * @param annotation
     * @param <T>
     * @return
     */
    public <T extends Annotation> Optional<T> findMethodAnnotation(Class<T> annotation) {
        return requestMappingContext.findAnnotation(annotation);
    }

    /**
     * 查找参数注解
     *
     * @param annotation
     * @param <T>
     * @return
     */
    public <T extends Annotation> Optional<T> findRequestParameterAnnotation(Class<T> annotation) {
        List<ResolvedMethodParameter> resolvedMethodParameter = requestMappingContext.getParameters();
        return (Optional<T>) resolvedMethodParameter.get(requestParameter.getParameterIndex()).findAnnotation(annotation);
    }

    public <T extends Annotation> Optional<T> findAnnotation(Class<T> annotation) {
        Optional<T> optionalT = findMethodAnnotation(annotation);
        return optionalT.isPresent() ? optionalT : findRequestParameterAnnotation(annotation);
    }
}
