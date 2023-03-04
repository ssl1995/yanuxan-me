package com.msb.framework.mq.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * @author liao
 */
public class ClassPathMessageQueueScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathMessageQueueScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @NonNull
    @Override
    protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        for (BeanDefinitionHolder definitionHolder : beanDefinitionHolders) {
            AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) definitionHolder.getBeanDefinition();
            String beanClassName = beanDefinition.getBeanClassName();
            beanDefinition.setBeanClass(MessageQueueFactoryBean.class);
            if (beanClassName == null) {
                continue;
            }
            //
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            beanDefinition.setPrimary(true);
        }
        return beanDefinitionHolders;
    }
}
