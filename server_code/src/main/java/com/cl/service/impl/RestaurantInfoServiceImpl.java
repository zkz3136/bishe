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


import com.cl.dao.RestaurantInfoDao;
import com.cl.entity.RestaurantInfoEntity;
import com.cl.service.RestaurantInfoService;
import com.cl.entity.view.RestaurantInfoView;

@Service("restaurantInfoService")
public class RestaurantInfoServiceImpl extends ServiceImpl<RestaurantInfoDao, RestaurantInfoEntity> implements RestaurantInfoService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RestaurantInfoEntity> page = this.selectPage(
                new Query<RestaurantInfoEntity>(params).getPage(),
                new EntityWrapper<RestaurantInfoEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<RestaurantInfoEntity> wrapper) {
		  Page<RestaurantInfoView> page =new Query<RestaurantInfoView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<RestaurantInfoView> selectListView(Wrapper<RestaurantInfoEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public RestaurantInfoView selectView(Wrapper<RestaurantInfoEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
