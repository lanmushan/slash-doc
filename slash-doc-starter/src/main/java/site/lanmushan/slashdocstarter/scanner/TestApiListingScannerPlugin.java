package site.lanmushan.slashdocstarter.scanner;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author dy
 */
//@Configuration
@Slf4j
public class TestApiListingScannerPlugin implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        RequestParameter parameter = new RequestParameterBuilder()
                .description("用户名")
                .name("username")
                .required(true)
                .build();
        Collection collection = Arrays.asList(parameter);
        Operation operation = new OperationBuilder(
                new CachingOperationNameGenerator())
                //http请求类型
                .method(HttpMethod.POST)
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .summary("获取tok1en")
                //归类标签
                .tags(Sets.newHashSet("测试"))
                .requestParameters(collection)
                .build();
        List opList = Arrays.asList(operation);
        ApiDescription apiDescription = new ApiDescription("测试", "/test", "xxx", "xdfe", opList, false);
        return Arrays.asList(apiDescription);
    }


    @Override
    public boolean supports(DocumentationType documentationType) {
        log.info("测试");

        return false;
    }
}
