package site.lanmushan.slashdocstarter.scanner;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.service.ResourceGroup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

/**
 * @author dy
 */
public class ResourcePathProvider {
    private final ResourceGroup resourceGroup;

    ResourcePathProvider(ResourceGroup resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    public Optional<String> resourcePath() {
        return ofNullable(
                controllerClass()
                        .map(resourcePathExtractor())
                        .filter(((Predicate<String>) String::isEmpty).negate())
                        .orElse(null));
    }

    private Function<Class<?>, String> resourcePathExtractor() {
        return input -> {
            Optional<String> path = Arrays.stream(paths(input))
                    .findFirst().filter(((Predicate<String>) String::isEmpty)
                            .negate());
            if (!path.isPresent()) {
                return "";
            }
            if (path.get().startsWith("/")) {
                return path.get();
            }
            return "/" + path.get();
        };
    }

    String[] paths(Class<?> controller) {
        RequestMapping annotation
                = AnnotationUtils.findAnnotation(controller, RequestMapping.class);
        if (annotation != null) {
            return annotation.path();
        }
        return new String[] {};
    }

    private Optional<? extends Class<?>> controllerClass() {
        return resourceGroup.getControllerClass();
    }
}
