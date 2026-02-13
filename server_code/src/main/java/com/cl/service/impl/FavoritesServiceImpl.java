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


import com.cl.dao.FavoritesDao;
import com.cl.entity.FavoritesEntity;
import com.cl.service.FavoritesService;
import com.cl.entity.view.FavoritesView;

@Service("storeupService")
public class FavoritesServiceImpl extends ServiceImpl<FavoritesDao, FavoritesEntity> implements FavoritesService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<FavoritesEntity> page = this.selectPage(
                new Query<FavoritesEntity>(params).getPage(),
                new EntityWrapper<FavoritesEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<FavoritesEntity> wrapper) {
		  Page<FavoritesView> page =new Query<FavoritesView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<FavoritesView> selectListView(Wrapper<FavoritesEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public FavoritesView selectView(Wrapper<FavoritesEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
