package com.cl.dao;

import com.cl.entity.CantingyuyueEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.CantingyuyueView;


/**
 * 餐厅预约
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
public interface CantingyuyueDao extends BaseMapper<CantingyuyueEntity> {
	
	List<CantingyuyueView> selectListView(@Param("ew") Wrapper<CantingyuyueEntity> wrapper);

	List<CantingyuyueView> selectListView(Pagination page,@Param("ew") Wrapper<CantingyuyueEntity> wrapper);
	
	CantingyuyueView selectView(@Param("ew") Wrapper<CantingyuyueEntity> wrapper);


}
