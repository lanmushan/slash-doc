package site.lanmushan.slashdocstarter.scanner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import site.lanmushan.slashdocstarter.reader.SlashApiDescriptionReader;
import site.lanmushan.slashdocstarter.reader.SlashApiModelSpecificationReader;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.schema.Model;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.ModelSpecificationRegistry;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.paths.PathMappingAdjuster;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;
import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;
import static springfox.documentation.spi.service.contexts.Orderings.methodComparator;
import static springfox.documentation.spi.service.contexts.Orderings.resourceGroupComparator;
import static springfox.documentation.spring.web.paths.Paths.ROOT;

/**
 * 自定义的扫描器
 *
 * @author dy
 */
@Slf4j
@Configuration
public class SlashApiListingScannerMock extends ApiListingScanner {
    private final ApiDescriptionReader apiDescriptionReader;
    private final ApiModelReader apiModelReader;
    private final ApiModelSpecificationReader modelSpecificationReader;
    private final DocumentationPluginsManager pluginsManager;
    @Autowired
    SlashApiDescriptionReader slashApiDescriptionReader;
    @Autowired
    SlashApiModelSpecificationReader slashApiModelSpecificationReader;

    @Autowired
    public SlashApiListingScannerMock(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, ApiModelSpecificationReader modelSpecificationReader, DocumentationPluginsManager pluginsManager) {
        super(apiDescriptionReader, apiModelReader, modelSpecificationReader, pluginsManager);
        this.apiDescriptionReader = apiDescriptionReader;
        this.apiModelReader = apiModelReader;
        this.modelSpecificationReader = modelSpecificationReader;
        this.pluginsManager = pluginsManager;
    }

    static Optional<String> longestCommonPath(List<ApiDescription> apiDescriptions) {
        List<String> commons = new ArrayList<>();
        if (null == apiDescriptions || apiDescriptions.isEmpty()) {
            return empty();
        }
        List<String> firstWords = urlParts(apiDescriptions.get(0));

        for (int position = 0; position < firstWords.size(); position++) {
            String word = firstWords.get(position);
            boolean allContain = true;
            for (int i = 1; i < apiDescriptions.size(); i++) {
                List<String> words = urlParts(apiDescriptions.get(i));
                if (words.size() < position + 1 || !words.get(position).equals(word)) {
                    allContain = false;
                    break;
                }
            }
            if (allContain) {
                commons.add(word);
            }
        }
        return of("/" + commons.stream()
                .filter(Objects::nonNull)
                .collect(joining("/")));
    }

    private static List<String> urlParts(ApiDescription apiDescription) {
        return Stream.of(apiDescription.getPath().split("/"))
                .filter(((Predicate<String>) String::isEmpty).negate())
                .map(String::trim)
                .collect(toList());
    }

