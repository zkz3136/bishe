package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.DishReviewEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DishReviewView;


/**
 * 美食信息评价表
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:59
 */
public interface DishReviewService extends IService<DishReviewEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DishReviewView> selectListView(Wrapper<DishReviewEntity> wrapper);
   	
   	DishReviewView selectView(@Param("ew") Wrapper<DishReviewEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DishReviewEntity> wrapper);
   	
   
}
