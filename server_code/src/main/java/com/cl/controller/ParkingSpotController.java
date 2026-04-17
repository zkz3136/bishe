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

import com.cl.entity.ParkingSpotEntity;
import com.cl.entity.view.ParkingSpotView;

import com.cl.service.ParkingSpotService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 车位信息
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/parking_spot")
public class ParkingSpotController {
    @Autowired
    private ParkingSpotService cheweixinxiService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ParkingSpotEntity parkingSpot,
                                                                                                                                                HttpServletRequest request){
                                    EntityWrapper<ParkingSpotEntity> ew = new EntityWrapper<ParkingSpotEntity>();
                                                                                                                                                                                                                    
        
        
        PageUtils page = cheweixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, parkingSpot), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ParkingSpotEntity parkingSpot,
		HttpServletRequest request){
        EntityWrapper<ParkingSpotEntity> ew = new EntityWrapper<ParkingSpotEntity>();

		PageUtils page = cheweixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, parkingSpot), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ParkingSpotEntity parkingSpot){
       	EntityWrapper<ParkingSpotEntity> ew = new EntityWrapper<ParkingSpotEntity>();
      	ew.allEq(MPUtil.allEQMapPre( parkingSpot, "parking_spot")); 
        return R.ok().put("data", cheweixinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ParkingSpotEntity parkingSpot){
        EntityWrapper< ParkingSpotEntity> ew = new EntityWrapper< ParkingSpotEntity>();
 		ew.allEq(MPUtil.allEQMapPre( parkingSpot, "parking_spot")); 
		ParkingSpotView cheweixinxiView =  cheweixinxiService.selectView(ew);
		return R.ok("查询车位信息成功").put("data", cheweixinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ParkingSpotEntity parkingSpot = cheweixinxiService.selectById(id);
		parkingSpot = cheweixinxiService.selectView(new EntityWrapper<ParkingSpotEntity>().eq("id", id));
        return R.ok().put("data", parkingSpot);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ParkingSpotEntity parkingSpot = cheweixinxiService.selectById(id);
		parkingSpot = cheweixinxiService.selectView(new EntityWrapper<ParkingSpotEntity>().eq("id", id));
        return R.ok().put("data", parkingSpot);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ParkingSpotEntity parkingSpot, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            return R.error(403, "无权限操作");
        }
        if(cheweixinxiService.selectCount(new EntityWrapper<ParkingSpotEntity>().eq("spot_number", parkingSpot.getSpotNumber()))>0) {
            return R.error("车位编号已存在");
        }
    	//ValidatorUtils.validateEntity(parkingSpot);
        parkingSpot.setSpotStatus("空闲");
        cheweixinxiService.insert(parkingSpot);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ParkingSpotEntity parkingSpot, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            return R.error(403, "无权限操作");
        }
        if(cheweixinxiService.selectCount(new EntityWrapper<ParkingSpotEntity>().eq("spot_number", parkingSpot.getSpotNumber()))>0) {
            return R.error("车位编号已存在");
        }
    	//ValidatorUtils.validateEntity(parkingSpot);
        parkingSpot.setSpotStatus("空闲");
        cheweixinxiService.insert(parkingSpot);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ParkingSpotEntity parkingSpot, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            return R.error(403, "无权限操作");
        }
        //ValidatorUtils.validateEntity(parkingSpot);
        cheweixinxiService.updateById(parkingSpot);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            return R.error(403, "无权限操作");
        }
        cheweixinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 入场
     */
    @PostMapping("/entry")
    public R entry(@RequestBody ParkingSpotEntity parkingSpot){
        ParkingSpotEntity entity = cheweixinxiService.selectById(parkingSpot.getId());
        if(entity == null){
            return R.error("车位不存在");
        }
        if("已使用".equals(entity.getSpotStatus())){
            return R.error("车位已使用");
        }
        entity.setSpotStatus("已使用");
        entity.setPlateNumber(parkingSpot.getPlateNumber());
        entity.setEntryTime(new Date());
        cheweixinxiService.updateById(entity);
        return R.ok();
    }

    /**
     * 离场
     */
    @PostMapping("/exit")
    public R exit(@RequestBody Map<String, Object> params){
        Long id = Long.parseLong(params.get("id").toString());
        ParkingSpotEntity entity = cheweixinxiService.selectById(id);
        if(entity == null){
            return R.error("车位不存在");
        }
        if(!"已使用".equals(entity.getSpotStatus())){
            return R.error("车位未使用");
        }
        
        Date now = new Date();
        int durationMinutes = 0;
        int halfHours = 1;
        if (entity.getEntryTime() != null) {
            long diff = now.getTime() - entity.getEntryTime().getTime();
            double minutesD = diff / (1000 * 60.0);
            durationMinutes = (int)Math.max(0, Math.round(minutesD));
            halfHours = (int) Math.ceil((durationMinutes) / 30.0);
            if(halfHours < 1) halfHours = 1;
        }
        
        Double hourly = entity.getHourlyPrice();
        if (hourly == null || hourly <= 0) {
            hourly = 10.0;
        }
        double unitPrice = hourly / 2.0;
        double price = new BigDecimal(halfHours * unitPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        
        // Reset status
        entity.setSpotStatus("空闲");
        entity.setPlateNumber("");
        entity.setEntryTime(null);
        cheweixinxiService.updateById(entity);
        
        return R.ok().put("price", price).put("duration", durationMinutes).put("unit", "半小时").put("unitPrice", unitPrice);
    }
    
    /**
     * 总数量
     */
    @IgnoreAuth
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params, ParkingSpotEntity parkingSpot, HttpServletRequest request){
        EntityWrapper<ParkingSpotEntity> ew = new EntityWrapper<ParkingSpotEntity>();
        int count = cheweixinxiService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, parkingSpot), params), params));
        return R.ok().put("data", count);
    }
    
	









}
