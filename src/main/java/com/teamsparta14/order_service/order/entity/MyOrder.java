package com.teamsparta14.order_service.order.entity;


import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.order.dto.OrderType;
import com.teamsparta14.order_service.payment.entity.Payment;
import com.teamsparta14.order_service.payment.entity.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;


import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "p_order")
public class MyOrder extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    @Column(name = "dest_id", nullable = false)
    private UUID destId;

    @Column(name = "is_deleted")
    Boolean isDeleted;

    @Column(name = "order_comment", nullable = true, length = 1000)
    private String orderComment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;

    public void addOrderProductsList(OrderProduct orderProduct){
        this.orderProducts.add(orderProduct);
    }


    public void createPayment(){
        this.payment = Payment.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .amount(getAmount(orderProducts))
                .userName(userName)
                .build();
    }

    private Long getAmount(List<OrderProduct> orderProducts) {
        Long amount = 0L;
        for (OrderProduct orderProduct : orderProducts) {
            amount += orderProduct.getPrice() * orderProduct.getQuantity();
        }
        return amount;
    }

    public void updateOrderProductList(List<OrderProduct> updateList) {
        this.orderProducts.clear();
        this.orderProducts.addAll(updateList);
    }

    public boolean isOwner(String userName){

        return this.userName.equals(userName);
    }
}