package org.HJan.dto;

import lombok.Data;
import org.HJan.entity.ShopOrders;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderAppDto {

    public OrderAppDto(ShopOrders shopOrders){
        this.orderId = shopOrders.getOrderId();
        this.userId = shopOrders.getUserId();
        this.productId = shopOrders.getProductId();
        this.productPrice = shopOrders.getProductPrice();
        this.quantity = shopOrders.getQuantity();
        this.totalPrice = shopOrders.getTotalPrice();
        this.createTime = shopOrders.getCreateTime();
        this.name = shopOrders.getName();
        this.phone = shopOrders.getPhone();
        this.address = shopOrders.getAddress();
        this.status = shopOrders.getStatus();
    }

    public OrderAppDto() {
    }

    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private BigDecimal productPrice;
    private String productName;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Date createTime;
    private String name;
    private String phone;
    private String address;
    private String status;
    private String imgUrl;
}
