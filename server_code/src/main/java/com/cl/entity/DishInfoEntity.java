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
 * 美食信息
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
@TableName("dish_info")
public class DishInfoEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public DishInfoEntity() {
		
	}
	
	public DishInfoEntity(T t) {
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
	 * 菜品名称
	 */
	@TableField("dish_name")
	private String dishName;
	
	/**
	 * 菜品图片
	 */
	@TableField("dish_image")
	private String dishImage;
	
	/**
	 * 菜品类型
	 */
	@TableField("dish_category")
	private String dishCategory;
	
	/**
	 * 菜品详情
	 */
	@TableField("dish_description")
	private String dishDescription;
	
	/**
	 * 口味
	 */
					
	private String flavor;
	
	/**
	 * 推荐指数
	 */
					
	private String rating;
	
	/**
	 * 最近点击时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	@TableField("click_time")
	private Date clickTime;
	
	/**
	 * 单限
	 */
	@TableField("onelimittimes")
	private Integer purchaseLimit;
	
	/**
	 * 库存
	 */
	@TableField("alllimittimes")
	private Integer stock;
	
	/**
	 * 价格
	 */
					
	private Double price;

	/**
	 * 折扣价
	 */
	private Double discountprice;
	
	/**
	 * 收藏数
	 */
	@TableField("storeup_number")
	private Integer favoritesNumber;
	
	/**
	 * 评论数
	 */
	@TableField("discuss_number")
	private Integer discussNumber;
	
	

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
	 * 设置：菜品名称
	 */
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	/**
	 * 获取：菜品名称
	 */
	public String getDishName() {
		return dishName;
	}
	/**
	 * 设置：菜品图片
	 */
	public void setDishImage(String dishImage) {
		this.dishImage = dishImage;
	}
	/**
	 * 获取：菜品图片
	 */
	public String getDishImage() {
		return dishImage;
	}
	/**
	 * 设置：菜品类型
	 */
	public void setDishCategory(String dishCategory) {
		this.dishCategory = dishCategory;
	}
	/**
	 * 获取：菜品类型
	 */
	public String getDishCategory() {
		return dishCategory;
	}
	/**
	 * 设置：菜品详情
	 */
	public void setDishDescription(String dishDescription) {
		this.dishDescription = dishDescription;
	}
	/**
	 * 获取：菜品详情
	 */
	public String getDishDescription() {
		return dishDescription;
	}
	/**
	 * 设置：口味
	 */
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	/**
	 * 获取：口味
	 */
	public String getFlavor() {
		return flavor;
	}
	/**
	 * 设置：推荐指数
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}
	/**
	 * 获取：推荐指数
	 */
	public String getRating() {
		return rating;
	}
	/**
	 * 设置：最近点击时间
	 */
	public void setClickTime(Date clickTime) {
		this.clickTime = clickTime;
	}
	/**
	 * 获取：最近点击时间
	 */
	public Date getClickTime() {
		return clickTime;
	}
	/**
	 * 设置：单限
	 */
	public void setPurchaseLimit(Integer purchaseLimit) {
		this.purchaseLimit = purchaseLimit;
	}
	/**
	 * 获取：单限
	 */
	public Integer getPurchaseLimit() {
		return purchaseLimit;
	}
	/**
	 * 设置：库存
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	/**
	 * 获取：库存
	 */
	public Integer getStock() {
		return stock;
	}
	/**
	 * 设置：价格
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * 获取：价格
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * 设置：折扣价
	 */
	public void setDiscountprice(Double discountprice) {
		this.discountprice = discountprice;
	}
	/**
	 * 获取：折扣价
	 */
	public Double getDiscountprice() {
		return discountprice;
	}
	/**
	 * 设置：收藏数
	 */
	public void setFavoritesNumber(Integer favoritesNumber) {
		this.favoritesNumber = favoritesNumber;
	}
	/**
	 * 获取：收藏数
	 */
	public Integer getFavoritesNumber() {
		return favoritesNumber;
	}
	/**
	 * 设置：评论数
	 */
	public void setDiscussNumber(Integer discussNumber) {
		this.discussNumber = discussNumber;
	}
	/**
	 * 获取：评论数
	 */
	public Integer getDiscussNumber() {
		return discussNumber;
	}

}
