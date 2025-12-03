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


import com.cl.dao.SystemNoticeDao;
import com.cl.entity.SystemNoticeEntity;
import com.cl.service.SystemNoticeService;
import com.cl.entity.view.SystemNoticeView;

@Service("systemNoticeService")
public class SystemNoticeServiceImpl extends ServiceImpl<SystemNoticeDao, SystemNoticeEntity> implements SystemNoticeService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SystemNoticeEntity> page = this.selectPage(
                new Query<SystemNoticeEntity>(params).getPage(),
                new EntityWrapper<SystemNoticeEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<SystemNoticeEntity> wrapper) {
		  Page<SystemNoticeView> page =new Query<SystemNoticeView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<SystemNoticeView> selectListView(Wrapper<SystemNoticeEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public SystemNoticeView selectView(Wrapper<SystemNoticeEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
