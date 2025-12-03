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


import com.cl.dao.DianpuhuodongDao;
import com.cl.entity.DianpuhuodongEntity;
import com.cl.service.DianpuhuodongService;
import com.cl.entity.view.DianpuhuodongView;

@Service("dianpuhuodongService")
public class DianpuhuodongServiceImpl extends ServiceImpl<DianpuhuodongDao, DianpuhuodongEntity> implements DianpuhuodongService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DianpuhuodongEntity> page = this.selectPage(
                new Query<DianpuhuodongEntity>(params).getPage(),
                new EntityWrapper<DianpuhuodongEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DianpuhuodongEntity> wrapper) {
		  Page<DianpuhuodongView> page =new Query<DianpuhuodongView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<DianpuhuodongView> selectListView(Wrapper<DianpuhuodongEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DianpuhuodongView selectView(Wrapper<DianpuhuodongEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
