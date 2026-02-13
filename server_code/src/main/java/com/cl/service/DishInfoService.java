package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.DishInfoEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DishInfoView;


/**
 * 美食信息
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
public interface DishInfoService extends IService<DishInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DishInfoView> selectListView(Wrapper<DishInfoEntity> wrapper);
   	
   	DishInfoView selectView(@Param("ew") Wrapper<DishInfoEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DishInfoEntity> wrapper);
   	
   
    List<Map<String, Object>> selectValue(Map<String, Object> params,Wrapper<DishInfoEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,Wrapper<DishInfoEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,Wrapper<DishInfoEntity> wrapper);

    int renameDishCategory(String oldCategory, String newCategory);


}
