package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.SupportTicketDao;
import com.cl.entity.SupportTicketEntity;
import com.cl.entity.view.SupportTicketView;
import com.cl.service.SupportTicketService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

@Service("supportTicketService")
public class SupportTicketServiceImpl extends ServiceImpl<SupportTicketDao, SupportTicketEntity> implements SupportTicketService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SupportTicketEntity> page = this.selectPage(
            new Query<SupportTicketEntity>(params).getPage(),
            new EntityWrapper<SupportTicketEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<SupportTicketEntity> wrapper) {
        Page<SupportTicketView> page = new Query<SupportTicketView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<SupportTicketView> selectListView(Wrapper<SupportTicketEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SupportTicketView selectView(Wrapper<SupportTicketEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}

