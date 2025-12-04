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
@TableName("meishixinxi")
public class MeishixinxiEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public MeishixinxiEntity() {
		
	}
	
	public MeishixinxiEntity(T t) {
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
					
	private String caipinmingcheng;
	
	/**
	 * 菜品图片
	 */
					
	private String caipintupian;
	
	/**
	 * 菜品类型
	 */
					
	private String caipinleixing;
	
	/**
	 * 菜品详情
	 */
					
	private String caipinxiangqing;
	
	/**
	 * 口味
	 */
					
	private String kouwei;
	
	/**
	 * 推荐指数
	 */
					
	private String tuijianzhishu;
	
	/**
	 * 最近点击时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date clicktime;
	
	/**
	 * 单限
	 */
					
	private Integer onelimittimes;
	
	/**
	 * 库存
	 */
					
	private Integer alllimittimes;
	
	/**
	 * 价格
	 */
					
	private Double price;
	
	/**
	 * 收藏数
	 */
					
	private Integer storeupNumber;
	
	/**
	 * 评论数
	 */
					
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
	public void setCaipinmingcheng(String caipinmingcheng) {
		this.caipinmingcheng = caipinmingcheng;
	}
	/**
	 * 获取：菜品名称
	 */
	public String getCaipinmingcheng() {
		return caipinmingcheng;
	}
	/**
	 * 设置：菜品图片
	 */
	public void setCaipintupian(String caipintupian) {
		this.caipintupian = caipintupian;
	}
	/**
	 * 获取：菜品图片
	 */
	public String getCaipintupian() {
		return caipintupian;
	}
	/**
	 * 设置：菜品类型
	 */
	public void setCaipinleixing(String caipinleixing) {
		this.caipinleixing = caipinleixing;
	}
	/**
	 * 获取：菜品类型
	 */
	public String getCaipinleixing() {
		return caipinleixing;
	}
	/**
	 * 设置：菜品详情
	 */
	public void setCaipinxiangqing(String caipinxiangqing) {
		this.caipinxiangqing = caipinxiangqing;
	}
	/**
	 * 获取：菜品详情
	 */
	public String getCaipinxiangqing() {
		return caipinxiangqing;
	}
	/**
	 * 设置：口味
	 */
	public void setKouwei(String kouwei) {
		this.kouwei = kouwei;
	}
	/**
	 * 获取：口味
	 */
	public String getKouwei() {
		return kouwei;
	}
	/**
	 * 设置：推荐指数
	 */
	public void setTuijianzhishu(String tuijianzhishu) {
		this.tuijianzhishu = tuijianzhishu;
	}
	/**
	 * 获取：推荐指数
	 */
	public String getTuijianzhishu() {
		return tuijianzhishu;
	}
	/**
	 * 设置：最近点击时间
	 */
	public void setClicktime(Date clicktime) {
		this.clicktime = clicktime;
	}
	/**
	 * 获取：最近点击时间
	 */
	public Date getClicktime() {
		return clicktime;
	}
	/**
	 * 设置：单限
	 */
	public void setOnelimittimes(Integer onelimittimes) {
		this.onelimittimes = onelimittimes;
	}
	/**
	 * 获取：单限
	 */
	public Integer getOnelimittimes() {
		return onelimittimes;
	}
	/**
	 * 设置：库存
	 */
	public void setAlllimittimes(Integer alllimittimes) {
		this.alllimittimes = alllimittimes;
	}
	/**
	 * 获取：库存
	 */
	public Integer getAlllimittimes() {
		return alllimittimes;
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
	 * 设置：收藏数
	 */
	public void setStoreupNumber(Integer storeupNumber) {
		this.storeupNumber = storeupNumber;
	}
	/**
	 * 获取：收藏数
	 */
	public Integer getStoreupNumber() {
		return storeupNumber;
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
