package com.likedancesport.common.model.domain.marketplace;

import com.likedancesport.common.model.domain.IPreviewable;
import com.likedancesport.common.model.domain.S3Key;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity(name = "competition")
@Table(name = "competition")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competition implements IPreviewable {
    @Id
    private Long id;

    @Column
    private String name;

    @Column(name = "photo_s3_key")
    private S3Key photoS3Key;

    @Column(name = "date_of_issue")
    private Date dateOfIssue;

    @OneToMany(mappedBy = "competition")
    @OrderBy("participant_number")
    @ToString.Exclude
    private List<Participant> participants;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Competition that = (Competition) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
