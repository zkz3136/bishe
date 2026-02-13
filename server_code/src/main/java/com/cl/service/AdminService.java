package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.AdminEntity;
import com.cl.entity.view.AdminView;
import com.cl.utils.PageUtils;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface AdminService extends IService<AdminEntity> {
	PageUtils queryPage(Map<String, Object> params);

	List<AdminView> selectListView(Wrapper<AdminEntity> wrapper);

	AdminView selectView(@Param("ew") Wrapper<AdminEntity> wrapper);

	PageUtils queryPage(Map<String, Object> params, Wrapper<AdminEntity> wrapper);
}

