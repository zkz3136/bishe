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

import com.cl.entity.CaipinleixingEntity;
import com.cl.entity.view.CaipinleixingView;

import com.cl.service.CaipinleixingService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 菜品类型
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
@RestController
@RequestMapping("/caipinleixing")
public class CaipinleixingController {
    @Autowired
    private CaipinleixingService caipinleixingService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CaipinleixingEntity caipinleixing,
                                            HttpServletRequest request){
                                    EntityWrapper<CaipinleixingEntity> ew = new EntityWrapper<CaipinleixingEntity>();
                                                        
        
        
        PageUtils page = caipinleixingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipinleixing), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CaipinleixingEntity caipinleixing,
		HttpServletRequest request){
        EntityWrapper<CaipinleixingEntity> ew = new EntityWrapper<CaipinleixingEntity>();

		PageUtils page = caipinleixingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipinleixing), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CaipinleixingEntity caipinleixing){
       	EntityWrapper<CaipinleixingEntity> ew = new EntityWrapper<CaipinleixingEntity>();
      	ew.allEq(MPUtil.allEQMapPre( caipinleixing, "caipinleixing")); 
        return R.ok().put("data", caipinleixingService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CaipinleixingEntity caipinleixing){
        EntityWrapper< CaipinleixingEntity> ew = new EntityWrapper< CaipinleixingEntity>();
 		ew.allEq(MPUtil.allEQMapPre( caipinleixing, "caipinleixing")); 
		CaipinleixingView caipinleixingView =  caipinleixingService.selectView(ew);
		return R.ok("查询菜品类型成功").put("data", caipinleixingView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CaipinleixingEntity caipinleixing = caipinleixingService.selectById(id);
		caipinleixing = caipinleixingService.selectView(new EntityWrapper<CaipinleixingEntity>().eq("id", id));
        return R.ok().put("data", caipinleixing);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CaipinleixingEntity caipinleixing = caipinleixingService.selectById(id);
		caipinleixing = caipinleixingService.selectView(new EntityWrapper<CaipinleixingEntity>().eq("id", id));
        return R.ok().put("data", caipinleixing);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CaipinleixingEntity caipinleixing, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(caipinleixing);
        caipinleixingService.insert(caipinleixing);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody CaipinleixingEntity caipinleixing, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(caipinleixing);
        caipinleixingService.insert(caipinleixing);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CaipinleixingEntity caipinleixing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(caipinleixing);
        caipinleixingService.updateById(caipinleixing);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        caipinleixingService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
