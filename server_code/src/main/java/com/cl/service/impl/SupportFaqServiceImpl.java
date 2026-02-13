package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.SupportFaqDao;
import com.cl.entity.SupportFaqEntity;
import com.cl.entity.view.SupportFaqView;
import com.cl.service.SupportFaqService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

@Service("supportFaqService")
public class SupportFaqServiceImpl extends ServiceImpl<SupportFaqDao, SupportFaqEntity> implements SupportFaqService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SupportFaqEntity> page = this.selectPage(
            new Query<SupportFaqEntity>(params).getPage(),
            new EntityWrapper<SupportFaqEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<SupportFaqEntity> wrapper) {
        Page<SupportFaqView> page = new Query<SupportFaqView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<SupportFaqView> selectListView(Wrapper<SupportFaqEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SupportFaqView selectView(Wrapper<SupportFaqEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}

