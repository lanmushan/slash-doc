package site.lanmushan.slashdocstarter.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author dy
 */
@Configuration
@Slf4j
@ComponentScan(basePackages = "site.lanmushan.slashdocstarter")
@ConditionalOnProperty(prefix = "slashdoc", value = "enabled", havingValue = "false")
public class SlashDocConfiguration {
    @PostConstruct
    public void init() {
        log.info("初始化SlashDoc");
    }
}
