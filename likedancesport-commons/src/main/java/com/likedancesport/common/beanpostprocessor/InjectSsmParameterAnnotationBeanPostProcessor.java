package com.likedancesport.common.beanpostprocessor;

import com.likedancesport.common.annotation.InjectSsmParameter;
import com.likedancesport.common.service.SsmParameterStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
@Primary
@Slf4j
public class InjectSsmParameterAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final SsmParameterStoreService ssm;

    @Autowired
    public InjectSsmParameterAnnotationBeanPostProcessor(SsmParameterStoreService ssm) {
        log.info("-----INIT SSM BPP");
        this.ssm = ssm;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        injectAnnotatedFields(bean, bean.getClass());
        return bean;
    }

    private void injectAnnotatedFields(Object bean, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            InjectSsmParameter annotation = field.getAnnotation(InjectSsmParameter.class);
            if (annotation != null) {
                if (field.getType() != String.class) {
                    throw new RuntimeException("Annotation is not applicable to non-String fields");
                }
                String parameterValue = ssm.getParameter(annotation.parameterName(), annotation.encrypted());
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, parameterValue);
            }
        }
        if (clazz.getSuperclass() == Object.class) {
            return;
        }
        injectAnnotatedFields(bean, clazz.getSuperclass());
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
