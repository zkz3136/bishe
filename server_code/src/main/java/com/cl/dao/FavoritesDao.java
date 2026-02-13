package com.cl.dao;

import com.cl.entity.FavoritesEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.FavoritesView;


/**
 * 我的收藏
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface FavoritesDao extends BaseMapper<FavoritesEntity> {
	
	List<FavoritesView> selectListView(@Param("ew") Wrapper<FavoritesEntity> wrapper);

	List<FavoritesView> selectListView(Pagination page,@Param("ew") Wrapper<FavoritesEntity> wrapper);
	
	FavoritesView selectView(@Param("ew") Wrapper<FavoritesEntity> wrapper);


}
