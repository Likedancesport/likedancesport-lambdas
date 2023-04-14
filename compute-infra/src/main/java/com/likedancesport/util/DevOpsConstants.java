package com.likedancesport.util;

import software.amazon.awscdk.services.lambda.HttpMethod;

public class DevOpsConstants {
    public static final String POST = HttpMethod.POST.name();
    public static final String DELETE = HttpMethod.DELETE.name();
    public static final String PUT = HttpMethod.PUT.name();
    public static final String GET = HttpMethod.GET.name();
}
