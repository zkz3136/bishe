package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.ParkingSpotEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.ParkingSpotView;


/**
 * 车位信息
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface ParkingSpotService extends IService<ParkingSpotEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ParkingSpotView> selectListView(Wrapper<ParkingSpotEntity> wrapper);
   	
   	ParkingSpotView selectView(@Param("ew") Wrapper<ParkingSpotEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<ParkingSpotEntity> wrapper);
   	
   
}

