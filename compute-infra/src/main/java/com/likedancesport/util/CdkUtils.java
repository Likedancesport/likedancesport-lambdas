package com.likedancesport.util;

import software.amazon.awscdk.services.lambda.CfnFunction;
import software.amazon.awscdk.services.lambda.IFunction;

public class CdkUtils {
    public static void setSnapStart(IFunction lambda) {
        CfnFunction.SnapStartProperty snapStartProperty = CfnFunction.SnapStartProperty.builder()
                .applyOn("PublishedVersions")
                .build();
        ((CfnFunction) lambda.getNode().getDefaultChild()).setSnapStart(snapStartProperty);
    }
}
