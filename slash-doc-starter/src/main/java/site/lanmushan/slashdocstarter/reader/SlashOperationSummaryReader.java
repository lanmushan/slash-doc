package site.lanmushan.slashdocstarter.reader;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.util.Optional;

/**
 * 处理接口名称
 * @author dy
 */
@Configuration
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class SlashOperationSummaryReader implements OperationBuilderPlugin {


    @Override
    public void apply(OperationContext context) {
        Optional<ApiOperation> apiOperationAnnotation = context.findAnnotation(ApiOperation.class);
        if (apiOperationAnnotation.isPresent() && StringUtils.hasText(((ApiOperation) apiOperationAnnotation.get()).value())) {
            context.operationBuilder().summary(apiOperationAnnotation.get().value());
            return;
        }
        Optional<RequestMapping> requestMappingOptional = context.findAnnotation(RequestMapping.class);
        if (requestMappingOptional.isPresent() && StringUtils.hasText(requestMappingOptional.get().name())) {
            context.operationBuilder().summary(requestMappingOptional.get().name());
            return;
        }
        Optional<GetMapping> getMappingOptional = context.findAnnotation(GetMapping.class);
        if (getMappingOptional.isPresent() && StringUtils.hasText(getMappingOptional.get().name())) {
            context.operationBuilder().summary(getMappingOptional.get().name());
            return;
        }
        Optional<PostMapping> postMappingOptional = context.findAnnotation(PostMapping.class);
        if (postMappingOptional.isPresent() && StringUtils.hasText(postMappingOptional.get().name())) {
            context.operationBuilder().summary(postMappingOptional.get().name());
            return;
        }
        Optional<PutMapping> putMappingOptional = context.findAnnotation(PutMapping.class);
        if (putMappingOptional.isPresent() && StringUtils.hasText(putMappingOptional.get().name())) {
            context.operationBuilder().summary(putMappingOptional.get().name());
            return;
        }
        Optional<DeleteMapping> deleteMappingOptional = context.findAnnotation(DeleteMapping.class);
        if (deleteMappingOptional.isPresent() && StringUtils.hasText(deleteMappingOptional.get().name())) {
            context.operationBuilder().summary(deleteMappingOptional.get().name());
            return;
        }
        Optional<PatchMapping> patchMappingOptional = context.findAnnotation(PatchMapping.class);
        if (patchMappingOptional.isPresent() && StringUtils.hasText(patchMappingOptional.get().name())) {
            context.operationBuilder().summary(patchMappingOptional.get().name());
            return;
        }
    }

    private String getMappingName() {
        return null;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
