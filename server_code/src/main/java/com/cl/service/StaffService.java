package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.StaffEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.StaffView;


/**
 * 员工
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface StaffService extends IService<StaffEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<StaffView> selectListView(Wrapper<StaffEntity> wrapper);
   	
   	StaffView selectView(@Param("ew") Wrapper<StaffEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<StaffEntity> wrapper);
   	

}
