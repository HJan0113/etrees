package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 *
 * @TableName orders
 */
@TableName(value = "shop_orders")
@Data
public class ShopOrders implements Serializable {

    public ShopOrders() {
    }

    public ShopOrders(ShopCart shopCart) {
        this.productId = shopCart.getProductId();
        this.productPrice = shopCart.getProductPrice();
        this.userId = shopCart.getUserId();
        this.quantity = shopCart.getQuantity();
    }

    /**
     * 订单编号
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 商品id
     */
    @TableField(value = "product_id")
    private Integer productId;

    /**
     * 商品单价
     */
    @TableField(value = "product_price")
    private BigDecimal productPrice;

    /**
     * 订单数量
     */
    @TableField(value = "quantity")
    private Integer quantity;

    /**
     * 订单总价
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 订单创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 收货手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 收货地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 订单状态
     */
    @TableField(value = "status")
    private String status;


}