    @Override
    public Map<String, List<ApiListing>> scan(ApiListingScanningContext context) {
        final Map<String, List<ApiListing>> apiListingMap = new HashMap<>();
        int position = 0;

        Map<ResourceGroup, List<RequestMappingContext>> requestMappingsByResourceGroup = context.getRequestMappingsByResourceGroup();
        Collection<ApiDescription> additionalListings = pluginsManager.additionalListings(context);
        //获取所有的分组
        Set<ResourceGroup> allResourceGroups =
                concat(
                        stream(
                                collectResourceGroups(additionalListings).spliterator(),
                                false),
                        requestMappingsByResourceGroup.keySet().stream()).collect(toSet());
        //安全相关的引用
        List<SecurityReference> securityReferences = new ArrayList<>();
        //全局的model
        Map<String, Set<Model>> globalModelMap = new HashMap<>();
        //遍历所有分组
        for (ResourceGroup resourceGroup : sortedByName(allResourceGroups)) {

            DocumentationContext documentationContext = context.getDocumentationContext();
            Set<String> produces = new LinkedHashSet<>(documentationContext.getProduces());
            Set<String> consumes = new LinkedHashSet<>(documentationContext.getConsumes());
            String host = documentationContext.getHost();
            Set<String> protocols = new LinkedHashSet<>(documentationContext.getProtocols());
            ModelSpecificationRegistryBuilder modelRegistryBuilder = new ModelSpecificationRegistryBuilder();


            final Map<String, Model> models = new LinkedHashMap<>();
            Set<ApiDescription> apiDescriptions = new HashSet<>();

            List<RequestMappingContext> requestMappings = nullToEmptyList(requestMappingsByResourceGroup.get(resourceGroup));

//            for (RequestMappingContext each : sortedByMethods(requestMappingsByResourceGroup.get(resourceGroup))) {
//                List<ApiDescription> apiDescriptionList = apiDescriptionReader.read(each);
//                //apiDescriptions.addAll(apiDescriptionList);
//             apiDescriptions.addAll(slashApiDescriptionReader.read(resourceGroup, each, models, apiDescriptionList));
//            }
            //排序所有方法
            for (RequestMappingContext each : sortedByMethods(requestMappings)) {
                Map<String, Set<Model>> currentModelMap
//                        = apiModelReader.read(each.withKnownModels(
//                        globalModelMap));
                        = new HashMap<>();
               // log.info("收集方法:{}", each.getPatternsCondition().getPatterns());
//                List<ModelSpecification> modelSpecifications= modelSpecificationReader.read(each.withKnownModels(globalModelMap))
//                        .stream()
//                        .filter(m -> m.key().isPresent())
//                        .collect(toList());
//                List<ModelSpecification> temp = slashApiModelSpecificationReader.read(each.withKnownModels(globalModelMap)).stream()
//                        .filter(m -> m.key().isPresent())
//                        .collect(toList());
//                modelRegistryBuilder.addAll(
//                        temp
//                );
//                currentModelMap.values().forEach(list -> {
//                    for (Model model : list) {
//                        models.put(model.getName(), model);
//                    }
//                });
                globalModelMap.putAll(currentModelMap);
                List apiDescriptionList = apiDescriptionReader.read(each.withKnownModels(globalModelMap));
                apiDescriptions.addAll(slashApiDescriptionReader.read(resourceGroup, each, models, apiDescriptionList,modelRegistryBuilder));

            }
            List<ApiDescription> additional = additionalListings.stream()
                    .filter(
                            belongsTo(resourceGroup.getGroupName())
                                    .and(onlySelectedApis(documentationContext)))
                    .collect(toList());
            apiDescriptions.addAll(additional);
            List<ApiDescription> sortedApis = apiDescriptions.stream()
                    .sorted(documentationContext.getApiDescriptionOrdering()).collect(toList());
            String resourcePath = new ResourcePathProvider(resourceGroup)
                    .resourcePath()
                    .orElse(
                            longestCommonPath(sortedApis)
                                    .orElse(null));


            PathAdjuster adjuster = new PathMappingAdjuster(documentationContext);
            ModelSpecificationRegistry modelRegistry = modelRegistryBuilder.build();
            ModelNamesRegistry modelNamesRegistry =
                    pluginsManager.modelNamesGeneratorFactory(documentationContext.getDocumentationType())
                            .modelNamesRegistry(modelRegistry);
            log.trace("Models in the name registry {}", modelNamesRegistry.modelsByName().keySet());
            ApiListingBuilder apiListingBuilder = new ApiListingBuilder(context.apiDescriptionOrdering())
                    .apiVersion(documentationContext.getApiInfo().getVersion())
                    .basePath(adjuster.adjustedPath(ROOT))
                    .resourcePath(resourcePath)
                    .produces(produces)
                    .consumes(consumes)
                    .host(host)
                    .protocols(protocols)
                    .securityReferences(securityReferences)
                    .apis(sortedApis)
                    .models(models)
                    .modelSpecifications(modelNamesRegistry.modelsByName())
                    .modelNamesRegistry(modelNamesRegistry)
                    .position(position++)
                    .availableTags(documentationContext.getTags());

            ApiListingContext apiListingContext = new ApiListingContext(
                    context.getDocumentationType(),
                    resourceGroup,
                    apiListingBuilder);
            //获取apiListing
            ApiListing apiListing = pluginsManager.apiListing(apiListingContext);
            //把收集到的接口放进这个分组里面
            apiListingMap.putIfAbsent(
                    resourceGroup.getGroupName(),
                    new LinkedList<>());
            apiListingMap.get(resourceGroup.getGroupName()).add(apiListing);
        }
        return apiListingMap;
    }

    private Predicate<ApiDescription> onlySelectedApis(final DocumentationContext context) {
        return input -> context.getApiSelector().getPathSelector().test(input.getPath());
    }

    private Iterable<RequestMappingContext> sortedByMethods(List<RequestMappingContext> contexts) {
        return contexts.stream().sorted(methodComparator()).collect(toList());
    }

    static Iterable<ResourceGroup> collectResourceGroups(Collection<ApiDescription> apiDescriptions) {
        return apiDescriptions.stream()
                .map(toResourceGroups()).collect(toList());
    }

    static Iterable<ResourceGroup> sortedByName(Set<ResourceGroup> resourceGroups) {
        return resourceGroups.stream().sorted(resourceGroupComparator()).collect(toList());
    }

    static Predicate<ApiDescription> belongsTo(final String groupName) {
        return input -> !input.getGroupName().isPresent()
                || groupName.equals(input.getGroupName().get());
    }

    private static Function<ApiDescription, ResourceGroup> toResourceGroups() {
        return input -> new ResourceGroup(
                input.getGroupName().orElse(Docket.DEFAULT_GROUP_NAME),
                null);
    }
}
