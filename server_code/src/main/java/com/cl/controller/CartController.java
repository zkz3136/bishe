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
import com.cl.entity.CartEntity;
import com.cl.entity.view.CartView;

import com.cl.service.CartService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 购物车
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CartEntity cart,
                                                                                                                                    HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if(roleObj == null){
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = roleObj.toString();
        if(!"管理员".equals(roleStr)) {
            Object uidObj = request.getSession().getAttribute("userId");
            if(uidObj == null){
                return R.error(401, "登录状态失效，请重新登录！");
            }
            cart.setUserid((Long)uidObj);
        }
        EntityWrapper<CartEntity> ew = new EntityWrapper<CartEntity>();
        PageUtils page = cartService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cart), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CartEntity cart,
		HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object uidObj = request.getSession().getAttribute("userId");
        if (roleObj == null || uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            cart.setUserid((Long) uidObj);
        }
        EntityWrapper<CartEntity> ew = new EntityWrapper<CartEntity>();
		PageUtils page = cartService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cart), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(CartEntity cart, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object uidObj = request.getSession().getAttribute("userId");
        if (roleObj == null || uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            cart.setUserid((Long) uidObj);
        }
       	EntityWrapper<CartEntity> ew = new EntityWrapper<CartEntity>();
      	ew.allEq(MPUtil.allEQMapPre( cart, "cart")); 
        return R.ok().put("data", cartService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CartEntity cart, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object uidObj = request.getSession().getAttribute("userId");
        if (roleObj == null || uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            cart.setUserid((Long) uidObj);
        }
        EntityWrapper< CartEntity> ew = new EntityWrapper< CartEntity>();
 		ew.allEq(MPUtil.allEQMapPre( cart, "cart")); 
		CartView cartView =  cartService.selectView(ew);
		return R.ok("查询购物车成功").put("data", cartView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object uidObj = request.getSession().getAttribute("userId");
        if (roleObj == null || uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        CartEntity cart = cartService.selectById(id);
		cart = cartService.selectView(new EntityWrapper<CartEntity>().eq("id", id));
        if (cart == null) {
            return R.error("数据不存在");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr) && cart.getUserid() != null && !cart.getUserid().equals((Long) uidObj)) {
            return R.error(403, "无权限访问");
        }
        return R.ok().put("data", cart);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object uidObj = request.getSession().getAttribute("userId");
        if (roleObj == null || uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        CartEntity cart = cartService.selectById(id);
		cart = cartService.selectView(new EntityWrapper<CartEntity>().eq("id", id));
        if (cart == null) {
            return R.error("数据不存在");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr) && cart.getUserid() != null && !cart.getUserid().equals((Long) uidObj)) {
            return R.error(403, "无权限访问");
        }
        return R.ok().put("data", cart);
    }
    
    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CartEntity cart, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cart);
    	cart.setUserid((Long)request.getSession().getAttribute("userId"));
        cartService.insert(cart);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody CartEntity cart, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cart);
        Object uidObj = request.getSession().getAttribute("userId");
        if (uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        cart.setUserid((Long) uidObj);
        cartService.insert(cart);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CartEntity cart, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cart);
        Object roleObj = request.getSession().getAttribute("role");
        Object uidObj = request.getSession().getAttribute("userId");
        if (roleObj == null || uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            CartEntity db = cartService.selectById(cart.getId());
            if (db == null) {
                return R.error("数据不存在");
            }
            if (db.getUserid() != null && !db.getUserid().equals((Long) uidObj)) {
                return R.error(403, "无权限操作");
            }
            cart.setUserid(db.getUserid());
        }
        cartService.updateById(cart);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object uidObj = request.getSession().getAttribute("userId");
        if (roleObj == null || uidObj == null) {
            return R.error(401, "登录状态失效，请重新登录！");
        }
        if (ids == null || ids.length == 0) {
            return R.error("参数错误");
        }
        String roleStr = String.valueOf(roleObj);
        if (!"管理员".equals(roleStr)) {
            List<CartEntity> list = cartService.selectBatchIds(Arrays.asList(ids));
            if (list != null) {
                for (CartEntity c : list) {
                    if (c == null) continue;
                    if (c.getUserid() != null && !c.getUserid().equals((Long) uidObj)) {
                        return R.error(403, "无权限操作");
                    }
                }
            }
        }
        cartService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
