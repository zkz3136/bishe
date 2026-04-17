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


import com.cl.dao.DishInfoDao;
import com.cl.entity.DishInfoEntity;
import com.cl.service.DishInfoService;
import com.cl.entity.view.DishInfoView;

@Service("dishInfoService")
public class DishInfoServiceImpl extends ServiceImpl<DishInfoDao, DishInfoEntity> implements DishInfoService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DishInfoEntity> page = this.selectPage(
                new Query<DishInfoEntity>(params).getPage(),
                new EntityWrapper<DishInfoEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DishInfoEntity> wrapper) {
		  Page<DishInfoView> page =new Query<DishInfoView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<DishInfoView> selectListView(Wrapper<DishInfoEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DishInfoView selectView(Wrapper<DishInfoEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<DishInfoEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<DishInfoEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<DishInfoEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }

	@Override
	public int renameDishCategory(String oldCategory, String newCategory) {
		return baseMapper.renameDishCategory(oldCategory, newCategory);
	}

	@Override
	public int renameFlavor(String oldFlavor, String newFlavor) {
		return baseMapper.renameFlavor(oldFlavor, newFlavor);
	}

	@Override
	public int deleteFlavor(String oldFlavor) {
		return baseMapper.deleteFlavor(oldFlavor);
	}




}
