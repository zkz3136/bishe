package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.SupportFaqEntity;
import com.cl.entity.view.SupportFaqView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SupportFaqService extends IService<SupportFaqEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<SupportFaqEntity> wrapper);

    List<SupportFaqView> selectListView(Wrapper<SupportFaqEntity> wrapper);

    SupportFaqView selectView(@Param("ew") Wrapper<SupportFaqEntity> wrapper);
}

