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

import com.cl.entity.RestaurantReservationEntity;
import com.cl.entity.RestaurantInfoEntity;
import com.cl.entity.view.RestaurantReservationView;

import com.cl.service.RestaurantReservationService;
import com.cl.service.RestaurantInfoService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;
import com.cl.service.FavoritesService;
import com.cl.entity.FavoritesEntity;
import com.cl.service.UserService;
import com.cl.entity.UserEntity;
import com.cl.entity.ConfigEntity;
import com.cl.service.ConfigService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 餐厅预约
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/restaurant_reservation")
public class RestaurantReservationController {
    @Autowired
    private RestaurantReservationService restaurantReservationService;
    
    @Autowired
    private RestaurantInfoService restaurantInfoService;

    @Autowired
    private FavoritesService storeupService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    private static final String RESERVATION_TIME_SLOTS_CONFIG = "reservation_time_slots";
    private static final String RESERVATION_TIME_SLOTS_BY_DATE_CONFIG = "reservation_time_slots_by_date";
    private static final int MAX_RESERVATION_DAYS_AHEAD = 30;








    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,RestaurantReservationEntity restaurantReservation,HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if("user".equals(tableName)) {
            restaurantReservation.setLoginName((String)request.getSession().getAttribute("username"));
        }
            EntityWrapper<RestaurantReservationEntity> ew = new EntityWrapper<RestaurantReservationEntity>();
        
