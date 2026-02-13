package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.StaffDao;
import com.cl.entity.StaffEntity;
import com.cl.service.StaffService;
import com.cl.entity.view.StaffView;

@Service("staffService")
public class StaffServiceImpl extends ServiceImpl<StaffDao, StaffEntity> implements StaffService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<StaffEntity> page = this.selectPage(
                new Query<StaffEntity>(params).getPage(),
                new EntityWrapper<StaffEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<StaffEntity> wrapper) {
		  Page<StaffView> page =new Query<StaffView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<StaffView> selectListView(Wrapper<StaffEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public StaffView selectView(Wrapper<StaffEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	


}
