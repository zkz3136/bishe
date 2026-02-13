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

import com.cl.entity.RestaurantInfoEntity;
import com.cl.entity.view.RestaurantInfoView;

import com.cl.service.RestaurantInfoService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;
import com.cl.service.FavoritesService;
import com.cl.entity.FavoritesEntity;

/**
 * 餐厅信息
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/restaurant_info")
public class RestaurantInfoController {
    @Autowired
    private RestaurantInfoService restaurantInfoService;

    @Autowired
    private FavoritesService storeupService;








    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,RestaurantInfoEntity restaurantInfo,
                                                                                                                                                                            HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                                                                                                    // 删除员工字段后，无需权限过滤
                                                                                                                                                    EntityWrapper<RestaurantInfoEntity> ew = new EntityWrapper<RestaurantInfoEntity>();
                                                                                                                                                                                                                                                                        
        
        
        PageUtils page = restaurantInfoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, restaurantInfo), params), params));
        return R.ok().put("data", page);
    }








    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,RestaurantInfoEntity restaurantInfo,
		HttpServletRequest request){
        EntityWrapper<RestaurantInfoEntity> ew = new EntityWrapper<RestaurantInfoEntity>();

		PageUtils page = restaurantInfoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, restaurantInfo), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( RestaurantInfoEntity restaurantInfo){
       	EntityWrapper<RestaurantInfoEntity> ew = new EntityWrapper<RestaurantInfoEntity>();
      	ew.allEq(MPUtil.allEQMapPre( restaurantInfo, "restaurant_info")); 
        return R.ok().put("data", restaurantInfoService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(RestaurantInfoEntity restaurantInfo){
        EntityWrapper< RestaurantInfoEntity> ew = new EntityWrapper< RestaurantInfoEntity>();
 		ew.allEq(MPUtil.allEQMapPre( restaurantInfo, "restaurant_info")); 
		RestaurantInfoView restaurantInfoView =  restaurantInfoService.selectView(ew);
		return R.ok("查询餐厅信息成功").put("data", restaurantInfoView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RestaurantInfoEntity restaurantInfo = restaurantInfoService.selectById(id);
		restaurantInfo = restaurantInfoService.selectView(new EntityWrapper<RestaurantInfoEntity>().eq("id", id));
        return R.ok().put("data", restaurantInfo);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RestaurantInfoEntity restaurantInfo = restaurantInfoService.selectById(id);
		restaurantInfo = restaurantInfoService.selectView(new EntityWrapper<RestaurantInfoEntity>().eq("id", id));
        return R.ok().put("data", restaurantInfo);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RestaurantInfoEntity restaurantInfo, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(restaurantInfo);
        boolean ok = restaurantInfoService.insert(restaurantInfo);
        if(!ok){
            return R.error("保存失败");
        }
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody RestaurantInfoEntity restaurantInfo, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(restaurantInfo);
        boolean ok = restaurantInfoService.insert(restaurantInfo);
        if(!ok){
            return R.error("保存失败");
        }
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RestaurantInfoEntity restaurantInfo, HttpServletRequest request){
        //ValidatorUtils.validateEntity(restaurantInfo);
        if(restaurantInfo.getId() == null){
            return R.error("id不能为空");
        }
        boolean ok = restaurantInfoService.updateById(restaurantInfo);//全部更新
        if(!ok){
            return R.error("更新失败");
        }
        return R.ok();
    }




    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        restaurantInfoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
}
