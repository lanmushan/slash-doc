package site.lanmushan.slashdocstarter.annotations;

import site.lanmushan.slashdocstarter.api.ApiModelEnumMappingProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelEnumMapping {
    Class<?> value();
}