        PageUtils page = restaurantReservationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, restaurantReservation), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,RestaurantReservationEntity restaurantReservation,
		HttpServletRequest request){
        EntityWrapper<RestaurantReservationEntity> ew = new EntityWrapper<RestaurantReservationEntity>();

		PageUtils page = restaurantReservationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, restaurantReservation), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( RestaurantReservationEntity restaurantReservation){
       	EntityWrapper<RestaurantReservationEntity> ew = new EntityWrapper<RestaurantReservationEntity>();
      	ew.allEq(MPUtil.allEQMapPre( restaurantReservation, "restaurant_reservation")); 
        return R.ok().put("data", restaurantReservationService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(RestaurantReservationEntity restaurantReservation){
        EntityWrapper< RestaurantReservationEntity> ew = new EntityWrapper< RestaurantReservationEntity>();
 		ew.allEq(MPUtil.allEQMapPre( restaurantReservation, "restaurant_reservation")); 
		RestaurantReservationView restaurantReservationView =  restaurantReservationService.selectView(ew);
		return R.ok("查询餐厅预约成功").put("data", restaurantReservationView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RestaurantReservationEntity restaurantReservation = restaurantReservationService.selectById(id);
		restaurantReservation = restaurantReservationService.selectView(new EntityWrapper<RestaurantReservationEntity>().eq("id", id));
        return R.ok().put("data", restaurantReservation);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RestaurantReservationEntity restaurantReservation = restaurantReservationService.selectById(id);
		restaurantReservation = restaurantReservationService.selectView(new EntityWrapper<RestaurantReservationEntity>().eq("id", id));
        return R.ok().put("data", restaurantReservation);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RestaurantReservationEntity restaurantReservation, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(restaurantReservation);
        if(StringUtils.isBlank(restaurantReservation.getSeatName())) {
            return R.error("餐桌名称不能为空");
        }
        if(restaurantReservation.getReservationTime() == null) {
            return R.error("预约时间不能为空");
        }
        String dateError = validateReservationDate(restaurantReservation.getReservationTime());
        if(dateError != null) {
            return R.error(dateError);
        }
        if(!isReservationTimeAllowed(restaurantReservation.getReservationTime())) {
            return R.error("该预约时段不可预约");
        }
        RestaurantInfoEntity restaurantInfo = restaurantInfoService.selectOne(new EntityWrapper<RestaurantInfoEntity>().eq("seat_name", restaurantReservation.getSeatName()));
        if(restaurantInfo != null) {
            String tableStatus = restaurantInfo.getTableStatus();
            if("禁止预约".equals(tableStatus) || "已预约".equals(tableStatus)) {
                return R.error("该餐桌已禁止预约");
            }
        }
        // 设置定金为50元
        restaurantReservation.setDeposit(50.0);
        if("已支付".equals(restaurantReservation.getPaymentStatus())) {
            restaurantReservation.setVerificationStatus("未核销");
        } else {
            restaurantReservation.setPaymentStatus("未支付");
            restaurantReservation.setVerificationStatus("未核销");
        }
        boolean ok = restaurantReservationService.insert(restaurantReservation);
        if(!ok) {
            return R.error("保存失败");
        }
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @Transactional
    public R add(@RequestBody RestaurantReservationEntity restaurantReservation, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(restaurantReservation);
        if(StringUtils.isBlank(restaurantReservation.getSeatName())) {
            return R.error("餐桌名称不能为空");
        }
        if(restaurantReservation.getReservationTime() == null) {
            return R.error("预约时间不能为空");
        }
        String dateError = validateReservationDate(restaurantReservation.getReservationTime());
        if(dateError != null) {
            return R.error(dateError);
        }
        if(!isReservationTimeAllowed(restaurantReservation.getReservationTime())) {
            return R.error("该预约时段不可预约");
        }
        RestaurantInfoEntity restaurantInfo = restaurantInfoService.selectOne(new EntityWrapper<RestaurantInfoEntity>().eq("seat_name", restaurantReservation.getSeatName()));
        if(restaurantInfo != null) {
            String tableStatus = restaurantInfo.getTableStatus();
            if("禁止预约".equals(tableStatus) || "已预约".equals(tableStatus)) {
                return R.error("该餐桌已禁止预约");
            }
        }
        
        // 只有在支付成功后才检查冲突（避免未支付的预约占用时段）
        if("已支付".equals(restaurantReservation.getPaymentStatus())) {
            // 检查该餐桌在该时间段是否已被预约
            EntityWrapper<RestaurantReservationEntity> wrapper = new EntityWrapper<RestaurantReservationEntity>();
            wrapper.eq("seat_name", restaurantReservation.getSeatName());
            wrapper.eq("reservation_time", restaurantReservation.getReservationTime());
            wrapper.eq("payment_status", "已支付"); // 只检查已支付的预约
            wrapper.eq("verification_status", "未核销"); // 只检查未核销的预约
            
            int count = restaurantReservationService.selectCount(wrapper);
            
            if(count > 0) {
                return R.error("该餐桌在此时间段已被预约，请选择其他时间段");
            }
        }
        
        // 设置定金为50元
        restaurantReservation.setDeposit(50.0);
        // 如果支付状态为已支付，扣除用户余额
        if("已支付".equals(restaurantReservation.getPaymentStatus())) {
            // 查询用户信息
            UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("login_name", restaurantReservation.getLoginName()));
            if(user == null) {
                return R.error("用户不存在");
            }
            // 检查余额是否足够
            if(user.getBalance() == null || user.getBalance() < 50.0) {
                return R.error("账户余额不足，请先充值");
            }
            // 扣除定金，使用 BigDecimal 确保精度
            double currentMoney = user.getBalance();
            double newMoney = Math.round((currentMoney - 50.0) * 100.0) / 100.0; // 保留2位小数
            user.setBalance(newMoney);
            userService.updateById(user);
            restaurantReservation.setVerificationStatus("未核销");
        } else {
            restaurantReservation.setPaymentStatus("未支付");
            restaurantReservation.setVerificationStatus("未核销");
        }
        boolean ok = restaurantReservationService.insert(restaurantReservation);
        if(!ok) {
            return R.error("保存失败");
        }
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RestaurantReservationEntity restaurantReservation, HttpServletRequest request){
        //ValidatorUtils.validateEntity(restaurantReservation);
        if(restaurantReservation.getId() == null) {
            return R.error("id不能为空");
        }
        if(restaurantReservation.getReservationTime() != null) {
            String dateError = validateReservationDate(restaurantReservation.getReservationTime());
            if(dateError != null) {
                return R.error(dateError);
            }
            if(!isReservationTimeAllowed(restaurantReservation.getReservationTime())) {
                return R.error("该预约时段不可预约");
            }
        }
        boolean ok = restaurantReservationService.updateById(restaurantReservation);//全部更新
        if(!ok) {
            return R.error("更新失败");
        }
        return R.ok();
    }

    @IgnoreAuth
    @GetMapping("/time_slots")
    public R timeSlots(@RequestParam(required = false) String date) {
        List<Integer> hours = getAllowedSlotHours(date);
        List<String> slots = new ArrayList<>();
        for(Integer h : hours) {
            if(h == null) continue;
            slots.add(formatHour(h));
        }
        return R.ok().put("data", slots);
    }

    @PostMapping("/time_slots/update")
    public R updateTimeSlots(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Object roleObj = request.getSession().getAttribute("role");
        if(roleObj == null) {
            return R.error(401, "未登录或登录过期");
        }
        String role = String.valueOf(roleObj);
        if(!"管理员".equals(role)) {
            return R.error(403, "无权限操作");
        }

        String date = body == null ? null : String.valueOf(body.get("date"));
        if(StringUtils.isBlank(date) || "null".equalsIgnoreCase(date)) {
            date = null;
        } else if(!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return R.error("日期格式错误");
        }

        Object slotsObj = body == null ? null : body.get("slots");
        List<Integer> hours = parseSlotHours(slotsObj == null ? null : String.valueOf(slotsObj), slotsObj);
        if(hours == null) {
            hours = new ArrayList<>();
        }
        if(date == null && hours.isEmpty()) {
            return R.error("预约时段不能为空");
        }
        if(!hours.isEmpty()) {
            Collections.sort(hours);
        }

        boolean clear = false;
        if(date != null && body != null) {
            Object clearObj = body.get("clear");
            if(clearObj instanceof Boolean) {
                clear = (Boolean) clearObj;
            } else if(clearObj != null) {
                clear = "true".equalsIgnoreCase(String.valueOf(clearObj));
            }
        }

        boolean ok;
        if(date == null) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < hours.size(); i++) {
                if(i > 0) sb.append(",");
                sb.append(formatHour(hours.get(i)));
            }

            ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", RESERVATION_TIME_SLOTS_CONFIG));
            if(configEntity == null) {
                configEntity = new ConfigEntity();
                configEntity.setName(RESERVATION_TIME_SLOTS_CONFIG);
            }
            configEntity.setValue(sb.toString());
            ok = configService.insertOrUpdate(configEntity);
        } else {
            if(clear) {
                ok = removeDateSlotOverride(date);
            } else {
                ok = upsertDateSlotOverride(date, hours);
            }
        }
        if(!ok) {
            return R.error("保存失败");
        }
        return R.ok();
    }

    private boolean isReservationTimeAllowed(Date reservationTime) {
        int hour = getHourOfDay(reservationTime);
        if(hour < 0) return false;
        String date = formatYmd(reservationTime);
        List<Integer> hours = getAllowedSlotHours(date);
        return hours.contains(hour);
    }

    private boolean isReservationDateAllowed(Date reservationTime) {
        return validateReservationDate(reservationTime) == null;
    }

    private String validateReservationDate(Date reservationTime) {
        if(reservationTime == null) return "预约时间不能为空";
        Date todayStart = getStartOfToday();
        Date reservationDayStart = getStartOfDay(reservationTime);
        if(reservationDayStart == null) return "预约时间不能为空";
        if(reservationDayStart.before(todayStart)) {
            return "预约日期不能早于今天";
        }
        Date maxDayStart = getStartOfMaxReservationDay();
        if(maxDayStart != null && reservationDayStart.after(maxDayStart)) {
            return "预约日期只能预约未来" + MAX_RESERVATION_DAYS_AHEAD + "天内";
        }
        return null;
    }

    private Date getStartOfMaxReservationDay() {
        Date todayStart = getStartOfToday();
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayStart);
        cal.add(Calendar.DAY_OF_YEAR, MAX_RESERVATION_DAYS_AHEAD);
        return cal.getTime();
    }

    private int getHourOfDay(Date date) {
        if(date == null) return -1;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    private List<Integer> getAllowedSlotHours(String date) {
        if(!StringUtils.isBlank(date)) {
            List<Integer> override = getDateSlotOverride(date);
            if(override != null) {
                Collections.sort(override);
                return override;
            }
        }

        ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", RESERVATION_TIME_SLOTS_CONFIG));
        String raw = configEntity == null ? null : configEntity.getValue();
        List<Integer> parsed = parseSlotHours(raw, null);
        if(parsed != null && !parsed.isEmpty()) {
            Collections.sort(parsed);
            return parsed;
        }

        List<Integer> defaults = new ArrayList<>();
        for(int i = 9; i <= 19; i++) {
            defaults.add(i);
        }
        return defaults;
    }

    private boolean upsertDateSlotOverride(String date, List<Integer> hours) {
        JSONObject all = readDateSlotOverridesJson();
        JSONArray arr = new JSONArray();
        for(Integer h : hours) {
            if(h == null) continue;
            arr.add(formatHour(h));
        }
        all.put(date, arr);

        ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", RESERVATION_TIME_SLOTS_BY_DATE_CONFIG));
        if(configEntity == null) {
            configEntity = new ConfigEntity();
            configEntity.setName(RESERVATION_TIME_SLOTS_BY_DATE_CONFIG);
        }
        configEntity.setValue(all.toJSONString());
        return configService.insertOrUpdate(configEntity);
    }

    private boolean removeDateSlotOverride(String date) {
        if(StringUtils.isBlank(date)) return false;
        JSONObject all = readDateSlotOverridesJson();
        all.remove(date);
        ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", RESERVATION_TIME_SLOTS_BY_DATE_CONFIG));
        if(configEntity == null) {
            configEntity = new ConfigEntity();
            configEntity.setName(RESERVATION_TIME_SLOTS_BY_DATE_CONFIG);
        }
        configEntity.setValue(all.toJSONString());
        return configService.insertOrUpdate(configEntity);
    }

    private List<Integer> getDateSlotOverride(String date) {
        if(StringUtils.isBlank(date)) return null;
        JSONObject all = readDateSlotOverridesJson();
        if(all == null || all.isEmpty()) return null;
        Object v = all.get(date);
        if(v == null) return null;
        if(v instanceof JSONArray) {
            return parseSlotHours(((JSONArray) v).toJSONString(), (JSONArray) v);
        }
        return parseSlotHours(String.valueOf(v), null);
    }

    private JSONObject readDateSlotOverridesJson() {
        ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", RESERVATION_TIME_SLOTS_BY_DATE_CONFIG));
        String raw = configEntity == null ? null : configEntity.getValue();
        if(StringUtils.isBlank(raw)) {
            return new JSONObject();
        }
        try {
            JSONObject obj = JSONObject.parseObject(raw);
            return obj == null ? new JSONObject() : obj;
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private String formatYmd(Date date) {
        if(date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private Date getStartOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getStartOfDay(Date date) {
        if(date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private List<Integer> parseSlotHours(String raw, Object original) {
        List<Integer> hours = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        if(original instanceof List) {
            List list = (List) original;
            for(Object o : list) {
                addHourIfValid(String.valueOf(o), hours, seen);
            }
            return hours;
        }

        if(StringUtils.isBlank(raw)) {
            return hours;
        }

        String s = raw.trim();
        if(s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1);
        }
        s = s.replace("\"", "").replace("'", "");
        String[] parts = s.split("[,，\\s]+");
        for(String part : parts) {
            addHourIfValid(part, hours, seen);
        }
        return hours;
    }

    private void addHourIfValid(String text, List<Integer> hours, Set<Integer> seen) {
        if(StringUtils.isBlank(text)) return;
        String t = text.trim();
        if(t.contains(":")) {
            t = t.split(":")[0];
        }
        if(!t.matches("^\\d{1,2}$")) return;
        int h = Integer.parseInt(t);
        if(h < 0 || h > 23) return;
        if(seen.add(h)) {
            hours.add(h);
        }
    }

    private String formatHour(Integer hour) {
        if(hour == null) return "";
        if(hour < 10) return "0" + hour;
        return String.valueOf(hour);
    }

    


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        restaurantReservationService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 核销退费
     */
    @RequestMapping("/hexiao")
    @Transactional
    public R hexiao(@RequestBody Long[] ids){
        List<RestaurantReservationEntity> list = new ArrayList<RestaurantReservationEntity>();
        double totalRefund = 0;
        for(Long id : ids) {
            RestaurantReservationEntity restaurantReservation = restaurantReservationService.selectById(id);
            if("已支付".equals(restaurantReservation.getPaymentStatus()) && "未核销".equals(restaurantReservation.getVerificationStatus())) {
                // 查询用户信息
                UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("login_name", restaurantReservation.getLoginName()));
                if(user != null) {
                    // 退还定金到用户账户，使用精确计算
                    double currentMoney = user.getBalance() == null ? 0 : user.getBalance();
                    double newMoney = Math.round((currentMoney + 50.0) * 100.0) / 100.0; // 保留2位小数
                    user.setBalance(newMoney);
                    userService.updateById(user);
                    totalRefund += 50.0;
                }
                
                restaurantReservation.setVerificationStatus("已核销");
                restaurantReservation.setPaymentStatus("已退款");
                list.add(restaurantReservation);
            }
        }
        if(list.size() > 0) {
            restaurantReservationService.updateBatchById(list);
            return R.ok().put("message", "核销退费成功，共退还" + totalRefund + "元到用户账户");
        } else {
            return R.error("没有可核销的预约");
        }
    }
	











}
