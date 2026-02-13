package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 餐厅预约
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("restaurant_reservation")
public class RestaurantReservationEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public RestaurantReservationEntity() {
		
	}
	
	public RestaurantReservationEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 餐桌名称
	 */
	@TableField("seat_name")
	@JsonProperty("seat_name")
	@JsonAlias({"seatName"})
	private String seat_name;
	
	/**
	 * 封面
	 */
	@TableField("cover_image")
	@JsonProperty("cover_image")
	@JsonAlias({"coverImage"})
	private String cover_image;
	
	/**
	 * 餐桌位置
	 */
	@TableField("table_location")
	@JsonProperty("table_location")
	@JsonAlias({"tableLocation"})
	private String table_location;
	
	/**
	 * 可坐人数
	 */
					
	private Integer capacity;
	
	
	/**
	 * 收藏数
	 */
	@TableField("storeup_number")
	private Integer storeupNumber;
	
	/**
	 * 账号
	 */
	@JsonProperty("login_name")
	@JsonAlias({"account", "username", "loginName", "login_name"})
	@TableField("account")
	private String login_name;
	
	/**
	 * 名称
	 */
					
	private String name;
	
	/**
	 * 预约时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	@JsonProperty("reservation_time")
	@JsonAlias({"reservationTime"})
	@TableField("reservation_time")
	private Date reservation_time;
	
	/**
	 * 定金金额
	 */
	private Double deposit;
	
	/**
	 * 支付状态
	 */
	@TableField("payment_status")
	@JsonProperty("payment_status")
	@JsonAlias({"paymentStatus"})
	private String payment_status;
	
	/**
	 * 核销状态
	 */
	@TableField("verification_status")
	@JsonProperty("verification_status")
	@JsonAlias({"verificationStatus"})
	private String verification_status;
	

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：餐桌名称
	 */
	public void setSeatName(String seat_name) {
		this.seat_name = seat_name;
	}
	/**
	 * 获取：餐桌名称
	 */
	public String getSeatName() {
		return seat_name;
	}
	/**
	 * 设置：封面
	 */
	public void setCoverImage(String cover_image) {
		this.cover_image = cover_image;
	}
	/**
	 * 获取：封面
	 */
	public String getCoverImage() {
		return cover_image;
	}
	/**
	 * 设置：餐桌位置
	 */
	public void setTableLocation(String table_location) {
		this.table_location = table_location;
	}
	/**
	 * 获取：餐桌位置
	 */
	public String getTableLocation() {
		return table_location;
	}
	/**
	 * 设置：可坐人数
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	/**
	 * 获取：可坐人数
	 */
	public Integer getCapacity() {
		return capacity;
	}
	/**
	 * 设置：收藏数
	 */
	public void setFavoritesNumber(Integer storeupNumber) {
		this.storeupNumber = storeupNumber;
	}
	/**
	 * 获取：收藏数
	 */
	public Integer getFavoritesNumber() {
		return storeupNumber;
	}
	/**
	 * 设置：账号
	 */
	public void setLoginName(String login_name) {
		this.login_name = login_name;
	}
	/**
	 * 获取：账号
	 */
	public String getLoginName() {
		return login_name;
	}
	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：预约时间
	 */
	public void setReservationTime(Date reservation_time) {
		this.reservation_time = reservation_time;
	}
	/**
	 * 获取：预约时间
	 */
	public Date getReservationTime() {
		return reservation_time;
	}
	/**
	 * 设置：定金金额
	 */
	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	/**
	 * 获取：定金金额
	 */
	public Double getDeposit() {
		return deposit;
	}
	/**
	 * 设置：支付状态
	 */
	public void setPaymentStatus(String payment_status) {
		this.payment_status = payment_status;
	}
	/**
	 * 获取：支付状态
	 */
	public String getPaymentStatus() {
		return payment_status;
	}
	/**
	 * 设置：核销状态
	 */
	public void setVerificationStatus(String verification_status) {
		this.verification_status = verification_status;
	}
	/**
	 * 获取：核销状态
	 */
	public String getVerificationStatus() {
		return verification_status;
	}

}
