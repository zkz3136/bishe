package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.AdminEntity;
import com.cl.entity.view.AdminView;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminDao extends BaseMapper<AdminEntity> {
	List<AdminView> selectListView(@Param("ew") Wrapper<AdminEntity> wrapper);

	List<AdminView> selectListView(Pagination page, @Param("ew") Wrapper<AdminEntity> wrapper);

	AdminView selectView(@Param("ew") Wrapper<AdminEntity> wrapper);
}

