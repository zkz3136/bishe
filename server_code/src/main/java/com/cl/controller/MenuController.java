package com.cl.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cl.annotation.IgnoreAuth;

import com.cl.entity.MenuEntity;
import com.cl.entity.view.MenuView;

import com.cl.utils.PageUtils;
import com.cl.utils.R;

/**
 * 菜单
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    private static final long DEFAULT_MENU_ID = 1L;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,MenuEntity menu,
                        HttpServletRequest request){
        PageUtils page = buildDefaultPage();
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,MenuEntity menu,
		HttpServletRequest request){
        PageUtils page = buildDefaultPage();
        return R.ok().put("data", page);
    }

	private void pruneDishCategoryMenus(Object node) {
		if (node == null) {
			return;
		}
		if (node instanceof JSONObject) {
			JSONObject json = (JSONObject) node;
			pruneRestaurantInfoCommentButtons(json);
			for (String key : json.keySet()) {
				pruneDishCategoryMenus(json.get(key));
			}
			return;
		}
		if (node instanceof JSONArray) {
			JSONArray arr = (JSONArray) node;
			for (int i = arr.size() - 1; i >= 0; i--) {
				Object item = arr.get(i);
				if (item instanceof JSONObject) {
					JSONObject obj = (JSONObject) item;
					String menuName = obj.getString("menu");
					String tableName = obj.getString("tableName");
					String className = obj.getString("classname");
					pruneRestaurantInfoCommentButtons(obj);
					if ("菜品类型".equals(menuName) || "dish_category".equals(tableName) || "dish_category".equals(className)) {
						arr.remove(i);
						continue;
					}
					if ("餐厅信息评价".equals(menuName) || "restaurant_review".equals(tableName) || "restaurant_review".equals(className)
							|| "discuss_restaurant_info".equals(tableName) || "discuss_restaurant_info".equals(className)
							|| "discussrestaurant_info".equals(tableName) || "discussrestaurant_info".equals(className)) {
						arr.remove(i);
						continue;
					}
					pruneDishCategoryMenus(obj);
					Object childObj = obj.get("child");
					if (childObj instanceof JSONArray) {
						JSONArray child = (JSONArray) childObj;
						if (child.isEmpty()) {
							arr.remove(i);
						}
					}
				}
			}
		}
	}

	private void pruneRestaurantInfoCommentButtons(JSONObject obj) {
		if (obj == null) {
			return;
		}
		String menuName = obj.getString("menu");
		String tableName = obj.getString("tableName");
		String className = obj.getString("classname");
		boolean isRestaurantInfo = "餐厅信息".equals(menuName)
				|| "restaurant_info".equals(tableName) || "cantingxinxi".equals(tableName)
				|| "restaurant_info".equals(className) || "cantingxinxi".equals(className);
		if (!isRestaurantInfo) {
			return;
		}
		removeStringFromJsonArray(obj, "buttons", "查看评价");
		removeStringFromJsonArray(obj, "allButtons", "查看评价");
	}

	private void removeStringFromJsonArray(JSONObject obj, String key, String valueToRemove) {
		Object arrObj = obj.get(key);
		if (!(arrObj instanceof JSONArray)) {
			return;
		}
		JSONArray arr = (JSONArray) arrObj;
		for (int i = arr.size() - 1; i >= 0; i--) {
			Object v = arr.get(i);
			if (valueToRemove.equals(v)) {
				arr.remove(i);
			}
		}
	}

	private void normalizeRoleBlock(JSONObject roleJson) {
		String roleName = roleJson.getString("roleName");
		String normalizedPath = mapRoleNameToNormalizedPath(roleName);
		if (normalizedPath == null || normalizedPath.isEmpty()) {
			return;
		}

		String oldTableName = roleJson.getString("tableName");
		if (oldTableName != null && !oldTableName.isEmpty() && !normalizedPath.equals(oldTableName)) {
			replaceStringValues(roleJson, oldTableName, normalizedPath);
		}
		roleJson.put("tableName", normalizedPath);
		roleJson.put("pathName", normalizedPath);
	}

	private String mapRoleNameToNormalizedPath(String roleName) {
		if ("管理员".equals(roleName)) {
			return "admin";
		}
		if ("用户".equals(roleName)) {
			return "user";
		}
		if ("员工".equals(roleName)) {
			return "staff";
		}
		return null;
	}

	private void replaceStringValues(Object node, String oldValue, String newValue) {
		if (node == null) {
			return;
		}
		if (node instanceof JSONObject) {
			JSONObject json = (JSONObject) node;
			for (String key : json.keySet()) {
				Object value = json.get(key);
				if (value instanceof String) {
					if (oldValue.equals(value)) {
						json.put(key, newValue);
					}
				} else {
					replaceStringValues(value, oldValue, newValue);
				}
			}
			return;
		}
		if (node instanceof JSONArray) {
			JSONArray arr = (JSONArray) node;
			for (int i = 0; i < arr.size(); i++) {
				replaceStringValues(arr.get(i), oldValue, newValue);
			}
		}
	}

	private void applyMenuNameMappings(Object node, Map<String, String> menuNameToPath) {
		if (node == null) {
			return;
		}
		if (node instanceof JSONObject) {
			JSONObject json = (JSONObject) node;
			String menuName = json.getString("menu");
			if (menuName != null && !menuName.isEmpty()) {
				String mappedPath = menuNameToPath.get(menuName);
				if (mappedPath != null && !mappedPath.isEmpty()) {
					if (json.containsKey("tableName")) {
						json.put("tableName", mappedPath);
					}
					if (json.containsKey("classname")) {
						json.put("classname", mappedPath);
					}
				}
			}
			for (String key : json.keySet()) {
				applyMenuNameMappings(json.get(key), menuNameToPath);
			}
			return;
		}
		if (node instanceof JSONArray) {
			JSONArray arr = (JSONArray) node;
			for (int i = 0; i < arr.size(); i++) {
				applyMenuNameMappings(arr.get(i), menuNameToPath);
			}
		}
	}

	private Map<String, String> buildMenuNameToPathMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("管理员", "admin");
		map.put("用户", "user");
		map.put("员工", "staff");
		map.put("餐厅信息", "restaurant_info");
		map.put("餐厅预约", "restaurant_reservation");
		map.put("餐厅活动", "restaurant_event");
		map.put("店铺活动", "restaurant_event");
		map.put("系统公告", "system_notice");
		map.put("美食信息", "dish_info");
		map.put("菜品信息", "dish_info");
		map.put("美食信息评价", "dish_review");
		map.put("菜品评价", "dish_review");
		map.put("车位信息", "parking_spot");
		map.put("购物车", "cart");
		map.put("我的收藏", "favorites");
		map.put("订单", "orders");
		map.put("已支付订单", "orders");
		map.put("轮播图", "config");
		return map;
	}

	private PageUtils buildDefaultPage() {
		MenuEntity menu = buildDefaultMenuEntity();
		List<MenuEntity> list = new ArrayList<MenuEntity>();
		list.add(menu);
		return new PageUtils(list, 1, 1, 1);
	}

	private MenuEntity buildDefaultMenuEntity() {
		MenuEntity menu = new MenuEntity();
		menu.setId(DEFAULT_MENU_ID);
		menu.setMenujson(buildDefaultMenuJson());
		return menu;
	}

	private MenuView buildDefaultMenuView() {
		return new MenuView(buildDefaultMenuEntity());
	}

	private List<MenuView> buildDefaultMenuViews() {
		List<MenuView> list = new ArrayList<MenuView>();
		list.add(buildDefaultMenuView());
		return list;
	}

	private String buildDefaultMenuJson() {
		String fromFile = loadMenuJsonFromResource();
		if (fromFile != null && !fromFile.trim().isEmpty()) {
			String normalized = normalizeMenuJson(fromFile);
			if (normalized != null && !normalized.trim().isEmpty()) {
				return normalized;
			}
		}
		return normalizeMenuJson(buildDefaultMenus().toJSONString());
	}

	private String normalizeMenuJson(String menuJson) {
		if (menuJson == null || menuJson.trim().isEmpty()) {
			return menuJson;
		}
		JSONArray menuArray;
		try {
			menuArray = JSONArray.parseArray(menuJson);
		} catch (Exception e) {
			return menuJson;
		}
		if (menuArray == null) {
			return menuJson;
		}
		Map<String, String> menuNameToPath = buildMenuNameToPathMap();
		for (int i = 0; i < menuArray.size(); i++) {
			Object roleObj = menuArray.get(i);
			if (!(roleObj instanceof JSONObject)) {
				continue;
			}
			JSONObject roleJson = (JSONObject) roleObj;
			normalizeRoleBlock(roleJson);
			applyMenuNameMappings(roleJson, menuNameToPath);
			pruneDishCategoryMenus(roleJson);
		}
		return menuArray.toJSONString();
	}

	private String loadMenuJsonFromResource() {
		ClassPathResource resource = new ClassPathResource("menu.json");
		if (!resource.exists()) {
			return null;
		}
		try (InputStream inputStream = resource.getInputStream()) {
			return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}
	}

	private JSONArray buildDefaultMenus() {
		JSONArray arr = new JSONArray();
		arr.add(buildAdminMenu());
		arr.add(buildUserMenu());
		arr.add(buildStaffMenu());
		return arr;
	}

	private JSONObject buildAdminMenu() {
		JSONObject role = new JSONObject();
		role.put("roleName", "管理员");
		role.put("tableName", "admin");
		role.put("pathName", "admin");
		role.put("hasBackLogin", "是");
		role.put("hasFrontLogin", "否");
		role.put("hasBackRegister", "否");
		role.put("hasFrontRegister", "否");
		JSONArray backMenu = new JSONArray();
		backMenu.add(buildGroup("车位管理", Arrays.asList(
				buildChild("车位信息", "parking_spot", Arrays.asList("新增", "查看", "修改", "删除"), "列表")
		)));
		backMenu.add(buildGroup("餐厅管理", Arrays.asList(
				buildChild("餐厅信息", "restaurant_info", Arrays.asList("新增", "查看", "修改", "删除", "查看评价"), "列表"),
				buildChild("餐厅预约", "restaurant_reservation", Arrays.asList("查看", "删除", "审核"), "列表")
		)));
		backMenu.add(buildGroup("菜品管理", Arrays.asList(
				buildChild("菜品信息", "dish_info", Arrays.asList("新增", "查看", "修改", "删除", "菜品类型", "查看评价", "首页统计", "下架", "上架"), "列表"),
				buildChild("菜品评价", "dish_review", Arrays.asList("查看", "修改", "删除"), "列表")
		)));
		backMenu.add(buildGroup("订单管理", Arrays.asList(
				buildChild("订单", "orders", Arrays.asList("查看", "修改", "删除", "月销额", "年销额", "品销量", "导出", "首页总数", "首页统计", "发货", "物流", "核销"), "")
		)));
		backMenu.add(buildGroup("人员管理", Arrays.asList(
				buildChild("管理员", "admin", Arrays.asList("新增", "查看", "修改", "删除"), "列表"),
				buildChild("用户", "user", Arrays.asList("新增", "查看", "修改", "删除"), "列表"),
				buildChild("员工", "staff", Arrays.asList("新增", "查看", "修改", "删除"), "列表")
		)));
		backMenu.add(buildGroup("客服管理", Arrays.asList(
				buildChild("客服会话", "support_ticket", Arrays.asList("查看", "修改"), "列表"),
				buildChild("FAQ知识库", "support_faq", Arrays.asList("新增", "查看", "修改", "删除"), "列表")
		)));
		backMenu.add(buildGroup("内容运营", Arrays.asList(
				buildChild("餐厅活动", "restaurant_event", Arrays.asList("新增", "查看", "修改", "删除"), "列表"),
				buildChild("系统公告", "system_notice", Arrays.asList("新增", "查看", "修改", "删除"), "列表"),
				buildChild("轮播图", "config", Arrays.asList("新增", "查看", "修改", "删除"), "列表")
		)));
		role.put("backMenu", backMenu);
		role.put("frontMenu", new JSONArray());
		return role;
	}

	private JSONObject buildUserMenu() {
		JSONObject role = new JSONObject();
		role.put("roleName", "用户");
		role.put("tableName", "user");
		role.put("pathName", "user");
		role.put("hasBackLogin", "否");
		role.put("hasFrontLogin", "是");
		role.put("hasBackRegister", "否");
		role.put("hasFrontRegister", "是");
		role.put("backMenu", new JSONArray());
		JSONArray frontMenu = new JSONArray();
		frontMenu.add(buildGroup("信息管理", Arrays.asList(
				buildChild("菜品信息", "dish_info", Arrays.asList("查看", "查看评价"), "列表")
		)));
		frontMenu.add(buildGroup("活动管理", Arrays.asList(
				buildChild("餐厅活动", "restaurant_event", Arrays.asList("查看"), "列表")
		)));
		frontMenu.add(buildGroup("购物车管理", Arrays.asList(
				buildChild("购物车", "cart", Arrays.asList("查看"), "列表")
		)));
		frontMenu.add(buildGroup("车位信息管理", Arrays.asList(
				buildChild("车位信息", "parking_spot", Arrays.asList("查看", "预约"), "列表")
		)));
		frontMenu.add(buildGroup("餐厅信息管理", Arrays.asList(
				buildChild("餐厅信息", "restaurant_info", Arrays.asList("查看", "查看评价", "预约"), "列表")
		)));
		role.put("frontMenu", frontMenu);
		return role;
	}

	private JSONObject buildStaffMenu() {
		JSONObject role = new JSONObject();
		role.put("roleName", "员工");
		role.put("tableName", "staff");
		role.put("pathName", "staff");
		role.put("hasBackLogin", "是");
		role.put("hasFrontLogin", "否");
		role.put("hasBackRegister", "否");
		role.put("hasFrontRegister", "否");
		JSONArray backMenu = new JSONArray();
		backMenu.add(buildGroup("订单管理", Arrays.asList(
				buildChild("订单", "orders", Arrays.asList("查看"), "")
		)));
		backMenu.add(buildGroup("预约管理", Arrays.asList(
				buildChild("餐厅预约", "restaurant_reservation", Arrays.asList("查看", "审核"), "列表")
		)));
		backMenu.add(buildGroup("信息查看", Arrays.asList(
				buildChild("餐厅信息", "restaurant_info", Arrays.asList("查看"), "列表"),
				buildChild("菜品信息", "dish_info", Arrays.asList("查看"), "列表"),
				buildChild("菜品评价", "dish_review", Arrays.asList("查看"), "列表")
		)));
		backMenu.add(buildGroup("内容运营", Arrays.asList(
				buildChild("餐厅活动", "restaurant_event", Arrays.asList("新增", "查看", "修改", "删除"), "列表"),
				buildChild("系统公告", "system_notice", Arrays.asList("新增", "查看", "修改", "删除"), "列表"),
				buildChild("轮播图", "config", Arrays.asList("新增", "查看", "修改", "删除"), "列表")
		)));
		role.put("backMenu", backMenu);
		role.put("frontMenu", new JSONArray());
		return role;
	}

	private JSONObject buildGroup(String name, List<JSONObject> children) {
		JSONObject group = new JSONObject();
		group.put("menu", name);
		JSONArray child = new JSONArray();
		child.addAll(children);
		group.put("child", child);
		return group;
	}

	private JSONObject buildChild(String name, String tableName, List<String> buttons, String menuJump) {
		JSONObject child = new JSONObject();
		child.put("menu", name);
		child.put("tableName", tableName);
		child.put("classname", tableName);
		child.put("menuJump", menuJump);
		JSONArray btns = new JSONArray();
		btns.addAll(buttons);
		child.put("buttons", btns);
		JSONArray allBtns = new JSONArray();
		allBtns.addAll(buttons);
		child.put("allButtons", allBtns);
		return child;
	}

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( MenuEntity menu){
        return R.ok().put("data", buildDefaultMenuViews());
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(MenuEntity menu){
		return R.ok("查询菜单成功").put("data", buildDefaultMenuView());
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        return R.ok().put("data", buildDefaultMenuEntity());
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        return R.ok().put("data", buildDefaultMenuEntity());
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MenuEntity menu, HttpServletRequest request){
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody MenuEntity menu, HttpServletRequest request){
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MenuEntity menu, HttpServletRequest request){
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        return R.ok();
    }
    
	









}
