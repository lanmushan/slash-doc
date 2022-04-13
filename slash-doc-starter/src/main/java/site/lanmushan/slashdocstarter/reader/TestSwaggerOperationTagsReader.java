package site.lanmushan.slashdocstarter.reader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;
import static springfox.documentation.service.Tags.emptyTags;

/**
 * @author dy
 */
@Configuration
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class TestSwaggerOperationTagsReader implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {
        context.operationBuilder().tags(
                Stream.concat(operationTags(context).stream(),
                        controllerTags(context).stream()).collect(toSet()));
    }

    private Set<String> controllerTags(OperationContext context) {
        Optional<Api> controllerAnnotation = context.findControllerAnnotation(Api.class);
        Optional<RequestMapping> requestMappingOptional = context.findControllerAnnotation(RequestMapping.class);
        Set<String> result= controllerAnnotation
                .map(tagsFromController())
                .orElse(new HashSet<>());
        if(result.isEmpty()&&requestMappingOptional.isPresent()&& StringUtils.hasText(requestMappingOptional.get().name()))
        {
            result.add(requestMappingOptional.get().name());
        }
        return result;
    }

    private Set<String> operationTags(OperationContext context) {
        Optional<ApiOperation> annotation = context.findAnnotation(ApiOperation.class);
        return new HashSet<>(annotation.map(tagsFromOperation())
                .orElse(new HashSet<>()));
    }

    private Function<ApiOperation, Set<String>> tagsFromOperation() {
        return input -> Stream.of(input.tags())
                .filter(emptyTags())
                .collect(toCollection(TreeSet::new));
    }

    private Function<Api, Set<String>> tagsFromController() {
        return input -> Stream.of(input.tags())
                .filter(emptyTags())
                .collect(toCollection(TreeSet::new));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
