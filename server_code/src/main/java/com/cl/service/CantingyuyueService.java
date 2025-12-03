package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.CantingyuyueEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.CantingyuyueView;


/**
 * 餐厅预约
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface CantingyuyueService extends IService<CantingyuyueEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<CantingyuyueView> selectListView(Wrapper<CantingyuyueEntity> wrapper);
   	
   	CantingyuyueView selectView(@Param("ew") Wrapper<CantingyuyueEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<CantingyuyueEntity> wrapper);
   	
   
}

