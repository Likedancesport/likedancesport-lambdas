package com.likedancesport.temp.util.bpp;

import com.likedancesport.temp.util.CdkUtils;
import com.likedancesport.temp.util.annotation.SnapStart;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import software.amazon.awscdk.services.lambda.Function;

@Component
public class SnapStartAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final ConfigurableListableBeanFactory beanFactory;

    @Autowired
    public SnapStartAnnotationBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        SnapStart annotationOnBean = beanFactory.findAnnotationOnBean(beanName, SnapStart.class);
        if (bean.getClass() != Function.class) {
            return bean;
        }
        if (annotationOnBean != null) {
            CdkUtils.setSnapStart((Function) bean);
        }
        return bean;
    }
}
