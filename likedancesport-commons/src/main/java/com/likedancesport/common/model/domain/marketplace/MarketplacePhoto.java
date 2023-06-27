package com.likedancesport.common.model.domain.marketplace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@SuperBuilder
@Entity(name = "MarketplacePhoto")
@Table(name = "marketplace_photo")
@AllArgsConstructor
public class MarketplacePhoto extends MarketplaceMediaResource {
    @Override
    public String getS3KeyPrefix() {
        return "photos";
    }
}
