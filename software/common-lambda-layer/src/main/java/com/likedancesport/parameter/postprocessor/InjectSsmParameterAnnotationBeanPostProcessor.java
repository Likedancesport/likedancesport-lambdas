package com.likedancesport.parameter.postprocessor;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.likedancesport.parameter.annotation.InjectSsmParameter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
public class InjectSsmParameterAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final AWSSimpleSystemsManagement ssm;

    @Autowired
    public InjectSsmParameterAnnotationBeanPostProcessor(AWSSimpleSystemsManagement ssm) {
        this.ssm = ssm;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            InjectSsmParameter annotation = field.getAnnotation(InjectSsmParameter.class);
            if (annotation != null) {
                if (field.getType() != String.class) {
                    throw new RuntimeException("Annotation is not applicable to non-String types");
                }
                GetParameterRequest getParameterRequest = new GetParameterRequest();
                getParameterRequest.setName(annotation.parameterName());
                getParameterRequest.withWithDecryption(annotation.encrypted());
                GetParameterResult parameterResult = ssm.getParameter(getParameterRequest);
                String parameterValue = parameterResult.getParameter().getValue();
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, parameterValue);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
