package com.cl.dao;

import com.cl.entity.DianpuhuodongEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.DianpuhuodongView;


/**
 * 店铺活动
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface DianpuhuodongDao extends BaseMapper<DianpuhuodongEntity> {
	
	List<DianpuhuodongView> selectListView(@Param("ew") Wrapper<DianpuhuodongEntity> wrapper);

	List<DianpuhuodongView> selectListView(Pagination page,@Param("ew") Wrapper<DianpuhuodongEntity> wrapper);
	
	DianpuhuodongView selectView(@Param("ew") Wrapper<DianpuhuodongEntity> wrapper);


}
