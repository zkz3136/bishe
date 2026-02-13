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


import com.cl.dao.RestaurantReservationDao;
import com.cl.entity.RestaurantReservationEntity;
import com.cl.service.RestaurantReservationService;
import com.cl.entity.view.RestaurantReservationView;

@Service("restaurantReservationService")
public class RestaurantReservationServiceImpl extends ServiceImpl<RestaurantReservationDao, RestaurantReservationEntity> implements RestaurantReservationService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RestaurantReservationEntity> page = this.selectPage(
                new Query<RestaurantReservationEntity>(params).getPage(),
                new EntityWrapper<RestaurantReservationEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<RestaurantReservationEntity> wrapper) {
		  Page<RestaurantReservationView> page =new Query<RestaurantReservationView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<RestaurantReservationView> selectListView(Wrapper<RestaurantReservationEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public RestaurantReservationView selectView(Wrapper<RestaurantReservationEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
