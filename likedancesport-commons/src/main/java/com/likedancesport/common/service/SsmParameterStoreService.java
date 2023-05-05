package com.likedancesport.common.service;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SsmParameterStoreService {
    private final AWSSimpleSystemsManagement ssm;

    private final Map<String, String> parameterCache;

    @Autowired
    public SsmParameterStoreService(AWSSimpleSystemsManagement ssm) {
        this.ssm = ssm;
        this.parameterCache = new HashMap<>();
    }

    public String getParameter(String parameterName, boolean encrypted) {
        String parameterValue = parameterCache.get(parameterName);
        if (parameterValue != null) {
            return parameterValue;
        }
        parameterValue = getParameterValue(parameterName, encrypted);
        parameterCache.put(parameterName, parameterValue);
        return parameterValue;
    }

    private String getParameterValue(String parameterName, boolean encrypted) {
        GetParameterRequest getParameterRequest = new GetParameterRequest();
        getParameterRequest.setName(parameterName);
        getParameterRequest.withWithDecryption(encrypted);
        GetParameterResult parameterResult = ssm.getParameter(getParameterRequest);
        return parameterResult.getParameter().getValue();
    }
}
