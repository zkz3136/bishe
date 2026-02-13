package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.SupportTicketMessageEntity;
import com.cl.entity.view.SupportTicketMessageView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SupportTicketMessageService extends IService<SupportTicketMessageEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<SupportTicketMessageEntity> wrapper);

    List<SupportTicketMessageView> selectListView(Wrapper<SupportTicketMessageEntity> wrapper);

    SupportTicketMessageView selectView(@Param("ew") Wrapper<SupportTicketMessageEntity> wrapper);
}

