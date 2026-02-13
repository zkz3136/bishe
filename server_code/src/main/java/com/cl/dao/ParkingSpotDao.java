package com.cl.dao;

import com.cl.entity.ParkingSpotEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.ParkingSpotView;


/**
 * 车位信息
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface ParkingSpotDao extends BaseMapper<ParkingSpotEntity> {
	
	List<ParkingSpotView> selectListView(@Param("ew") Wrapper<ParkingSpotEntity> wrapper);

	List<ParkingSpotView> selectListView(Pagination page,@Param("ew") Wrapper<ParkingSpotEntity> wrapper);
	
	ParkingSpotView selectView(@Param("ew") Wrapper<ParkingSpotEntity> wrapper);


}
