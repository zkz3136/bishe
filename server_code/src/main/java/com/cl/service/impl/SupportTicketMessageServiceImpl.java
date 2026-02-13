package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.SupportTicketMessageDao;
import com.cl.entity.SupportTicketMessageEntity;
import com.cl.entity.view.SupportTicketMessageView;
import com.cl.service.SupportTicketMessageService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

@Service("supportTicketMessageService")
public class SupportTicketMessageServiceImpl extends ServiceImpl<SupportTicketMessageDao, SupportTicketMessageEntity> implements SupportTicketMessageService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SupportTicketMessageEntity> page = this.selectPage(
            new Query<SupportTicketMessageEntity>(params).getPage(),
            new EntityWrapper<SupportTicketMessageEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<SupportTicketMessageEntity> wrapper) {
        Page<SupportTicketMessageView> page = new Query<SupportTicketMessageView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<SupportTicketMessageView> selectListView(Wrapper<SupportTicketMessageEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SupportTicketMessageView selectView(Wrapper<SupportTicketMessageEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}

