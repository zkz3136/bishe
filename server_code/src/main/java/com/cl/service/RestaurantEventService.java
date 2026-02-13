package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.RestaurantEventEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.RestaurantEventView;


/**
 * 餐厅活动
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface RestaurantEventService extends IService<RestaurantEventEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<RestaurantEventView> selectListView(Wrapper<RestaurantEventEntity> wrapper);
   	
   	RestaurantEventView selectView(@Param("ew") Wrapper<RestaurantEventEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<RestaurantEventEntity> wrapper);
   	
   
}
