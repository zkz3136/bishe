package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import com.cl.entity.MenuEntity;
import com.cl.entity.view.MenuView;

import com.cl.service.MenuService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

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
    @Autowired
    private MenuService menuService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,MenuEntity menu,
                        HttpServletRequest request){
                                    EntityWrapper<MenuEntity> ew = new EntityWrapper<MenuEntity>();
                            
        
        
        PageUtils page = menuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, menu), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,MenuEntity menu,
		HttpServletRequest request){
        EntityWrapper<MenuEntity> ew = new EntityWrapper<MenuEntity>();

		PageUtils page = menuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, menu), params), params));
		if (page != null && page.getList() != null) {
			for (Object o : page.getList()) {
				if (!(o instanceof MenuEntity)) {
					continue;
				}
				MenuEntity m = (MenuEntity) o;
				String menujson = m.getMenujson();
				if (menujson == null || menujson.isEmpty()) {
					continue;
				}
				JSONArray menuArray;
				try {
					menuArray = JSONArray.parseArray(menujson);
				} catch (Exception e) {
					continue;
				}
				if (menuArray == null) {
					continue;
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
				m.setMenujson(menuArray.toJSONString());
			}
		}
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
					if ("餐厅信息评论".equals(menuName) || "restaurant_review".equals(tableName) || "restaurant_review".equals(className)
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
		removeStringFromJsonArray(obj, "buttons", "查看评论");
		removeStringFromJsonArray(obj, "allButtons", "查看评论");
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
		map.put("车位信息", "parking_spot");
		map.put("购物车", "cart");
		map.put("我的收藏", "favorites");
		map.put("订单", "orders");
		map.put("已支付订单", "orders");
		map.put("轮播图", "config");
		return map;
	}

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( MenuEntity menu){
       	EntityWrapper<MenuEntity> ew = new EntityWrapper<MenuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( menu, "menu")); 
		List<MenuView> list = menuService.selectListView(ew);
		if (list != null) {
			Map<String, String> menuNameToPath = buildMenuNameToPathMap();
			for (MenuView m : list) {
				if (m == null) {
					continue;
				}
				String menujson = m.getMenujson();
				if (menujson == null || menujson.isEmpty()) {
					continue;
				}
				JSONArray menuArray;
				try {
					menuArray = JSONArray.parseArray(menujson);
				} catch (Exception e) {
					continue;
				}
				if (menuArray == null) {
					continue;
				}
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
				m.setMenujson(menuArray.toJSONString());
			}
		}
        return R.ok().put("data", list);
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(MenuEntity menu){
        EntityWrapper< MenuEntity> ew = new EntityWrapper< MenuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( menu, "menu")); 
		MenuView menuView =  menuService.selectView(ew);
		return R.ok("查询菜单成功").put("data", menuView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        MenuEntity menu = menuService.selectById(id);
		menu = menuService.selectView(new EntityWrapper<MenuEntity>().eq("id", id));
        return R.ok().put("data", menu);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        MenuEntity menu = menuService.selectById(id);
		menu = menuService.selectView(new EntityWrapper<MenuEntity>().eq("id", id));
        return R.ok().put("data", menu);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MenuEntity menu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(menu);
        menuService.insert(menu);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody MenuEntity menu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(menu);
        menuService.insert(menu);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody MenuEntity menu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(menu);
        menuService.updateById(menu);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        menuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
