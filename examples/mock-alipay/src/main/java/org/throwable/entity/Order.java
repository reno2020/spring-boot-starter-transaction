package org.throwable.entity;

import org.throwable.mapper.core.common.annotation.Column;
import org.throwable.mapper.core.common.annotation.Id;
import org.throwable.mapper.core.common.annotation.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/23 23:03
 */
@Table("t_order")
public class Order {

	@Id(value = "id")
	private Long id;
	@Column(value = "order_id")
	private String orderId;
	@Column(value = "create_time")
	private LocalDateTime createTime;
	@Column(value = "amount")
	private Integer amount;
	@Column(value = "order_status")
	private Integer orderStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "OrderLocalTransactionExecutor{" +
				"id=" + id +
				", orderId='" + orderId + '\'' +
				", createTime=" + createTime +
				", amount=" + amount +
				", orderStatus=" + orderStatus +
				'}';
	}

	private static final DateTimeFormatter FORMATTER =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args){
		String sql = "INSERT INTO t_order(id,order_id,create_time,amount,order_status) VALUES";
		for (int i =0;i<100; i ++){
			StringBuilder s = new StringBuilder(sql);
			s.append("(").append((i+1)).append(",").append("'").append(UUID.randomUUID().toString()).append("',")
					.append("'").append(LocalDateTime.now().format(FORMATTER)).append("',")
					.append(100000).append(",").append(0).append(");");
			System.out.println(s.toString());
		}
	}
}
