package site.lanmushan.slashdocstarter.reader;

import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import site.lanmushan.slashdocstarter.context.SlashDocContext;
import site.lanmushan.slashdocstarter.context.SlashDocContextThreadLocal;
import site.lanmushan.slashdocstarter.provider.SlashDefaultModelSpecificationProvider;
import site.lanmushan.slashdocstarter.util.ReflectionUtil;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelKey;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.schema.ReferenceModelSpecification;
import springfox.documentation.service.*;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.scanners.ModelSpecificationRegistryBuilder;

import java.util.*;


/**
 * @author dy
 */
@Configuration
@Slf4j
public class SlashApiDescriptionReader {
    @Autowired
    SlashApiModelSpecificationReader slashApiModelSpecificationReader;
    @Autowired
    SlashDefaultModelSpecificationProvider slashDefaultModelSpecificationProvider;


    public List<ApiDescription> read(ResourceGroup resourceGroup,
                                     RequestMappingContext requestMappingContext,
                                     Map<String, Model> models,
                                     List<ApiDescription> apiDescriptionList,
                                     ModelSpecificationRegistryBuilder registryBuilder) {
        for (ApiDescription apiDescription : apiDescriptionList) {
            List<Operation> operationList = apiDescription.getOperations();
            if (CollectionUtils.isEmpty(operationList)) {
                continue;
            }
            //遍历所有操作方法
            for (Operation operation : operationList) {
                extendOperation(operation, requestMappingContext, models, registryBuilder);
            }

        }
        return apiDescriptionList;
    }

    /**
     * 扩展方法
     *
     * @param requestMappingContext
     * @param models
     */
    public void extendOperation(Operation operation, RequestMappingContext requestMappingContext, Map<String, Model> models, ModelSpecificationRegistryBuilder registryBuilder) {
//        // 请求参数增强
//        List<Parameter> parameterList = operation.getParameters();
//        List<Parameter> newParameterList=new ArrayList<>(parameterList.size());
//        if (!CollectionUtils.isEmpty(parameterList)) {
//            for (Parameter parameter : parameterList) {
//                log.info("获取模型类型:{}",parameter.getModelRef().getType());
//            Model model=  models.get(parameter.getModelRef().getType());
//                log.info("参数:{}",parameter.getName(), JSONObject.toJSONString(model));
//                Parameter parameter1=new ParameterBuilder().build();
//                BeanUtils.copyProperties(parameter,parameter1);
//                ReflectionUtil.setFieldValue(parameter,"name",new String("测试"));
//                newParameterList.add(parameter1);
//            }
//        }
//        ReflectionUtil.setFieldValue(operation,"parameters",newParameterList);
//        ReflectionUtil.setFieldValue(operation,"parameters",newParameterList);
//        List<ModelSpecification> temp = slashApiModelSpecificationReader.read(requestMappingContext).stream()
//                .filter(m -> m.key().isPresent())
//                .collect(toList());
//        log.info("原来的：{}",temp);

        Set<ModelContext> modelContexts = slashApiModelSpecificationReader.modelContexts(requestMappingContext);
        Set<ModelSpecification> specifications = new LinkedHashSet<>();
//        for (ModelContext each : modelContexts) {
//            //slashDefaultModelSpecificationProvider.modelSpecificationsFor(each).ifPresent(specifications::add);
//            Set<ModelSpecification> modelSpecifications = slashApiModelSpecificationReader.read(each,requestMappingContext);
//            log.info("哈哈,{}",modelSpecifications.size());
//           //  specifications.addAll(modelProvider.modelDependenciesSpecifications(each));
//        }
        //解析请求参数
        String suffixName = "_" + requestMappingContext.getName();
        Set<ModelSpecification> modelSpecifications = new HashSet<>();
        Set<RequestParameter> requestParameters = operation.getRequestParameters();
        SlashDocContext slashDocContext = new SlashDocContext();

        for (RequestParameter requestParameter : requestParameters) {
            slashDocContext.setRequestParameter(requestParameter);
            slashDocContext.setRequestMappingContext(requestMappingContext);
            slashDocContext.setRequest(true);
            SlashDocContextThreadLocal.set(slashDocContext);
            Optional<ContentSpecification> optionalContentSpecification = requestParameter.getParameterSpecification().getContent();
            if (optionalContentSpecification.isPresent()) {
                ContentSpecification contentSpecification = optionalContentSpecification.get();
                Set<Representation> representations = contentSpecification.getRepresentations();
                for (Representation representation : representations) {
                    Optional<ReferenceModelSpecification> referenceModelSpecification = representation.getModel().getReference();
                    if (referenceModelSpecification.isPresent()) {
                        ModelKey modelKey = referenceModelSpecification.get().getKey();
                        ModelContext modelContext = getModelContent(modelKey, modelContexts);
                        ReflectionUtil.setFieldValue(modelKey.getQualifiedModelName(), "name", modelKey.getQualifiedModelName().getName() + suffixName);
                        modelSpecifications = slashApiModelSpecificationReader.read(suffixName, modelContext, requestMappingContext);

                    }

                }
            }
        }
        slashDocContext.setRequest(false);
        SlashDocContextThreadLocal.set(slashDocContext);
        //解析响应参数
        ModelContext returnNodeContext = getReturnModelContent(modelContexts);
        if (returnNodeContext != null) {
            Set<ModelSpecification> tempModelSpecification = slashApiModelSpecificationReader.read("", returnNodeContext, requestMappingContext);
            modelSpecifications.addAll(tempModelSpecification);
        }
        registryBuilder.addAll(modelSpecifications);
        // models.put("test",models.get("SysTbDict"));
        //  ReflectionUtil.setFieldValue(operation,"requestParameters",new HashSet<>());
        log.debug("扩展参数:{}", requestMappingContext.getName());
        SlashDocContextThreadLocal.remove();

    }

    public ModelContext getReturnModelContent(Set<ModelContext> modelContexts) {
        for (ModelContext modelContext : modelContexts) {
            String typeName = modelContext.getType().getErasedType().getName();
            if (modelContext.isReturnType()) {
                return modelContext;
            }
        }
        return null;
    }

    public ModelContext getModelContent(ModelKey modelKey, Set<ModelContext> modelContexts) {
        for (ModelContext modelContext : modelContexts) {
            String modelKeyName = modelKey.getQualifiedModelName().getNamespace() + "." + modelKey.getQualifiedModelName().getName();
            String typeName = modelContext.getType().getErasedType().getName();
            ApiModel apiModel = modelContext.getType().getErasedType().getAnnotation(ApiModel.class);
            String tName = "";
            if (apiModel != null) {
                tName = modelContext.getType().getErasedType().getPackage().getName() + "." + apiModel.value();
            }
            if (!modelContext.isReturnType() && (modelKeyName.equals(typeName) || modelKeyName.equals(tName))) {
                return modelContext;
            }
        }
        return null;
    }
}
