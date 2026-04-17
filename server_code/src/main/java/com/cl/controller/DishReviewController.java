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

import com.cl.entity.DishReviewEntity;
import com.cl.entity.view.DishReviewView;

import com.cl.service.DishReviewService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;
import com.cl.entity.OrdersEntity;
import com.cl.service.OrdersService;

/**
 * 美食信息评价表
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:59
 */
@RestController
@RequestMapping({"/dish_review", "/discussdish_info"})
public class DishReviewController {
    @Autowired
    private DishReviewService dishReviewService;
    @Autowired
    private OrdersService ordersService;








    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DishReviewEntity dishReview,
                                                                                                                            HttpServletRequest request){
                                    EntityWrapper<DishReviewEntity> ew = new EntityWrapper<DishReviewEntity>();
                                                                                                                                                                                        
        Wrapper<DishReviewEntity> wrapper = MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dishReview), params), params);
        Object refId = params.get("ref_id");
        if (refId != null && StringUtils.isNotBlank(String.valueOf(refId))) {
            wrapper.eq("refid", refId);
        }
        Object userId = params.get("user_id");
        if (userId != null && StringUtils.isNotBlank(String.valueOf(userId))) {
            wrapper.eq("userid", userId);
        }
        PageUtils page = dishReviewService.queryPage(params, wrapper);
        return R.ok().put("data", page);
    }








    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DishReviewEntity dishReview,
		HttpServletRequest request){
        EntityWrapper<DishReviewEntity> ew = new EntityWrapper<DishReviewEntity>();

		Wrapper<DishReviewEntity> wrapper = MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dishReview), params), params);
		Object refId = params.get("ref_id");
		if (refId != null && StringUtils.isNotBlank(String.valueOf(refId))) {
			wrapper.eq("refid", refId);
		}
		Object userId = params.get("user_id");
		if (userId != null && StringUtils.isNotBlank(String.valueOf(userId))) {
			wrapper.eq("userid", userId);
		}
		PageUtils page = dishReviewService.queryPage(params, wrapper);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DishReviewEntity dishReview){
       	EntityWrapper<DishReviewEntity> ew = new EntityWrapper<DishReviewEntity>();
      	ew.allEq(MPUtil.allEQMapPre( dishReview, "dish_review")); 
        return R.ok().put("data", dishReviewService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DishReviewEntity dishReview){
        EntityWrapper< DishReviewEntity> ew = new EntityWrapper< DishReviewEntity>();
 		ew.allEq(MPUtil.allEQMapPre( dishReview, "dish_review")); 
		DishReviewView dishReviewView =  dishReviewService.selectView(ew);
		return R.ok("查询美食信息评价表成功").put("data", dishReviewView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DishReviewEntity dishReview = dishReviewService.selectById(id);
		dishReview = dishReviewService.selectView(new EntityWrapper<DishReviewEntity>().eq("id", id));
        return R.ok().put("data", dishReview);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DishReviewEntity dishReview = dishReviewService.selectById(id);
		dishReview = dishReviewService.selectView(new EntityWrapper<DishReviewEntity>().eq("id", id));
        return R.ok().put("data", dishReview);
    }
    
    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DishReviewEntity dishReview, HttpServletRequest request){
        Object userIdObj = request.getSession().getAttribute("userId");
        if(userIdObj == null){
            return R.error(401, "未登录或登录过期");
        }
        Long userId = Long.valueOf(String.valueOf(userIdObj));
        if (dishReview.getRefid() == null) {
            return R.error("缺少关联菜品");
        }
        int count = ordersService.selectCount(
                new EntityWrapper<OrdersEntity>()
                        .eq("userid", userId)
                        .eq("tablename", "dish_info")
                        .eq("goodid", dishReview.getRefid())
                        .eq("status", "已完成")
        );
        if (count <= 0) {
            return R.error("请在个人中心的已完成订单中进行评价");
        }
        dishReview.setUserid(userId);
        dishReviewService.insert(dishReview);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DishReviewEntity dishReview, HttpServletRequest request){
        Object userIdObj = request.getSession().getAttribute("userId");
        if(userIdObj == null){
            return R.error(401, "未登录或登录过期");
        }
        Long userId = Long.valueOf(String.valueOf(userIdObj));
        if (dishReview.getRefid() == null) {
            return R.error("缺少关联菜品");
        }
        int count = ordersService.selectCount(
                new EntityWrapper<OrdersEntity>()
                        .eq("userid", userId)
                        .eq("tablename", "dish_info")
                        .eq("goodid", dishReview.getRefid())
                        .eq("status", "已完成")
        );
        if (count <= 0) {
            return R.error("请在个人中心的已完成订单中进行评价");
        }
        dishReview.setUserid(userId);
        dishReviewService.insert(dishReview);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DishReviewEntity dishReview, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        String role = roleObj == null ? "" : String.valueOf(roleObj);
        Object userIdObj = request.getSession().getAttribute("userId");
        Long userId = userIdObj == null ? null : Long.valueOf(String.valueOf(userIdObj));
        if (dishReview.getId() == null) {
            return R.error("缺少评价ID");
        }
        DishReviewEntity existing = dishReviewService.selectById(dishReview.getId());
        if (existing == null) {
            return R.error("评价不存在");
        }
        if (dishReview.getReply() != null && !"".equals(dishReview.getReply())) {
            if (!"管理员".equals(role) && !"员工".equals(role)) {
                return R.error("仅管理员或员工可回复评价");
            }
            String reply = String.valueOf(dishReview.getReply()).trim();
            String prefix = "餐厅回复：";
            if (!reply.startsWith("餐厅回复：")) {
                dishReview.setReply(prefix + reply.replaceFirst("^(管理员：|员工：)\\s*", ""));
            }
        } else {
            if (userId == null) {
                return R.error(401, "未登录或登录过期");
            }
            if (!existing.getUserid().equals(userId)) {
                return R.error("不能修改他人的评价");
            }
        }
        dishReviewService.updateById(dishReview);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        dishReviewService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
