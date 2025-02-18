package com.teamsparta14.order_service.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRefreshEntity is a Querydsl query type for RefreshEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRefreshEntity extends EntityPathBase<RefreshEntity> {

    private static final long serialVersionUID = 1402377638L;

    public static final QRefreshEntity refreshEntity = new QRefreshEntity("refreshEntity");

    public final com.teamsparta14.order_service.domain.QBaseEntity _super = new com.teamsparta14.order_service.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final StringPath expiration = createString("expiration");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath refresh = createString("refresh");

    public final StringPath username = createString("username");

    public QRefreshEntity(String variable) {
        super(RefreshEntity.class, forVariable(variable));
    }

    public QRefreshEntity(Path<? extends RefreshEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRefreshEntity(PathMetadata metadata) {
        super(RefreshEntity.class, metadata);
    }

}

