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

import com.cl.entity.StaffEntity;
import com.cl.entity.view.StaffView;

import com.cl.service.StaffService;
import com.cl.utils.JwtUtils;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 员工
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;



	@Autowired
	private JwtUtils jwtUtils;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		String loginName = username;
		if (CommonUtil.isEmpty(loginName)) {
			loginName = request.getParameter("login_name");
		}
		if (CommonUtil.isEmpty(loginName)) {
			loginName = request.getParameter("loginName");
		}
		StaffEntity u = staffService.selectOne(new EntityWrapper<StaffEntity>().eq("login_name", loginName).eq("role", "员工"));
        if(u==null || !u.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
		String token = jwtUtils.generateToken(u.getId(), loginName,"staff",  "员工" );
		return R.ok().put("token", token);
	}


	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody StaffEntity staff){
    	//ValidatorUtils.validateEntity(staff);
                            StaffEntity u = staffService.selectOne(new EntityWrapper<StaffEntity>().eq("login_name", staff.getLoginName()));
                                                                                                            		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		staff.setId(uId);
		staff.setRole("员工");
        staffService.insert(staff);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        return R.ok().put("data", staffService.selectView(new EntityWrapper<StaffEntity>().eq("id", id)));
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	String loginName = username;
    	if (CommonUtil.isEmpty(loginName)) {
    		loginName = request.getParameter("login_name");
    	}
    	if (CommonUtil.isEmpty(loginName)) {
    		loginName = request.getParameter("loginName");
    	}
    	StaffEntity u = staffService.selectOne(new EntityWrapper<StaffEntity>().eq("login_name", loginName).eq("role", "员工"));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setPassword("123456");
        staffService.updateById(u);
        return R.ok("密码已重置为：123456");
    }






    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,StaffEntity staff,
                                                                                                                                            HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                            if(tableName.equals("staff")) {
                    staff.setLoginName((String)request.getSession().getAttribute("username"));
                                    }
					staff.setRole("员工");
                                                                                                                                                                                                                                                                                                EntityWrapper<StaffEntity> ew = new EntityWrapper<StaffEntity>();
                                                                                                                                                                                                                        
        
        
        PageUtils page = staffService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, staff), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,StaffEntity staff,
		HttpServletRequest request){
		staff.setRole("员工");
        EntityWrapper<StaffEntity> ew = new EntityWrapper<StaffEntity>();

		PageUtils page = staffService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, staff), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( StaffEntity staff){
		staff.setRole("员工");
       	EntityWrapper<StaffEntity> ew = new EntityWrapper<StaffEntity>();
      	ew.allEq(MPUtil.allEQMapPre( staff, "staff")); 
        return R.ok().put("data", staffService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(StaffEntity staff){
		staff.setRole("员工");
        EntityWrapper< StaffEntity> ew = new EntityWrapper< StaffEntity>();
 		ew.allEq(MPUtil.allEQMapPre( staff, "staff")); 
		StaffView staffView =  staffService.selectView(ew);
		return R.ok("查询员工成功").put("data", staffView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        StaffEntity staff = staffService.selectById(id);
		staff = staffService.selectView(new EntityWrapper<StaffEntity>().eq("id", id));
        return R.ok().put("data", staff);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        StaffEntity staff = staffService.selectById(id);
		staff = staffService.selectView(new EntityWrapper<StaffEntity>().eq("id", id));
        return R.ok().put("data", staff);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StaffEntity staff, HttpServletRequest request){
        if(staffService.selectCount(new EntityWrapper<StaffEntity>().eq("login_name", staff.getLoginName()))>0) {
            return R.error("员工账号已存在");
        }
    	staff.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(staff);
        StaffEntity u = staffService.selectOne(new EntityWrapper<StaffEntity>().eq("login_name", staff.getLoginName()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		staff.setId(new Date().getTime());
		staff.setRole("员工");
        staffService.insert(staff);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody StaffEntity staff, HttpServletRequest request){
        if(staffService.selectCount(new EntityWrapper<StaffEntity>().eq("login_name", staff.getLoginName()))>0) {
            return R.error("员工账号已存在");
        }
    	staff.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(staff);
        StaffEntity u = staffService.selectOne(new EntityWrapper<StaffEntity>().eq("login_name", staff.getLoginName()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		staff.setId(new Date().getTime());
		staff.setRole("员工");
        staffService.insert(staff);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody StaffEntity staff, HttpServletRequest request){
        //ValidatorUtils.validateEntity(staff);
        staffService.updateById(staff);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        staffService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
