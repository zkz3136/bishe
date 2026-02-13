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
 * 餐厅活动
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("restaurant_event")
public class RestaurantEventEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public RestaurantEventEntity() {
		
	}
	
	public RestaurantEventEntity(T t) {
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
	 * 活动名称
	 */
					
	private String event_name;
	
	/**
	 * 活动图片
	 */
					
	private String event_image;
	
	/**
	 * 开始时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date start_time;
	
	/**
	 * 活动地点
	 */
					
	private String event_location;
	
	/**
	 * 活动内容
	 */
					
	private String event_content;
	
	/**
	 * 发布时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date publish_time;
	
	/**
	 * 结束时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date end_time;
	

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
	 * 设置：活动名称
	 */
	public void setEventName(String event_name) {
		this.event_name = event_name;
	}
	/**
	 * 获取：活动名称
	 */
	public String getEventName() {
		return event_name;
	}
	/**
	 * 设置：活动图片
	 */
	public void setEventImage(String event_image) {
		this.event_image = event_image;
	}
	/**
	 * 获取：活动图片
	 */
	public String getEventImage() {
		return event_image;
	}
	/**
	 * 设置：开始时间
	 */
	public void setStartTime(Date start_time) {
		this.start_time = start_time;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getStartTime() {
		return start_time;
	}
	/**
	 * 设置：活动地点
	 */
	public void setEventLocation(String event_location) {
		this.event_location = event_location;
	}
	/**
	 * 获取：活动地点
	 */
	public String getEventLocation() {
		return event_location;
	}
	/**
	 * 设置：活动内容
	 */
	public void setEventContent(String event_content) {
		this.event_content = event_content;
	}
	/**
	 * 获取：活动内容
	 */
	public String getEventContent() {
		return event_content;
	}
	/**
	 * 设置：发布时间
	 */
	public void setPublishTime(Date publish_time) {
		this.publish_time = publish_time;
	}
	/**
	 * 获取：发布时间
	 */
	public Date getPublishTime() {
		return publish_time;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(Date end_time) {
		this.end_time = end_time;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getEndTime() {
		return end_time;
	}

}
