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

import com.cl.entity.RestaurantEventEntity;
import com.cl.entity.view.RestaurantEventView;

import com.cl.service.RestaurantEventService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 餐厅活动
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/restaurant_event")
public class RestaurantEventController {
    @Autowired
    private RestaurantEventService restaurantEventService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,RestaurantEventEntity restaurantEvent,
                                                                                                                    HttpServletRequest request){
                                    EntityWrapper<RestaurantEventEntity> ew = new EntityWrapper<RestaurantEventEntity>();
        Object eventNameObj = params.get("eventName");
        if(eventNameObj == null) {
            eventNameObj = params.get("event_name");
        }
        if(eventNameObj != null && StringUtils.isNotBlank(String.valueOf(eventNameObj))) {
            String keyword = String.valueOf(eventNameObj).trim();
            if(StringUtils.isNotBlank(keyword)) {
                ew.like("event_name", keyword);
                restaurantEvent.setEventName(null);
            }
        }
        PageUtils page = restaurantEventService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, restaurantEvent), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,RestaurantEventEntity restaurantEvent,
		HttpServletRequest request){
        EntityWrapper<RestaurantEventEntity> ew = new EntityWrapper<RestaurantEventEntity>();

		PageUtils page = restaurantEventService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, restaurantEvent), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( RestaurantEventEntity restaurantEvent){
       	EntityWrapper<RestaurantEventEntity> ew = new EntityWrapper<RestaurantEventEntity>();
      	ew.allEq(MPUtil.allEQMapPre( restaurantEvent, "restaurant_event")); 
        return R.ok().put("data", restaurantEventService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(RestaurantEventEntity restaurantEvent){
        EntityWrapper< RestaurantEventEntity> ew = new EntityWrapper< RestaurantEventEntity>();
 		ew.allEq(MPUtil.allEQMapPre( restaurantEvent, "restaurant_event")); 
		RestaurantEventView restaurantEventView =  restaurantEventService.selectView(ew);
		return R.ok("查询餐厅活动成功").put("data", restaurantEventView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RestaurantEventEntity restaurantEvent = restaurantEventService.selectById(id);
		restaurantEvent = restaurantEventService.selectView(new EntityWrapper<RestaurantEventEntity>().eq("id", id));
        return R.ok().put("data", restaurantEvent);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RestaurantEventEntity restaurantEvent = restaurantEventService.selectById(id);
		restaurantEvent = restaurantEventService.selectView(new EntityWrapper<RestaurantEventEntity>().eq("id", id));
        return R.ok().put("data", restaurantEvent);
    }
    
    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RestaurantEventEntity restaurantEvent, HttpServletRequest request){
        if(restaurantEvent.getEventName() == null || StringUtils.isBlank(restaurantEvent.getEventName())) {
            restaurantEvent.setEventName("未命名活动");
        }
        restaurantEventService.insert(restaurantEvent);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody RestaurantEventEntity restaurantEvent, HttpServletRequest request){
        if(restaurantEvent.getEventName() == null || StringUtils.isBlank(restaurantEvent.getEventName())) {
            restaurantEvent.setEventName("未命名活动");
        }
        restaurantEventService.insert(restaurantEvent);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RestaurantEventEntity restaurantEvent, HttpServletRequest request){
        //ValidatorUtils.validateEntity(restaurantEvent);
        restaurantEventService.updateById(restaurantEvent);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        restaurantEventService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
}
