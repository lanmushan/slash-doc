package site.lanmushan.slashdocstarter.postProcessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import site.lanmushan.slashdocstarter.util.ReflectionUtil;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelKeyBuilder;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.schema.PropertySpecification;
import springfox.documentation.service.ContentSpecification;
import springfox.documentation.service.Representation;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spring.web.readers.operation.ContentParameterAggregator;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author dy
 */
//@Configuration
@Slf4j
public class SlashContentParameterAggregator  extends ContentParameterAggregator {
    @Override
    public Collection<RequestParameter> aggregate(Collection<RequestParameter> parameters) {
        log.info("执行参数解析:{}", JSON.toJSONString(parameters));
        for (RequestParameter parameter:parameters) {
            Optional<ContentSpecification> contentSpecification= parameter.getParameterSpecification().getContent();
            if(!contentSpecification.isPresent())
            {
                continue;
            }

            Set<Representation> representationSet= contentSpecification.get().getRepresentations();
            for (Representation representation: representationSet) {
                CompoundModelSpecificationBuilder modelFacetsBuilder=new CompoundModelSpecificationBuilder();
                modelFacetsBuilder.modelKey((r)->{
                    r.qualifiedModelName(c->{c.name("先休息");});
                });
                PropertySpecification propertySpecification=new PropertySpecificationBuilder("aaa")
                        .description("测试")
                        .build();
                modelFacetsBuilder.properties(Arrays.asList(propertySpecification));
                representation.getModel().getReference();
                ModelSpecification modelSpecification=new ModelSpecificationBuilder()
                        .name("test")
                        .compoundModel(r->{
                            r.properties(modelFacetsBuilder.build().getProperties());
                        })
                        .build();
                ReflectionUtil.setFieldValue(representation,"model",modelSpecification);
                log.info("模型定义:{}", JSONObject.toJSONString(representation));
            }
        }
        RequestParameter requestParameter=new RequestParameterBuilder()
                .name("testName")
                .description("描述")
                .build();
      //  return Arrays.asList(requestParameter);
        return super.aggregate(parameters);

    }
}
