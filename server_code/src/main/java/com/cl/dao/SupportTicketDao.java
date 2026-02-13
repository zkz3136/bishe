package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.SupportTicketEntity;
import com.cl.entity.view.SupportTicketView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupportTicketDao extends BaseMapper<SupportTicketEntity> {
    List<SupportTicketView> selectListView(@Param("ew") Wrapper<SupportTicketEntity> wrapper);

    List<SupportTicketView> selectListView(Pagination page, @Param("ew") Wrapper<SupportTicketEntity> wrapper);

    SupportTicketView selectView(@Param("ew") Wrapper<SupportTicketEntity> wrapper);
}

