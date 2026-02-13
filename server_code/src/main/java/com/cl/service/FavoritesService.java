package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.FavoritesEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.FavoritesView;


/**
 * 我的收藏
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface FavoritesService extends IService<FavoritesEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<FavoritesView> selectListView(Wrapper<FavoritesEntity> wrapper);
   	
   	FavoritesView selectView(@Param("ew") Wrapper<FavoritesEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<FavoritesEntity> wrapper);
   	
   
}
