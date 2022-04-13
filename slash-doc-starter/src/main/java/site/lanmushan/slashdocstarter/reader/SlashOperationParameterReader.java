package site.lanmushan.slashdocstarter.reader;

import com.fasterxml.classmate.ResolvedType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import springfox.documentation.common.Compatibility;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.OperationParameterReader;
import springfox.documentation.spring.web.readers.operation.ParameterAggregator;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.schema.Maps.isMapType;
import static springfox.documentation.schema.ScalarTypes.builtInScalarType;

/**
 * @author dy
 */
@Configuration
@Slf4j
public class SlashOperationParameterReader extends OperationParameterReader {
    private final ModelAttributeParameterExpander expander;
    private final EnumTypeDeterminer enumTypeDeterminer;
    private final ParameterAggregator aggregator;

    @Autowired
    private DocumentationPluginsManager pluginsManager;


    @Autowired
    public SlashOperationParameterReader(ModelAttributeParameterExpander expander, EnumTypeDeterminer enumTypeDeterminer, ParameterAggregator aggregator, ModelAttributeParameterExpander expander1, EnumTypeDeterminer enumTypeDeterminer1, ParameterAggregator aggregator1) {
        super(expander, enumTypeDeterminer, aggregator);
        this.expander = expander1;
        this.enumTypeDeterminer = enumTypeDeterminer1;
        this.aggregator = aggregator1;
    }

    @Override
    public void apply(OperationContext context) {
        context.operationBuilder().parameters(context.getGlobalOperationParameters());
        List<Compatibility<Parameter, RequestParameter>> compatibilities
                = readParameters(context);
        context.operationBuilder().parameters(
                compatibilities.stream()
                        .map(Compatibility::getLegacy)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));
        context.operationBuilder().requestParameters(new HashSet<>(context.getGlobalRequestParameters()));
        Collection<RequestParameter> requestParameters = compatibilities.stream()
                .map(Compatibility::getModern)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
        context.operationBuilder()
                .requestParameters(aggregator.aggregate(requestParameters));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private List<Compatibility<springfox.documentation.service.Parameter, RequestParameter>> readParameters(OperationContext context) {
        List<ResolvedMethodParameter> methodParameters = context.getParameters();
        List<Compatibility<springfox.documentation.service.Parameter, RequestParameter>> parameters = new ArrayList<>();
        log.debug("Reading parameters for method {} at path {}", context.getName(), context.requestMappingPattern());

        int index = 0;
        for (ResolvedMethodParameter methodParameter : methodParameters) {
            log.debug("Processing parameter {}", methodParameter.defaultName().orElse("<unknown>"));
            ResolvedType alternate = context.alternateFor(methodParameter.getParameterType());
            if (!shouldIgnore(methodParameter, alternate, context.getIgnorableParameterTypes())) {

                ParameterContext parameterContext = new ParameterContext(methodParameter,
                        context.getDocumentationContext(),
                        context.getGenericsNamingStrategy(),
                        context,
                        index++);

                if (shouldExpand(methodParameter, alternate)) {
                    parameters.addAll(
                            expander.expand(
                                    new ExpansionContext("", alternate, context)));
                } else {
                    log.debug("常规参数:{}",parameterContext);
                    parameters.add(pluginsManager.parameter(parameterContext));
                }
            }
        }
        return parameters.stream()
                .filter(hiddenParameter().negate())
                .collect(toList());
    }

    private Predicate<Compatibility<Parameter, RequestParameter>> hiddenParameter() {
        return c -> c.getLegacy()
                .map(springfox.documentation.service.Parameter::isHidden)
                .orElse(false);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean shouldIgnore(
            final ResolvedMethodParameter parameter,
            ResolvedType resolvedParameterType,
            final Set<Class> ignorableParamTypes) {

        if (ignorableParamTypes.contains(resolvedParameterType.getErasedType())) {
            return true;
        }
        return ignorableParamTypes.stream()
                .filter(Annotation.class::isAssignableFrom)
                .anyMatch(parameter::hasParameterAnnotation);

    }

    private boolean shouldExpand(final ResolvedMethodParameter parameter, ResolvedType resolvedParamType) {
        return !parameter.hasParameterAnnotation(RequestBody.class)
                && !parameter.hasParameterAnnotation(RequestPart.class)
                && !parameter.hasParameterAnnotation(RequestParam.class)
                && !parameter.hasParameterAnnotation(PathVariable.class)
                && !builtInScalarType(resolvedParamType.getErasedType()).isPresent()
                && !enumTypeDeterminer.isEnum(resolvedParamType.getErasedType())
                && !isContainerType(resolvedParamType)
                && !isMapType(resolvedParamType);
    }


}
