package com.example.SpringBatchTutorial.core.domain.accounts;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.SpringBatchTutorial.core.domain.orders.Orders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor //default생성자도 생성
@ToString
@Entity
public class Accounts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String orderItem;
	private Integer price;
	private Date orderDate;
	private Date accountDate;//정산날짜
	
	public Accounts(Orders orders) {
		this.id = orders.getId();
		this.orderItem = orders.getOrderItem();
		this.price = orders.getPrice();
		this.orderDate = orders.getOrderDate();
		this.accountDate = new Date();
	}
}
