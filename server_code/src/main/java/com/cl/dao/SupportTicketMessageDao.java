package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.SupportTicketMessageEntity;
import com.cl.entity.view.SupportTicketMessageView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupportTicketMessageDao extends BaseMapper<SupportTicketMessageEntity> {
    List<SupportTicketMessageView> selectListView(@Param("ew") Wrapper<SupportTicketMessageEntity> wrapper);

    List<SupportTicketMessageView> selectListView(Pagination page, @Param("ew") Wrapper<SupportTicketMessageEntity> wrapper);

    SupportTicketMessageView selectView(@Param("ew") Wrapper<SupportTicketMessageEntity> wrapper);
}

