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


import com.cl.dao.CantingyuyueDao;
import com.cl.entity.CantingyuyueEntity;
import com.cl.service.CantingyuyueService;
import com.cl.entity.view.CantingyuyueView;

@Service("cantingyuyueService")
public class CantingyuyueServiceImpl extends ServiceImpl<CantingyuyueDao, CantingyuyueEntity> implements CantingyuyueService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CantingyuyueEntity> page = this.selectPage(
                new Query<CantingyuyueEntity>(params).getPage(),
                new EntityWrapper<CantingyuyueEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<CantingyuyueEntity> wrapper) {
		  Page<CantingyuyueView> page =new Query<CantingyuyueView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<CantingyuyueView> selectListView(Wrapper<CantingyuyueEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public CantingyuyueView selectView(Wrapper<CantingyuyueEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
