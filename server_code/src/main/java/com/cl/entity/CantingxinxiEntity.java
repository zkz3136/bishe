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
 * 餐厅信息
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("cantingxinxi")
public class CantingxinxiEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public CantingxinxiEntity() {
		
	}
	
	public CantingxinxiEntity(T t) {
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
	 * 餐厅名称
	 */
					
	private String cantingmingcheng;
	
	/**
	 * 餐桌名称
	 */
					
	private String canzhuomingcheng;
	
	/**
	 * 封面
	 */
					
	private String fengmian;
	
	/**
	 * 餐桌位置
	 */
					
	private String canzhuoweizhi;
	
	/**
	 * 可坐人数
	 */
					
	private Integer kezuorenshu;
	
	/**
	 * 餐桌状态
	 */
					
	private String canzhuozhuangtai;
	
	/**
	 * 商家账号
	 */
					
	private String shangjiazhanghao;
	
	/**
	 * 商家名称
	 */
					
	private String shangjiamingcheng;
	
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
	 * 设置：餐厅名称
	 */
	public void setCantingmingcheng(String cantingmingcheng) {
		this.cantingmingcheng = cantingmingcheng;
	}
	/**
	 * 获取：餐厅名称
	 */
	public String getCantingmingcheng() {
		return cantingmingcheng;
	}
	/**
	 * 设置：餐桌名称
	 */
	public void setCanzhuomingcheng(String canzhuomingcheng) {
		this.canzhuomingcheng = canzhuomingcheng;
	}
	/**
	 * 获取：餐桌名称
	 */
	public String getCanzhuomingcheng() {
		return canzhuomingcheng;
	}
	/**
	 * 设置：封面
	 */
	public void setFengmian(String fengmian) {
		this.fengmian = fengmian;
	}
	/**
	 * 获取：封面
	 */
	public String getFengmian() {
		return fengmian;
	}
	/**
	 * 设置：餐桌位置
	 */
	public void setCanzhuoweizhi(String canzhuoweizhi) {
		this.canzhuoweizhi = canzhuoweizhi;
	}
	/**
	 * 获取：餐桌位置
	 */
	public String getCanzhuoweizhi() {
		return canzhuoweizhi;
	}
	/**
	 * 设置：可坐人数
	 */
	public void setKezuorenshu(Integer kezuorenshu) {
		this.kezuorenshu = kezuorenshu;
	}
	/**
	 * 获取：可坐人数
	 */
	public Integer getKezuorenshu() {
		return kezuorenshu;
	}
	/**
	 * 设置：餐桌状态
	 */
	public void setCanzhuozhuangtai(String canzhuozhuangtai) {
		this.canzhuozhuangtai = canzhuozhuangtai;
	}
	/**
	 * 获取：餐桌状态
	 */
	public String getCanzhuozhuangtai() {
		return canzhuozhuangtai;
	}
	/**
	 * 设置：商家账号
	 */
	public void setShangjiazhanghao(String shangjiazhanghao) {
		this.shangjiazhanghao = shangjiazhanghao;
	}
	/**
	 * 获取：商家账号
	 */
	public String getShangjiazhanghao() {
		return shangjiazhanghao;
	}
	/**
	 * 设置：商家名称
	 */
	public void setShangjiamingcheng(String shangjiamingcheng) {
		this.shangjiamingcheng = shangjiamingcheng;
	}
	/**
	 * 获取：商家名称
	 */
	public String getShangjiamingcheng() {
		return shangjiamingcheng;
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
