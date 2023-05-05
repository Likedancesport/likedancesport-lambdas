package com.likedancesport.common.lifecycle;

import com.likedancesport.common.annotation.InjectSsmParameter;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.marketplace.MarketplaceMediaResource;
import com.likedancesport.common.service.S3StorageService;
import com.likedancesport.common.utils.constants.ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MarketplaceMediaResourceEntityListener extends BaseS3StorableEntityListener<MarketplaceMediaResource> {
//    @InjectSsmParameter(parameterName = ParameterNames.MARKETPLACE_ASSETS_BUCKET_NAME)
    private String marketplaceAssetsBucketName;

    @Autowired
    public MarketplaceMediaResourceEntityListener(S3StorageService s3StorageService) {
        super(s3StorageService);
    }

    @Override
    public void deleteAssets(MarketplaceMediaResource entity) {
        super.deleteAssets(entity);
        s3StorageService.deleteObject(entity.getResourceS3Key());
    }

    @Override
    public void prePersist(MarketplaceMediaResource entity) {
        super.prePersist(entity);
        S3Key resourceS3Key = S3Key.of(marketplaceAssetsBucketName, entity.getPrefix() + "/" + UUID.randomUUID());
        entity.setResourceS3Key(resourceS3Key);
    }
}
