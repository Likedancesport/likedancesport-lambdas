package com.likedancesport.common.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "hls_group")
@NoArgsConstructor
public class HlsGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    private S3Key folderKey;

    @ElementCollection
    @CollectionTable(name = "resolution_output_mapping",
            joinColumns = {@JoinColumn(name = "hls_group_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "resolution")
    @Column(name = "hls_video_s3_key")
    private Map<String, S3Key> hlsVideos;
}
