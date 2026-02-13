package com.cl.dao;

import com.cl.entity.UserEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.UserView;


/**
 * 用户
 * 
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
public interface UserDao extends BaseMapper<UserEntity> {
	
	List<UserView> selectListView(@Param("ew") Wrapper<UserEntity> wrapper);

	List<UserView> selectListView(Pagination page,@Param("ew") Wrapper<UserEntity> wrapper);
	
	UserView selectView(@Param("ew") Wrapper<UserEntity> wrapper);


}
