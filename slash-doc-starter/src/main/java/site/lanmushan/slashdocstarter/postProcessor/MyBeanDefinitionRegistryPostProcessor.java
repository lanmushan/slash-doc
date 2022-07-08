package site.lanmushan.slashdocstarter.postProcessor;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        log.info("bean 定义查看和修改...{}", beanDefinitionRegistry.getBeanDefinitionNames().length);
        String beanName = "swaggerApiListingReader";
// 先移除原来的bean定义
        beanDefinitionRegistry.removeBeanDefinition(beanName);
        beanDefinitionRegistry.removeBeanDefinition("apiListingScanner");
        beanDefinitionRegistry.removeBeanDefinition("operationParameterReader");
//        beanDefinitionRegistry.removeBeanDefinition("contentParameterAggregator");
        beanDefinitionRegistry.removeBeanDefinition("parameterDataTypeReader");
        beanDefinitionRegistry.removeBeanDefinition("modelAttributeParameterExpander");
        // beanDefinitionRegistry.removeBeanDefinition("apiModelPropertyPropertyBuilder");
        beanDefinitionRegistry.removeBeanDefinition("defaultModelSpecificationProvider");
        if (beanDefinitionRegistry.containsBeanDefinition("minMaxPlugin")) {
            beanDefinitionRegistry.removeBeanDefinition("minMaxPlugin");

        }
        if (beanDefinitionRegistry.containsBeanDefinition("notBlankPlugin")) {
            beanDefinitionRegistry.removeBeanDefinition("notBlankPlugin");

        }
        if (beanDefinitionRegistry.containsBeanDefinition("sizePlugin")) {
            beanDefinitionRegistry.removeBeanDefinition("sizePlugin");
        }

// 注册我们自己的bean定义
//        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(TestSwaggerApiListingReader.class);
// 如果有构造函数参数, 有几个构造函数的参数就设置几个 没有就不用设置
//        beanDefinitionBuilder.addConstructorArgValue("构造参数1");
//        beanDefinitionBuilder.addConstructorArgValue("构造参数2");
//        beanDefinitionBuilder.addConstructorArgValue("构造参数3");
// 设置 init方法 没有就不用设置
        //   beanDefinitionBuilder.setInitMethodName("init");
// 设置 destory方法 没有就不用设置
        //   beanDefinitionBuilder.setDestroyMethodName("destory");
// 将Bean 的定义注册到Spring环境
        //  beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}