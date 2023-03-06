package com.likedancesport;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.nio.file.Path;

public class ComputeApp {
    public static void main(final String[] args) {
        App app = new App();
        new ComputeServerlessStack(app, "ComputeServerlessStackWithLayers", StackProps.builder()
                .env(Environment.builder()
                        .account("066002146890")
                        .region("eu-central-1")
                        .build())

                .build());

        app.synth();
    }
}

