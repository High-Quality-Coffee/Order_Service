package com.teamsparta14.order_service.order.repository;



import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;


import com.teamsparta14.order_service.order.entity.MyOrder;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import java.util.List;
import java.util.UUID;

import static com.teamsparta14.order_service.order.entity.QMyOrder.myOrder;
import static com.teamsparta14.order_service.order.entity.QOrderProduct.orderProduct;
import static com.teamsparta14.order_service.payment.entity.QPayment.payment;


@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MyOrder> searchByUserName(String userName, Pageable pageable) {

        OrderSpecifier<?>[] orderSpecifiers = buildOrderSpecifiers(pageable);

        List<MyOrder> query = jpaQueryFactory
                .selectFrom(myOrder)
                .where(
                        myOrder.userName.eq(userName),
                        myOrder.deletedAt.isNull()
                )
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<MyOrder> count = jpaQueryFactory
                .selectFrom(myOrder)
                .from(myOrder);




        return PageableExecutionUtils.getPage(query, pageable, count::fetchCount);
    }

    @Override
    public Page<MyOrder> searchByStoreId(String userName, Pageable pageable, String storeId) {
        OrderSpecifier<?>[] orderSpecifiers = buildOrderSpecifiers(pageable);

        List<MyOrder> query = jpaQueryFactory
                .selectFrom(myOrder)
                .where(
                        myOrder.storeId.eq(UUID.fromString(storeId)),
                        myOrder.deletedAt.isNull()
                )
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<MyOrder> count = jpaQueryFactory
                .selectFrom(myOrder)
                .from(myOrder);




        return PageableExecutionUtils.getPage(query, pageable, count::fetchCount);
    }

    public Page<MyOrder> searchAllOrders(Pageable pageable){

        OrderSpecifier<?>[] orderSpecifiers = buildOrderSpecifiers(pageable);
        List<MyOrder> query = jpaQueryFactory
                .selectFrom(myOrder)
                .leftJoin(myOrder.payment, payment)
                .leftJoin(myOrder.orderProducts, orderProduct)
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<MyOrder> count = jpaQueryFactory
                .selectFrom(myOrder)
                .from(myOrder);
        return PageableExecutionUtils.getPage(query, pageable, count::fetchCount);
    }

    private OrderSpecifier<?>[] buildOrderSpecifiers(Pageable pageable) {

        return pageable.getSort().stream()
                .map(param -> {
                    String property = param.getProperty();
                    return switch (property) {
                        case "orderId" -> new OrderSpecifier<>(Order.DESC, myOrder.orderId);
                        case "orderType" -> new OrderSpecifier<>(Order.DESC, myOrder.orderType);
                        default -> new OrderSpecifier<>(Order.DESC, myOrder.createdAt);
                    };
                })
                .toArray(OrderSpecifier[]::new);




    }
}
