package com.cl.dao;

import com.cl.entity.RestaurantEventEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.RestaurantEventView;


/**
 * 餐厅活动
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface RestaurantEventDao extends BaseMapper<RestaurantEventEntity> {
	
	List<RestaurantEventView> selectListView(@Param("ew") Wrapper<RestaurantEventEntity> wrapper);

	List<RestaurantEventView> selectListView(Pagination page,@Param("ew") Wrapper<RestaurantEventEntity> wrapper);
	
	RestaurantEventView selectView(@Param("ew") Wrapper<RestaurantEventEntity> wrapper);


}
