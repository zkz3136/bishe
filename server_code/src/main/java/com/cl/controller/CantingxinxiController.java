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

import com.cl.entity.CantingxinxiEntity;
import com.cl.entity.view.CantingxinxiView;

import com.cl.service.CantingxinxiService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;
import com.cl.service.StoreupService;
import com.cl.entity.StoreupEntity;

/**
 * 餐厅信息
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/cantingxinxi")
public class CantingxinxiController {
    @Autowired
    private CantingxinxiService cantingxinxiService;

    @Autowired
    private StoreupService storeupService;








    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CantingxinxiEntity cantingxinxi,
                                                                                                                                                                            HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                                                                                                    // 删除员工字段后，无需权限过滤
                                                                                                                                                    EntityWrapper<CantingxinxiEntity> ew = new EntityWrapper<CantingxinxiEntity>();
                                                                                                                                                                                                                                                                        
        
        
        PageUtils page = cantingxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cantingxinxi), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CantingxinxiEntity cantingxinxi,
		HttpServletRequest request){
        EntityWrapper<CantingxinxiEntity> ew = new EntityWrapper<CantingxinxiEntity>();

		PageUtils page = cantingxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cantingxinxi), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CantingxinxiEntity cantingxinxi){
       	EntityWrapper<CantingxinxiEntity> ew = new EntityWrapper<CantingxinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( cantingxinxi, "cantingxinxi")); 
        return R.ok().put("data", cantingxinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CantingxinxiEntity cantingxinxi){
        EntityWrapper< CantingxinxiEntity> ew = new EntityWrapper< CantingxinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( cantingxinxi, "cantingxinxi")); 
		CantingxinxiView cantingxinxiView =  cantingxinxiService.selectView(ew);
		return R.ok("查询餐厅信息成功").put("data", cantingxinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CantingxinxiEntity cantingxinxi = cantingxinxiService.selectById(id);
		cantingxinxi = cantingxinxiService.selectView(new EntityWrapper<CantingxinxiEntity>().eq("id", id));
        return R.ok().put("data", cantingxinxi);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CantingxinxiEntity cantingxinxi = cantingxinxiService.selectById(id);
		cantingxinxi = cantingxinxiService.selectView(new EntityWrapper<CantingxinxiEntity>().eq("id", id));
        return R.ok().put("data", cantingxinxi);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CantingxinxiEntity cantingxinxi, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cantingxinxi);
        cantingxinxiService.insert(cantingxinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody CantingxinxiEntity cantingxinxi, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cantingxinxi);
        cantingxinxiService.insert(cantingxinxi);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CantingxinxiEntity cantingxinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cantingxinxi);
        cantingxinxiService.updateById(cantingxinxi);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        cantingxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
