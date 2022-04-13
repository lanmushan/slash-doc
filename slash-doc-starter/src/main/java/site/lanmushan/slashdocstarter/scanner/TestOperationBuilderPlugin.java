package site.lanmushan.slashdocstarter.scanner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.OperationContext;

/**
 * @author dy
 */
//@Configuration
@Slf4j
public class TestOperationBuilderPlugin  implements OperationBuilderPlugin {
    @Override
    public void apply(OperationContext context) {
        DocumentationContext documentationContext= context.getDocumentationContext();
       log.info(context.getName());
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
