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
import com.cl.entity.OrdersEntity;
import com.cl.service.OrdersService;

import com.cl.entity.DishInfoEntity;
import com.cl.entity.view.DishInfoView;

import com.cl.service.DishInfoService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;
import com.cl.service.FavoritesService;
import com.cl.entity.FavoritesEntity;

/**
 * 美食信息
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:57
 */
@RestController
@RequestMapping("/dish_info")
public class DishInfoController {
    @Autowired
    private DishInfoService dishInfoService;

    @Autowired
    private FavoritesService storeupService;

    @Autowired
    private OrdersService ordersService;







    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DishInfoEntity dishInfo,
                                                                                                                                                                                                                           HttpServletRequest request){
                    Object tableObj = request.getSession().getAttribute("tableName");
                    if(tableObj == null){
                        return R.error(401, "未登录或登录过期");
                    }
                    String tableName = tableObj.toString();
                                                                                                                                                                                                                                                                                                                                                                                           // 删除员工字段后，无需权限过滤
                                                                                            EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
                                                                                                                                                                                                                                                                                                                                                        
        
        
        PageUtils page = dishInfoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dishInfo), params), params));
        return R.ok().put("data", page);
    }








    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DishInfoEntity dishInfo,
		HttpServletRequest request){
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();

		PageUtils page = dishInfoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dishInfo), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DishInfoEntity dishInfo){
       	EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
      	ew.allEq(MPUtil.allEQMapPre( dishInfo, "dish_info")); 
        return R.ok().put("data", dishInfoService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DishInfoEntity dishInfo){
        EntityWrapper< DishInfoEntity> ew = new EntityWrapper< DishInfoEntity>();
 		ew.allEq(MPUtil.allEQMapPre( dishInfo, "dish_info")); 
		DishInfoView dishInfoView =  dishInfoService.selectView(ew);
		return R.ok("查询美食信息成功").put("data", dishInfoView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DishInfoEntity dishInfo = dishInfoService.selectById(id);
		dishInfo.setClickTime(new Date());
		dishInfoService.updateById(dishInfo);
		dishInfo = dishInfoService.selectView(new EntityWrapper<DishInfoEntity>().eq("id", id));
        return R.ok().put("data", dishInfo);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DishInfoEntity dishInfo = dishInfoService.selectById(id);
		dishInfo.setClickTime(new Date());
		dishInfoService.updateById(dishInfo);
		dishInfo = dishInfoService.selectView(new EntityWrapper<DishInfoEntity>().eq("id", id));
        return R.ok().put("data", dishInfo);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DishInfoEntity dishInfo, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(dishInfo);
        dishInfoService.insert(dishInfo);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DishInfoEntity dishInfo, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(dishInfo);
        dishInfoService.insert(dishInfo);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DishInfoEntity dishInfo, HttpServletRequest request){
        //ValidatorUtils.validateEntity(dishInfo);
        dishInfoService.updateById(dishInfo);//全部更新
        return R.ok();
    }

	@PostMapping("/dish_category/rename")
	@Transactional
	public R renameDishCategory(@RequestBody Map<String, Object> body, HttpServletRequest request) {
		Object tableObj = request.getSession().getAttribute("tableName");
		if (tableObj == null) {
			return R.error(401, "未登录或登录过期");
		}
		String oldCategory = body == null ? null : String.valueOf(body.get("oldCategory"));
		String newCategory = body == null ? null : String.valueOf(body.get("newCategory"));
		oldCategory = oldCategory == null ? null : oldCategory.trim();
		newCategory = newCategory == null ? null : newCategory.trim();
		if (oldCategory == null || oldCategory.isEmpty() || newCategory == null || newCategory.isEmpty()) {
			return R.error("参数不能为空");
		}
		if (oldCategory.equals(newCategory)) {
			return R.error("新旧类型相同");
		}
		int updated = dishInfoService.renameDishCategory(oldCategory, newCategory);
		return R.ok().put("data", updated);
	}




    

    /**
     * 删除
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        dishInfoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,DishInfoEntity dishInfo, HttpServletRequest request,String pre){
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "click_time");
        params.put("order", "desc");
		PageUtils page = dishInfoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dishInfo), params), params));
        return R.ok().put("data", page);
    }

        /**
     * 按用户购买推荐
     */
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params,DishInfoEntity dishInfo, HttpServletRequest request){
        Object userIdObj = request.getSession().getAttribute("userId");
        if(userIdObj == null){
            return R.error(401, "未登录或登录过期");
        }
        String userId = userIdObj.toString();
        String goodtypeColumn = "dish_category";
        List<OrdersEntity> orders = ordersService.selectList(new EntityWrapper<OrdersEntity>().eq("userid", userId).eq("tablename", "dish_info").orderBy("addtime", false));
        List<String> goodtypes = new ArrayList<String>();
        Integer limit = params.get("limit")==null?10:Integer.parseInt(params.get("limit").toString());
        List<DishInfoEntity> dishInfoList = new ArrayList<DishInfoEntity>();
	//去重
    	List<OrdersEntity> ordersDist = new ArrayList<OrdersEntity>();
    	for(OrdersEntity o1 : orders) {
    		boolean addFlag = true;
    		for(OrdersEntity o2 : ordersDist) {
    			if(o1.getGoodid()==o2.getGoodid() || o1.getGoodtype().equals(o2.getGoodtype())) {
    				addFlag = false;
    				break;
    			}
    		}
    		if(addFlag) ordersDist.add(o1);
    	}
        if(ordersDist!=null && ordersDist.size()>0) {
                for(OrdersEntity o : ordersDist) {
                        dishInfoList.addAll(dishInfoService.selectList(new EntityWrapper<DishInfoEntity>().eq(goodtypeColumn, o.getGoodtype())));
                }
        }
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
        params.put("sort", "id");
        params.put("order", "desc");
        PageUtils page = dishInfoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dishInfo), params), params));
        List<DishInfoEntity> pageList = (List<DishInfoEntity>)page.getList();
        if(dishInfoList.size()<limit) {
                int toAddNum = (limit-dishInfoList.size())<=pageList.size()?(limit-dishInfoList.size()):pageList.size();
                for(DishInfoEntity o1 : pageList) {
                    boolean addFlag = true;
                    for(DishInfoEntity o2 : dishInfoList) {
                        if(o1.getId().intValue()==o2.getId().intValue()) {
                            addFlag = false;
                            break;
                        }
                    }
                    if(addFlag) {
                        dishInfoList.add(o1);
                        if(--toAddNum==0) break;
                    }   
                }
        } else if(dishInfoList.size()>limit) {
            dishInfoList = dishInfoList.subList(0, limit);
        }
        page.setList(dishInfoList);
        return R.ok().put("data", page);
    }




    /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
		Object tableObj = request.getSession().getAttribute("tableName");
        if(tableObj == null){
            return R.error(401, "未登录或登录过期");
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(dishInfoService.selectValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }

        Collections.sort(result, (map1, map2) -> {
            // 假设 total 总是存在并且是数值类型
            Number total1 = (Number) map1.get("total");
            Number total2 = (Number) map2.get("total");
            if(total1==null)
            {
                total1 = 0;
            }
            if(total2==null)
            {
                total2 = 0;
            }
            String order = request.getParameter("order");
            if(StringUtils.isNotBlank(order)&&order.equals("desc")){
                return Double.compare(total2.doubleValue(), total1.doubleValue());
            }
            return Double.compare(total1.doubleValue(), total2.doubleValue());
        });

        return R.ok().put("data", result);
    }

    /**
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul, HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
        Object tableObj = request.getSession().getAttribute("tableName");
        if(tableObj == null){
            return R.error(401, "未登录或登录过期");
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(dishInfoService.selectValue(params, ew));
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        params.put("timeStatType", timeStatType);
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
        Object tableObj = request.getSession().getAttribute("tableName");
        if(tableObj == null){
            return R.error(401, "未登录或登录过期");
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(dishInfoService.selectTimeStatValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,@RequestParam String yColumnNameMul,HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
        Object tableObj = request.getSession().getAttribute("tableName");
        if(tableObj == null){
            return R.error(401, "未登录或登录过期");
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(dishInfoService.selectTimeStatValue(params, ew));
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", MPUtil.camelToSnake(columnName));
        EntityWrapper<DishInfoEntity> ew = new EntityWrapper<DishInfoEntity>();
        Object tableObj = request.getSession().getAttribute("tableName");
        if(tableObj == null){
            return R.error(401, "未登录或登录过期");
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(dishInfoService.selectGroup(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }







}
