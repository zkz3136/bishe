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
 * 餐厅预约
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("cantingyuyue")
public class CantingyuyueEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public CantingyuyueEntity() {
		
	}
	
	public CantingyuyueEntity(T t) {
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
	 * 收藏数
	 */
					
	private Integer storeupNumber;
	
	/**
	 * 账号
	 */
					
	private String zhanghao;
	
	/**
	 * 名称
	 */
					
	private String mingcheng;
	
	/**
	 * 预约时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date yuyueshijian;
	
	/**
	 * 是否审核
	 */
					
	private String sfsh;
	
	/**
	 * 回复内容
	 */
				
	private String shhf;
	
	/**
	 * 定金金额
	 */
	private Double dingjin;
	
	/**
	 * 支付状态
	 */
	private String zhifuzhuangtai;
	
	/**
	 * 核销状态
	 */
	private String hexiaozhuangtai;
	

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
	 * 设置：账号
	 */
	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	/**
	 * 获取：账号
	 */
	public String getZhanghao() {
		return zhanghao;
	}
	/**
	 * 设置：名称
	 */
	public void setMingcheng(String mingcheng) {
		this.mingcheng = mingcheng;
	}
	/**
	 * 获取：名称
	 */
	public String getMingcheng() {
		return mingcheng;
	}
	/**
	 * 设置：预约时间
	 */
	public void setYuyueshijian(Date yuyueshijian) {
		this.yuyueshijian = yuyueshijian;
	}
	/**
	 * 获取：预约时间
	 */
	public Date getYuyueshijian() {
		return yuyueshijian;
	}
	/**
	 * 设置：是否审核
	 */
	public void setSfsh(String sfsh) {
		this.sfsh = sfsh;
	}
	/**
	 * 获取：是否审核
	 */
	public String getSfsh() {
		return sfsh;
	}
	/**
	 * 设置：回复内容
	 */
	public void setShhf(String shhf) {
		this.shhf = shhf;
	}
	/**
	 * 获取：回复内容
	 */
	public String getShhf() {
		return shhf;
	}
	/**
	 * 设置：定金金额
	 */
	public void setDingjin(Double dingjin) {
		this.dingjin = dingjin;
	}
	/**
	 * 获取：定金金额
	 */
	public Double getDingjin() {
		return dingjin;
	}
	/**
	 * 设置：支付状态
	 */
	public void setZhifuzhuangtai(String zhifuzhuangtai) {
		this.zhifuzhuangtai = zhifuzhuangtai;
	}
	/**
	 * 获取：支付状态
	 */
	public String getZhifuzhuangtai() {
		return zhifuzhuangtai;
	}
	/**
	 * 设置：核销状态
	 */
	public void setHexiaozhuangtai(String hexiaozhuangtai) {
		this.hexiaozhuangtai = hexiaozhuangtai;
	}
	/**
	 * 获取：核销状态
	 */
	public String getHexiaozhuangtai() {
		return hexiaozhuangtai;
	}

}
