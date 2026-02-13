package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.RestaurantReservationEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.RestaurantReservationView;


/**
 * 餐厅预约
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface RestaurantReservationService extends IService<RestaurantReservationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<RestaurantReservationView> selectListView(Wrapper<RestaurantReservationEntity> wrapper);
   	
   	RestaurantReservationView selectView(@Param("ew") Wrapper<RestaurantReservationEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<RestaurantReservationEntity> wrapper);
   	
   
}

