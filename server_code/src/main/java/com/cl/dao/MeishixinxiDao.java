package com.cl.dao;

import com.cl.entity.MeishixinxiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.MeishixinxiView;


/**
 * 美食信息
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
public interface MeishixinxiDao extends BaseMapper<MeishixinxiEntity> {
	
	List<MeishixinxiView> selectListView(@Param("ew") Wrapper<MeishixinxiEntity> wrapper);

	List<MeishixinxiView> selectListView(Pagination page,@Param("ew") Wrapper<MeishixinxiEntity> wrapper);
	
	MeishixinxiView selectView(@Param("ew") Wrapper<MeishixinxiEntity> wrapper);


    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<MeishixinxiEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<MeishixinxiEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<MeishixinxiEntity> wrapper);



}
