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
 * 车位预约
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("cheweiyuyue")
public class CheweiyuyueEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public CheweiyuyueEntity() {
		
	}
	
	public CheweiyuyueEntity(T t) {
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
					
	private String cheweibianhao;
	
	/**
	 * 车位位置
	 */
					
	private String cheweiweizhi;
	
	/**
	 * 预约时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date yuyueshijian;
	
	/**
	 * 小时价格
	 */
					
	private Double xiaoshijiage;
	
	/**
	 * 入场状态
	 */
					
	private String ruchangzhuangtai;
	
	/**
	 * 账号
	 */
					
	private String zhanghao;
	
	/**
	 * 名称
	 */
					
	private String mingcheng;
	
	/**
	 * 车牌号
	 */
					
	private String chepaihao;
	
	/**
	 * 是否支付
	 */
				
	private String ispay;
	
	/**
	 * 定金
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
	 * 设置：车位编号
	 */
	public void setCheweibianhao(String cheweibianhao) {
		this.cheweibianhao = cheweibianhao;
	}
	/**
	 * 获取：车位编号
	 */
	public String getCheweibianhao() {
		return cheweibianhao;
	}
	/**
	 * 设置：车位位置
	 */
	public void setCheweiweizhi(String cheweiweizhi) {
		this.cheweiweizhi = cheweiweizhi;
	}
	/**
	 * 获取：车位位置
	 */
	public String getCheweiweizhi() {
		return cheweiweizhi;
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
	 * 设置：小时价格
	 */
	public void setXiaoshijiage(Double xiaoshijiage) {
		this.xiaoshijiage = xiaoshijiage;
	}
	/**
	 * 获取：小时价格
	 */
	public Double getXiaoshijiage() {
		return xiaoshijiage;
	}
	/**
	 * 设置：入场状态
	 */
	public void setRuchangzhuangtai(String ruchangzhuangtai) {
		this.ruchangzhuangtai = ruchangzhuangtai;
	}
	/**
	 * 获取：入场状态
	 */
	public String getRuchangzhuangtai() {
		return ruchangzhuangtai;
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
	 * 设置：车牌号
	 */
	public void setChepaihao(String chepaihao) {
		this.chepaihao = chepaihao;
	}
	/**
	 * 获取：车牌号
	 */
	public String getChepaihao() {
		return chepaihao;
	}
	/**
	 * 设置：是否支付
	 */
	public void setIspay(String ispay) {
		this.ispay = ispay;
	}
	/**
	 * 获取：是否支付
	 */
	public String getIspay() {
		return ispay;
	}
	/**
	 * 设置：定金
	 */
	public void setDingjin(Double dingjin) {
		this.dingjin = dingjin;
	}
	/**
	 * 获取：定金
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
