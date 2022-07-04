package site.lanmushan.slashdocstarter.configuration.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Data
@ConfigurationProperties(prefix = "slashdoc")
public class SlashDocProperties {
    private String title = "接口文档";
    private String basePackage;
    private String description = "接口描述";
    private String contactName = "";
    private String contactUrl = "";
    private String contactEmail = "";
    private String version = "1.0.0";
    private Boolean enabled = false;

    private Boolean enhance = false;
}
