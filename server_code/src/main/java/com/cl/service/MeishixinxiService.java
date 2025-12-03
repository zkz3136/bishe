package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.MeishixinxiEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.MeishixinxiView;


/**
 * 美食信息
 *
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
public interface MeishixinxiService extends IService<MeishixinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<MeishixinxiView> selectListView(Wrapper<MeishixinxiEntity> wrapper);
   	
   	MeishixinxiView selectView(@Param("ew") Wrapper<MeishixinxiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<MeishixinxiEntity> wrapper);
   	
   
    List<Map<String, Object>> selectValue(Map<String, Object> params,Wrapper<MeishixinxiEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,Wrapper<MeishixinxiEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,Wrapper<MeishixinxiEntity> wrapper);



}

