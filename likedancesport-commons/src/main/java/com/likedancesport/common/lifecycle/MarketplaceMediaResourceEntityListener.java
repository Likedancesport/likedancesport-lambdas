package com.likedancesport.common.lifecycle;

import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.marketplace.MarketplaceMediaResource;
import com.likedancesport.common.service.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MarketplaceMediaResourceEntityListener {
//    @InjectSsmParameter(parameterName = ParameterNames.MARKETPLACE_ASSETS_BUCKET_NAME)
    private String marketplaceAssetsBucketName;
    private final S3StorageService s3StorageService;

    @Autowired
    public MarketplaceMediaResourceEntityListener(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    public void deleteAssets(MarketplaceMediaResource entity) {
        s3StorageService.deleteObject(entity.getResourceS3Key());
    }

    public void prePersist(MarketplaceMediaResource entity) {
        S3Key resourceS3Key = S3Key.of(marketplaceAssetsBucketName, entity.getS3KeyPrefix() + "/" + UUID.randomUUID());
        entity.setResourceS3Key(resourceS3Key);
    }
}
