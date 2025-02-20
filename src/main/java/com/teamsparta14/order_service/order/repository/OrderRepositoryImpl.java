package com.teamsparta14.order_service.order.repository;




import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamsparta14.order_service.order.entity.Order;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.teamsparta14.order_service.order.entity.QOrder.order1;


@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Order> searchByUserId(String userName, Pageable pageable) {


        List<Order> query = jpaQueryFactory
                .selectFrom(order1)
                .distinct()
                .where(
                        order1.userName.eq(userName),
                        order1.deletedAt.isNull()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Order> count = jpaQueryFactory
                .selectFrom(order1)
                .from(order1);


        return PageableExecutionUtils.getPage(query, pageable, count::fetchCount);
    }
}