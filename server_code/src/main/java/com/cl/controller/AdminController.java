package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.AdminEntity;
import com.cl.service.AdminService;
import com.cl.utils.CommonUtil;
import com.cl.utils.JwtUtils;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;

	@Autowired
	private JwtUtils jwtUtils;

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
		AdminEntity u = adminService.selectOne(new EntityWrapper<AdminEntity>().eq("login_name", loginName).eq("role", "管理员"));
		if (u == null || !u.getPassword().equals(password)) {
			return R.error("账号或密码不正确");
		}
		String token = jwtUtils.generateToken(u.getId(), loginName, "admin", "管理员");
		return R.ok().put("token", token);
	}

	@IgnoreAuth
	@RequestMapping("/register")
	public R register(@RequestBody AdminEntity admin) {
		AdminEntity u = adminService.selectOne(new EntityWrapper<AdminEntity>().eq("login_name", admin.getLoginName()));
		if (u != null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		admin.setId(uId);
		admin.setPassword(admin.getPassword());
		admin.setRole("管理员");
		adminService.insert(admin);
		return R.ok();
	}

	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}

	@RequestMapping("/session")
	public R getCurrUser(HttpServletRequest request) {
		Long id = (Long) request.getSession().getAttribute("userId");
		return R.ok().put("data", adminService.selectView(new EntityWrapper<AdminEntity>().eq("id", id)));
	}

	@IgnoreAuth
	@RequestMapping(value = "/resetPass")
	public R resetPass(String username, HttpServletRequest request) {
		String loginName = username;
		if (CommonUtil.isEmpty(loginName)) {
			loginName = request.getParameter("login_name");
		}
		if (CommonUtil.isEmpty(loginName)) {
			loginName = request.getParameter("loginName");
		}
		AdminEntity u = adminService.selectOne(new EntityWrapper<AdminEntity>().eq("login_name", loginName).eq("role", "管理员"));
		if (u == null) {
			return R.error("账号不存在");
		}
		u.setPassword("123456");
		adminService.updateById(u);
		return R.ok("密码已重置为：123456");
	}

	@RequestMapping("/page")
	public R page(@RequestParam Map<String, Object> params, AdminEntity admin, HttpServletRequest request) {
		admin.setRole("管理员");
		EntityWrapper<AdminEntity> ew = new EntityWrapper<AdminEntity>();
		PageUtils page = adminService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, admin), params), params));
		return R.ok().put("data", page);
	}

	@IgnoreAuth
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params, AdminEntity admin, HttpServletRequest request) {
		admin.setRole("管理员");
		EntityWrapper<AdminEntity> ew = new EntityWrapper<AdminEntity>();
		PageUtils page = adminService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, admin), params), params));
		return R.ok().put("data", page);
	}

	@RequestMapping("/lists")
	public R list(AdminEntity admin) {
		admin.setRole("管理员");
		EntityWrapper<AdminEntity> ew = new EntityWrapper<AdminEntity>();
		ew.allEq(MPUtil.allEQMapPre(admin, "admin"));
		return R.ok().put("data", adminService.selectListView(ew));
	}

	@RequestMapping("/query")
	public R query(AdminEntity admin) {
		admin.setRole("管理员");
		EntityWrapper<AdminEntity> ew = new EntityWrapper<AdminEntity>();
		ew.allEq(MPUtil.allEQMapPre(admin, "admin"));
		return R.ok("查询管理员成功").put("data", adminService.selectView(ew));
	}

	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		AdminEntity admin = adminService.selectById(id);
		admin = adminService.selectView(new EntityWrapper<AdminEntity>().eq("id", id));
		return R.ok().put("data", admin);
	}

	@IgnoreAuth
	@RequestMapping("/detail/{id}")
	public R detail(@PathVariable("id") Long id) {
		AdminEntity admin = adminService.selectById(id);
		admin = adminService.selectView(new EntityWrapper<AdminEntity>().eq("id", id));
		return R.ok().put("data", admin);
	}

	@RequestMapping("/save")
	public R save(@RequestBody AdminEntity admin, HttpServletRequest request) {
		AdminEntity u = adminService.selectOne(new EntityWrapper<AdminEntity>().eq("login_name", admin.getLoginName()));
		if (u != null) {
			return R.error("用户已存在");
		}
		admin.setId(new Date().getTime());
		admin.setRole("管理员");
		adminService.insert(admin);
		return R.ok();
	}

	@IgnoreAuth
	@RequestMapping("/add")
	public R add(@RequestBody AdminEntity admin, HttpServletRequest request) {
		AdminEntity u = adminService.selectOne(new EntityWrapper<AdminEntity>().eq("login_name", admin.getLoginName()));
		if (u != null) {
			return R.error("用户已存在");
		}
		admin.setId(new Date().getTime());
		admin.setRole("管理员");
		adminService.insert(admin);
		return R.ok();
	}

	@RequestMapping("/update")
	@Transactional
	public R update(@RequestBody AdminEntity admin, HttpServletRequest request) {
		adminService.updateById(admin);
		return R.ok();
	}

	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		adminService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}
}
