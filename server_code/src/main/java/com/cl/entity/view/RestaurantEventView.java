package com.cl.entity.view;

import com.cl.entity.RestaurantEventEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.cl.utils.EncryptUtil;
 

/**
 * 餐厅活动
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@TableName("restaurant_event")
public class RestaurantEventView  extends RestaurantEventEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public RestaurantEventView(){
	}
 
 	public RestaurantEventView(RestaurantEventEntity restaurantEventEntity){
 	try {
			BeanUtils.copyProperties(this, restaurantEventEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}



}
