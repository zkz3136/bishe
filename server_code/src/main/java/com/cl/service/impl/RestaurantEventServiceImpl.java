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


import com.cl.dao.RestaurantEventDao;
import com.cl.entity.RestaurantEventEntity;
import com.cl.service.RestaurantEventService;
import com.cl.entity.view.RestaurantEventView;

@Service("restaurantEventService")
public class RestaurantEventServiceImpl extends ServiceImpl<RestaurantEventDao, RestaurantEventEntity> implements RestaurantEventService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RestaurantEventEntity> page = this.selectPage(
                new Query<RestaurantEventEntity>(params).getPage(),
                new EntityWrapper<RestaurantEventEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<RestaurantEventEntity> wrapper) {
		  Page<RestaurantEventView> page =new Query<RestaurantEventView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<RestaurantEventView> selectListView(Wrapper<RestaurantEventEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public RestaurantEventView selectView(Wrapper<RestaurantEventEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
