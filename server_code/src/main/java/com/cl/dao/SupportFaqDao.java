package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.SupportFaqEntity;
import com.cl.entity.view.SupportFaqView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupportFaqDao extends BaseMapper<SupportFaqEntity> {
    List<SupportFaqView> selectListView(@Param("ew") Wrapper<SupportFaqEntity> wrapper);

    List<SupportFaqView> selectListView(Pagination page, @Param("ew") Wrapper<SupportFaqEntity> wrapper);

    SupportFaqView selectView(@Param("ew") Wrapper<SupportFaqEntity> wrapper);
}

