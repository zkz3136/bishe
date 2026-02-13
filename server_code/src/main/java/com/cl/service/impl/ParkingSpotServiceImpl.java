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


import com.cl.dao.ParkingSpotDao;
import com.cl.entity.ParkingSpotEntity;
import com.cl.service.ParkingSpotService;
import com.cl.entity.view.ParkingSpotView;

@Service("cheweixinxiService")
public class ParkingSpotServiceImpl extends ServiceImpl<ParkingSpotDao, ParkingSpotEntity> implements ParkingSpotService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ParkingSpotEntity> page = this.selectPage(
                new Query<ParkingSpotEntity>(params).getPage(),
                new EntityWrapper<ParkingSpotEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<ParkingSpotEntity> wrapper) {
		  Page<ParkingSpotView> page =new Query<ParkingSpotView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<ParkingSpotView> selectListView(Wrapper<ParkingSpotEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ParkingSpotView selectView(Wrapper<ParkingSpotEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
