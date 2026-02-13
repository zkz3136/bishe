package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.SupportTicketEntity;
import com.cl.entity.view.SupportTicketView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SupportTicketService extends IService<SupportTicketEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<SupportTicketEntity> wrapper);

    List<SupportTicketView> selectListView(Wrapper<SupportTicketEntity> wrapper);

    SupportTicketView selectView(@Param("ew") Wrapper<SupportTicketEntity> wrapper);
}

