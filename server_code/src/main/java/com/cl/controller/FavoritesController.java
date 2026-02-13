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

import com.cl.entity.FavoritesEntity;
import com.cl.entity.view.FavoritesView;

import com.cl.service.FavoritesService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 我的收藏
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping({"/favorites", "/storeup"})
public class FavoritesController {
    @Autowired
    private FavoritesService storeupService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,FavoritesEntity favorites,
                                                                                                                                HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            favorites.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        EntityWrapper<FavoritesEntity> ew = new EntityWrapper<FavoritesEntity>();
        PageUtils page = storeupService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, favorites), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,FavoritesEntity favorites,
		HttpServletRequest request){
        EntityWrapper<FavoritesEntity> ew = new EntityWrapper<FavoritesEntity>();

		PageUtils page = storeupService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, favorites), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( FavoritesEntity favorites){
       	EntityWrapper<FavoritesEntity> ew = new EntityWrapper<FavoritesEntity>();
      	ew.allEq(MPUtil.allEQMapPre( favorites, "favorites")); 
        return R.ok().put("data", storeupService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(FavoritesEntity favorites){
        EntityWrapper< FavoritesEntity> ew = new EntityWrapper< FavoritesEntity>();
 		ew.allEq(MPUtil.allEQMapPre( favorites, "favorites")); 
		FavoritesView storeupView =  storeupService.selectView(ew);
		return R.ok("查询我的收藏成功").put("data", storeupView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FavoritesEntity favorites = storeupService.selectById(id);
		favorites = storeupService.selectView(new EntityWrapper<FavoritesEntity>().eq("id", id));
        return R.ok().put("data", favorites);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        FavoritesEntity favorites = storeupService.selectById(id);
		favorites = storeupService.selectView(new EntityWrapper<FavoritesEntity>().eq("id", id));
        return R.ok().put("data", favorites);
    }
    
    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FavoritesEntity favorites, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(favorites);
    	favorites.setUserid((Long)request.getSession().getAttribute("userId"));
        storeupService.insert(favorites);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody FavoritesEntity favorites, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(favorites);
    	favorites.setUserid((Long)request.getSession().getAttribute("userId"));
        storeupService.insert(favorites);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody FavoritesEntity favorites, HttpServletRequest request){
        //ValidatorUtils.validateEntity(favorites);
        storeupService.updateById(favorites);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        storeupService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
