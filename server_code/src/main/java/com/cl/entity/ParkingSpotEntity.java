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
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 车位信息
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("parking_info")
public class ParkingSpotEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public ParkingSpotEntity() {
		
	}
	
	public ParkingSpotEntity(T t) {
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
	 * 车位编号
	 */
	@TableField("spot_number")
	private String spotNumber;
	
	/**
	 * 车位图片
	 */
	@TableField("spot_image")
	private String spotImage;
	
	/**
	 * 区域
	 */
					
	private String area;
	
	/**
	 * 车位位置
	 */
	@TableField("spot_location")
	private String spotLocation;
	
	/**
	 * 小时价格
	 */
	@TableField("hourly_price")
	private Double hourlyPrice;
	
	/**
	 * 车位状态
	 */
	@TableField("spot_status")
	private String spotStatus;

	/**
	 * 车牌号
	 */
	@TableField("plate_number")
	private String plateNumber;

	/**
	 * 入场时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	@TableField("entry_time")
	private Date entryTime;

	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public Date getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	@TableField("addtime")
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
	 * 设置：车位编号
	 */
	public void setSpotNumber(String spotNumber) {
		this.spotNumber = spotNumber;
	}
	/**
	 * 获取：车位编号
	 */
	public String getSpotNumber() {
		return spotNumber;
	}
	/**
	 * 设置：车位图片
	 */
	public void setSpotImage(String spotImage) {
		this.spotImage = spotImage;
	}
	/**
	 * 获取：车位图片
	 */
	public String getSpotImage() {
		return spotImage;
	}
	/**
	 * 设置：区域
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * 获取：区域
	 */
	public String getArea() {
		return area;
	}
	/**
	 * 设置：车位位置
	 */
	public void setSpotLocation(String spotLocation) {
		this.spotLocation = spotLocation;
	}
	/**
	 * 获取：车位位置
	 */
	public String getSpotLocation() {
		return spotLocation;
	}
	/**
	 * 设置：小时价格
	 */
	public void setHourlyPrice(Double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
	}
	/**
	 * 获取：小时价格
	 */
	public Double getHourlyPrice() {
		return hourlyPrice;
	}
	/**
	 * 设置：车位状态
	 */
	public void setSpotStatus(String spotStatus) {
		this.spotStatus = spotStatus;
	}
	/**
	 * 获取：车位状态
	 */
	public String getSpotStatus() {
		return spotStatus;
	}

}
