package com.teamsparta14.order_service.review.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.review.dto.ReviewRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name="p_review")
@NoArgsConstructor
@AllArgsConstructor
public class Review  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private UUID storeId;

    @NotNull
    private String userName;

    @NotNull
    private String review;

    @Enumerated(EnumType.STRING)
    private Stars star;

    @ColumnDefault("false")
    private boolean isDeleted;

    public static Review from(ReviewRequestDto requestDto, String user) {
        return Review.builder()
                .storeId(requestDto.getStoreId())
                .userName(user)
                .review(requestDto.getReview())
                .star(requestDto.getStar())
                .build();
    }

    public void update(ReviewRequestDto requestDto, String user) {
        this.storeId = requestDto.getStoreId();
        this.userName = user;
        this.review = requestDto.getReview();
        this.star = requestDto.getStar();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
