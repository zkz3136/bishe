package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;

import com.cl.entity.DianpuhuodongEntity;
import com.cl.entity.view.DianpuhuodongView;

import com.cl.service.DianpuhuodongService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 店铺活动
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/dianpuhuodong")
public class DianpuhuodongController {
    @Autowired
    private DianpuhuodongService dianpuhuodongService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DianpuhuodongEntity dianpuhuodong,
                                                                                                                    HttpServletRequest request){
                                    EntityWrapper<DianpuhuodongEntity> ew = new EntityWrapper<DianpuhuodongEntity>();
                                                                                                                                                                                
        
        
        PageUtils page = dianpuhuodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dianpuhuodong), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DianpuhuodongEntity dianpuhuodong,
		HttpServletRequest request){
        EntityWrapper<DianpuhuodongEntity> ew = new EntityWrapper<DianpuhuodongEntity>();

		PageUtils page = dianpuhuodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dianpuhuodong), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DianpuhuodongEntity dianpuhuodong){
       	EntityWrapper<DianpuhuodongEntity> ew = new EntityWrapper<DianpuhuodongEntity>();
      	ew.allEq(MPUtil.allEQMapPre( dianpuhuodong, "dianpuhuodong")); 
        return R.ok().put("data", dianpuhuodongService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DianpuhuodongEntity dianpuhuodong){
        EntityWrapper< DianpuhuodongEntity> ew = new EntityWrapper< DianpuhuodongEntity>();
 		ew.allEq(MPUtil.allEQMapPre( dianpuhuodong, "dianpuhuodong")); 
		DianpuhuodongView dianpuhuodongView =  dianpuhuodongService.selectView(ew);
		return R.ok("查询店铺活动成功").put("data", dianpuhuodongView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DianpuhuodongEntity dianpuhuodong = dianpuhuodongService.selectById(id);
		dianpuhuodong = dianpuhuodongService.selectView(new EntityWrapper<DianpuhuodongEntity>().eq("id", id));
        return R.ok().put("data", dianpuhuodong);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DianpuhuodongEntity dianpuhuodong = dianpuhuodongService.selectById(id);
		dianpuhuodong = dianpuhuodongService.selectView(new EntityWrapper<DianpuhuodongEntity>().eq("id", id));
        return R.ok().put("data", dianpuhuodong);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DianpuhuodongEntity dianpuhuodong, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(dianpuhuodong);
        dianpuhuodongService.insert(dianpuhuodong);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DianpuhuodongEntity dianpuhuodong, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(dianpuhuodong);
        dianpuhuodongService.insert(dianpuhuodong);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DianpuhuodongEntity dianpuhuodong, HttpServletRequest request){
        //ValidatorUtils.validateEntity(dianpuhuodong);
        dianpuhuodongService.updateById(dianpuhuodong);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        dianpuhuodongService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
