package site.lanmushan.slashdocstarter.reader;

import com.fasterxml.classmate.TypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import site.lanmushan.slashdocstarter.provider.SlashDefaultModelSpecificationProvider;
import site.lanmushan.slashdocstarter.util.ReflectionUtil;
import springfox.documentation.schema.ModelKey;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.schema.PropertySpecification;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dy
 */
@Configuration
@Slf4j
public class SlashApiModelSpecificationReader {
    @Autowired
    private SlashDefaultModelSpecificationProvider modelProvider;
    @Autowired
    private DocumentationPluginsManager pluginsManager;
    @Autowired
    private TypeResolver resolver;


    public Set<ModelSpecification> read(RequestMappingContext context) {
        log.debug("解析参数:{}", context.getName());
        Set<ModelSpecification> specifications = new HashSet<>();
        Set<ModelContext> modelContexts = pluginsManager.modelContexts(context);
        for (ModelContext each : modelContexts) {
            markIgnorablesAsHasSeen(
                    context.getIgnorableParameterTypes(),
                    each);
            modelProvider.modelSpecificationsFor(each)
                    .ifPresent(specifications::add);
            specifications.addAll(modelProvider.modelDependenciesSpecifications(each));
        }
        return specifications;
    }

    public Set<ModelSpecification> read(String suffixName, ModelContext modelContext, RequestMappingContext context) {
        markIgnorablesAsHasSeen(context.getIgnorableParameterTypes(), modelContext);
        Set<ModelSpecification> specifications = new HashSet<>();
        modelProvider.modelSpecificationsFor(modelContext).ifPresent(it -> {
            ReflectionUtil.setFieldValue(it, "name", it.getName() +suffixName);
            ModelKey modelSpecificationsModelKey = it.getCompound().get().getModelKey();
            ReflectionUtil.setFieldValue(modelSpecificationsModelKey.getQualifiedModelName(), "name", modelSpecificationsModelKey.getQualifiedModelName().getName() + suffixName);
            Collection<PropertySpecification> propertySpecifications= it.getCompound().get().getProperties();
            for (PropertySpecification propertySpecification :propertySpecifications) {
                propertySpecification.getType().getReference().ifPresent(ref->{
                     ModelKey refModelKey=ref.getKey();
                    ReflectionUtil.setFieldValue(refModelKey.getQualifiedModelName(), "name", refModelKey.getQualifiedModelName().getName()+ suffixName);
                });
            }
            specifications.add(it);
        });
        Set<ModelSpecification>  modelDependenciesSpecificationSet= modelProvider.modelDependenciesSpecifications(modelContext);
        for (ModelSpecification specification: modelDependenciesSpecificationSet) {
            ModelKey modelSpecificationsModelKey = specification.getCompound().get().getModelKey();
            ReflectionUtil.setFieldValue(specification, "name", specification.getName() +suffixName);
            ReflectionUtil.setFieldValue(modelSpecificationsModelKey.getQualifiedModelName(), "name", modelSpecificationsModelKey.getQualifiedModelName().getName() + suffixName);
        }
        specifications.addAll(modelDependenciesSpecificationSet);
        return specifications;
    }

    public Set<ModelSpecification> modelDependenciesSpecifications(ModelContext modelContext, RequestMappingContext context) {
        Set<ModelSpecification> specifications = new HashSet<>();
        return modelProvider.modelDependenciesSpecifications(modelContext);
    }

    public Set<ModelContext> modelContexts(RequestMappingContext context) {
        log.debug("解析参数:{}", context.getName());
        Set<ModelSpecification> specifications = new HashSet<>();
        Set<ModelContext> modelContexts = pluginsManager.modelContexts(context);
        for (ModelContext each : modelContexts) {
            markIgnorablesAsHasSeen(
                    context.getIgnorableParameterTypes(),
                    each);

        }
        return modelContexts;
    }

    private void markIgnorablesAsHasSeen(
            Set<Class> ignorableParameterTypes,
            ModelContext modelContext) {

        for (Class ignorableParameterType : ignorableParameterTypes) {
            modelContext.seen(resolver.resolve(ignorableParameterType));
        }
    }
}
