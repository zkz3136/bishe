package com.cl.dao;

import com.cl.entity.DishInfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DishInfoView;


/**
 * 美食信息
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
public interface DishInfoDao extends BaseMapper<DishInfoEntity> {
	
	List<DishInfoView> selectListView(@Param("ew") Wrapper<DishInfoEntity> wrapper);

	List<DishInfoView> selectListView(Pagination page,@Param("ew") Wrapper<DishInfoEntity> wrapper);
	
	DishInfoView selectView(@Param("ew") Wrapper<DishInfoEntity> wrapper);


    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<DishInfoEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<DishInfoEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<DishInfoEntity> wrapper);

    int renameDishCategory(@Param("oldCategory") String oldCategory, @Param("newCategory") String newCategory);

}

