package com.cl.dao;

import com.cl.entity.RestaurantInfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.RestaurantInfoView;


/**
 * 餐厅信息
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface RestaurantInfoDao extends BaseMapper<RestaurantInfoEntity> {
	
	List<RestaurantInfoView> selectListView(@Param("ew") Wrapper<RestaurantInfoEntity> wrapper);

	List<RestaurantInfoView> selectListView(Pagination page,@Param("ew") Wrapper<RestaurantInfoEntity> wrapper);
	
	RestaurantInfoView selectView(@Param("ew") Wrapper<RestaurantInfoEntity> wrapper);


}
