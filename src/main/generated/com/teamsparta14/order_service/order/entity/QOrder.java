package com.teamsparta14.order_service.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -180241837L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order1 = new QOrder("order1");

    public final com.teamsparta14.order_service.domain.QBaseEntity _super = new com.teamsparta14.order_service.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final ComparablePath<java.util.UUID> destId = createComparable("destId", java.util.UUID.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final ComparablePath<java.util.UUID> order = createComparable("order", java.util.UUID.class);

    public final StringPath orderComment = createString("orderComment");

    public final ListPath<OrderProduct, QOrderProduct> orderProducts = this.<OrderProduct, QOrderProduct>createList("orderProducts", OrderProduct.class, QOrderProduct.class, PathInits.DIRECT2);

    public final EnumPath<com.teamsparta14.order_service.order.dto.OrderType> orderType = createEnum("orderType", com.teamsparta14.order_service.order.dto.OrderType.class);

    public final com.teamsparta14.order_service.payment.entity.QPayment payment;

    public final ComparablePath<java.util.UUID> storeId = createComparable("storeId", java.util.UUID.class);

    public final StringPath userName = createString("userName");

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payment = inits.isInitialized("payment") ? new com.teamsparta14.order_service.payment.entity.QPayment(forProperty("payment"), inits.get("payment")) : null;
    }

}

