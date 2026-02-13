package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.RestaurantInfoEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.RestaurantInfoView;


/**
 * 餐厅信息
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface RestaurantInfoService extends IService<RestaurantInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<RestaurantInfoView> selectListView(Wrapper<RestaurantInfoEntity> wrapper);
   	
   	RestaurantInfoView selectView(@Param("ew") Wrapper<RestaurantInfoEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<RestaurantInfoEntity> wrapper);
   	
   
}

