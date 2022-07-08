package site.lanmushan.slashdocstarter.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import site.lanmushan.slashdocstarter.configuration.properties.SlashDocProperties;

import javax.annotation.PostConstruct;

/**
 * @author dy
 */
@Configuration
@Slf4j
@ComponentScan(basePackages = "site.lanmushan.slashdocstarter",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {SwaggerConfig.class, SlashDocProperties.class}))
@ConditionalOnProperty(prefix = "slashdoc", value = "enhance", havingValue = "true", matchIfMissing = false)
public class SlashDocConfiguration {
    @PostConstruct
    public void init() {
        log.info("初始化SlashDoc");
    }
}
