package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.AdminDao;
import com.cl.entity.AdminEntity;
import com.cl.entity.view.AdminView;
import com.cl.service.AdminService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service("adminService")
public class AdminServiceImpl extends ServiceImpl<AdminDao, AdminEntity> implements AdminService {
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<AdminEntity> page = this.selectPage(new Query<AdminEntity>(params).getPage(), new EntityWrapper<AdminEntity>());
		return new PageUtils(page);
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<AdminEntity> wrapper) {
		Page<AdminView> page = new Query<AdminView>(params).getPage();
		page.setRecords(baseMapper.selectListView(page, wrapper));
		PageUtils pageUtil = new PageUtils(page);
		return pageUtil;
	}

	@Override
	public List<AdminView> selectListView(Wrapper<AdminEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public AdminView selectView(Wrapper<AdminEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
}

