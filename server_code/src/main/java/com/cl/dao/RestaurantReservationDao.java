package com.cl.dao;

import com.cl.entity.RestaurantReservationEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.RestaurantReservationView;


/**
 * 餐厅预约
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface RestaurantReservationDao extends BaseMapper<RestaurantReservationEntity> {
	
	List<RestaurantReservationView> selectListView(@Param("ew") Wrapper<RestaurantReservationEntity> wrapper);

	List<RestaurantReservationView> selectListView(Pagination page,@Param("ew") Wrapper<RestaurantReservationEntity> wrapper);
	
	RestaurantReservationView selectView(@Param("ew") Wrapper<RestaurantReservationEntity> wrapper);


}
