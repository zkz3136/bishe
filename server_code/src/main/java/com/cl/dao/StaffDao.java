package com.cl.dao;

import com.cl.entity.StaffEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.StaffView;


/**
 * 员工
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface StaffDao extends BaseMapper<StaffEntity> {
	
	List<StaffView> selectListView(@Param("ew") Wrapper<StaffEntity> wrapper);

	List<StaffView> selectListView(Pagination page,@Param("ew") Wrapper<StaffEntity> wrapper);
	
	StaffView selectView(@Param("ew") Wrapper<StaffEntity> wrapper);


}
