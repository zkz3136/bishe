package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 餐厅信息
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("restaurant_info")
public class RestaurantInfoEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public RestaurantInfoEntity() {
		
	}
	
	public RestaurantInfoEntity(T t) {
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
	private String seatName;
	
	/**
	 * 封面
	 */
	@TableField("cover_image")
	@JsonProperty("cover_image")
	@JsonAlias({"coverImage"})
	private String coverImage;
	
	/**
	 * 餐桌位置
	 */
	@TableField("table_location")
	@JsonProperty("table_location")
	@JsonAlias({"tableLocation"})
	private String tableLocation;
	
	/**
	 * 可坐人数
	 */
					
	private Integer capacity;
	
	/**
	 * 定金金额
	 */
	@TableField("deposit")
	@JsonProperty("deposit")
	@JsonAlias({"deposit"})
	private Double deposit;
	
	/**
	 * 餐桌状态
	 */
	@TableField("table_status")
	@JsonProperty("table_status")
	@JsonAlias({"tableStatus"})
	private String tableStatus;
	
	
	/**
	 * 收藏数
	 */
	@TableField("storeup_number")
	@JsonProperty("storeup_number")
	@JsonAlias({"storeupNumber"})
	private Integer storeupNumber;
	
	/**
	 * 创建时间
	 */
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
	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	/**
	 * 获取：餐桌名称
	 */
	public String getSeatName() {
		return seatName;
	}
	/**
	 * 设置：封面
	 */
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	/**
	 * 获取：封面
	 */
	public String getCoverImage() {
		return coverImage;
	}
	/**
	 * 设置：餐桌位置
	 */
	public void setTableLocation(String tableLocation) {
		this.tableLocation = tableLocation;
	}
	/**
	 * 获取：餐桌位置
	 */
	public String getTableLocation() {
		return tableLocation;
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
	 * 设置：餐桌状态
	 */
	public void setTableStatus(String tableStatus) {
		this.tableStatus = tableStatus;
	}
	/**
	 * 获取：餐桌状态
	 */
	public String getTableStatus() {
		return tableStatus;
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
	public void setStoreupNumber(Integer storeupNumber) {
		this.storeupNumber = storeupNumber;
	}
	public Integer getStoreupNumber() {
		return storeupNumber;
	}

}
