package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
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
 * 用户
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
@TableName("users")
public class UserEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public UserEntity() {
		
	}
	
	public UserEntity(T t) {
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
	@TableId
	private Long id;
	/**
	 * 账号
	 */
	@TableField("login_name")
	@JsonAlias({"username", "account", "loginName"})
	private String loginName;
	
	/**
	 * 头像
	 */
					
	private String avatar;
	
	/**
	 * 密码
	 */
					
	private String password;

	private String role;
	
	/**
	 * 名称
	 */
					
	private String name;
	
	/**
	 * 性别
	 */
					
	private String gender;
	
	/**
	 * 手机号码
	 */
					
	private String mobile;
	
	/**
	 * 密保问题
	 */
	@TableField("security_question")
	private String securityQuestion;
	
	/**
	 * 密保答案
	 */
	@TableField("security_answer")
	private String securityAnswer;
	
	/**
	 * 最大密码输错次数
	 */
	@TableField("max_password_wrong")
	@JsonAlias({"max_password_wrong"})
	private Integer maxPasswordWrong;
	
	/**
	 * 用户锁定状态
	 */
	@TableField("is_locked")
	@JsonAlias({"is_locked"})
	private Integer isLocked;
	
	/**
	 * 余额
	 */
	@TableField("money")
	private Double balance;

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
	 * 设置：账号
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * 获取：账号
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * 设置：头像
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	/**
	 * 获取：头像
	 */
	public String getAvatar() {
		return avatar;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
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
	 * 设置：性别
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * 获取：性别
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * 设置：手机号码
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机号码
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：密保问题
	 */
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	/**
	 * 获取：密保问题
	 */
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	/**
	 * 设置：密保答案
	 */
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	/**
	 * 获取：密保答案
	 */
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	/**
	 * 设置：最大密码输错次数
	 */
	public void setMaxPasswordWrong(Integer maxPasswordWrong) {
		this.maxPasswordWrong = maxPasswordWrong;
	}
	/**
	 * 获取：最大密码输错次数
	 */
	public Integer getMaxPasswordWrong() {
		return maxPasswordWrong;
	}
	/**
	 * 设置：用户锁定状态
	 */
	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}
	/**
	 * 获取：用户锁定状态
	 */
	public Integer getIsLocked() {
		return isLocked;
	}
	/**
	 * 设置：余额
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	/**
	 * 获取：余额
	 */
	public Double getBalance() {
		return balance;
	}

}
