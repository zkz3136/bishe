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


import com.cl.dao.DishReviewDao;
import com.cl.entity.DishReviewEntity;
import com.cl.service.DishReviewService;
import com.cl.entity.view.DishReviewView;

@Service("dishReviewService")
public class DishReviewServiceImpl extends ServiceImpl<DishReviewDao, DishReviewEntity> implements DishReviewService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DishReviewEntity> page = this.selectPage(
                new Query<DishReviewEntity>(params).getPage(),
                new EntityWrapper<DishReviewEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DishReviewEntity> wrapper) {
		  Page<DishReviewView> page =new Query<DishReviewView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<DishReviewView> selectListView(Wrapper<DishReviewEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DishReviewView selectView(Wrapper<DishReviewEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
