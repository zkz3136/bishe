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
        Object seatNameObj = params.get("seatName");
        if(seatNameObj == null) {
            seatNameObj = params.get("seat_name");
        }
        if(seatNameObj != null && StringUtils.isNotBlank(String.valueOf(seatNameObj))) {
            String keyword = String.valueOf(seatNameObj).trim();
            if(StringUtils.isNotBlank(keyword)) {
                ew.like("seat_name", keyword);
                restaurantReservation.setSeatName(null);
            }
        }
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
		restaurantReservation = restaurantReservationService.selectView(new EntityWrapper<RestaurantReservationEntity>().eq("restaurantReservation.id", id));
        return R.ok().put("data", restaurantReservation);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RestaurantReservationEntity restaurantReservation = restaurantReservationService.selectById(id);
		restaurantReservation = restaurantReservationService.selectView(new EntityWrapper<RestaurantReservationEntity>().eq("restaurantReservation.id", id));
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
            Object roleObj = request.getSession().getAttribute("role");
            String role = roleObj == null ? "" : String.valueOf(roleObj);
            if (!(dateError.contains("预约日期只能预约未来") && ("管理员".equals(role) || "员工".equals(role)))) {
                return R.error(dateError);
            }
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
        if(hasTimeSlotConflict(restaurantReservation.getSeatName(), restaurantReservation.getReservationTime(), null)) {
            return R.error("该餐桌在此时间段已被预约，请选择其他时间段");
        }
        // 订金金额：优先取餐位设置，其次取传入值，最后默认50
        Double depositVal = null;
        try {
            RestaurantInfoEntity info = restaurantInfoService.selectOne(new EntityWrapper<RestaurantInfoEntity>().eq("seat_name", restaurantReservation.getSeatName()));
            if (info != null && info.getDeposit() != null) {
                depositVal = info.getDeposit();
            }
        } catch (Exception ignore) {}
        if (depositVal == null && restaurantReservation.getDeposit() != null) {
            depositVal = restaurantReservation.getDeposit();
        }
        if (depositVal == null) {
            depositVal = 50.0;
        }
        restaurantReservation.setDeposit(depositVal);
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
            Object roleObj = request.getSession().getAttribute("role");
            String role = roleObj == null ? "" : String.valueOf(roleObj);
            if (!(dateError.contains("预约日期只能预约未来") && ("管理员".equals(role) || "员工".equals(role)))) {
                return R.error(dateError);
            }
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
        if(hasTimeSlotConflict(restaurantReservation.getSeatName(), restaurantReservation.getReservationTime(), null)) {
            return R.error("该餐桌在此时间段已被预约，请选择其他时间段");
        }
        
        // 订金金额：优先取餐位设置，其次取传入值，最后默认50
        Double depositVal2 = null;
        try {
            RestaurantInfoEntity info2 = restaurantInfoService.selectOne(new EntityWrapper<RestaurantInfoEntity>().eq("seat_name", restaurantReservation.getSeatName()));
            if (info2 != null && info2.getDeposit() != null) {
                depositVal2 = info2.getDeposit();
            }
        } catch (Exception ignore) {}
        if (depositVal2 == null && restaurantReservation.getDeposit() != null) {
            depositVal2 = restaurantReservation.getDeposit();
        }
        if (depositVal2 == null) {
            depositVal2 = 50.0;
        }
        restaurantReservation.setDeposit(depositVal2);
        // 如果支付状态为已支付，扣除用户余额
        if("已支付".equals(restaurantReservation.getPaymentStatus())) {
            // 查询用户信息
            UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("login_name", restaurantReservation.getLoginName()));
            if(user == null) {
                return R.error("用户不存在");
            }
            // 检查余额是否足够
            if(user.getBalance() == null || user.getBalance() < depositVal2) {
                return R.error("账户余额不足，请先充值");
            }
            // 扣除定金，使用 BigDecimal 确保精度
            double currentMoney = user.getBalance();
            double newMoney = Math.round((currentMoney - depositVal2) * 100.0) / 100.0; // 保留2位小数
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
        RestaurantReservationEntity db = restaurantReservationService.selectById(restaurantReservation.getId());
        if(db == null) {
            return R.error("数据不存在");
        }
        if(restaurantReservation.getReservationTime() != null) {
            String dateError = validateReservationDate(restaurantReservation.getReservationTime());
            if(dateError != null) {
                Object roleObj = request.getSession().getAttribute("role");
                String role = roleObj == null ? "" : String.valueOf(roleObj);
                if (!(dateError.contains("预约日期只能预约未来") && ("管理员".equals(role) || "员工".equals(role)))) {
                    return R.error(dateError);
                }
            }
            if(!isReservationTimeAllowed(restaurantReservation.getReservationTime())) {
                return R.error("该预约时段不可预约");
            }
        }
        String seatName = StringUtils.isBlank(restaurantReservation.getSeatName()) ? db.getSeatName() : restaurantReservation.getSeatName();
        Date reservationTime = restaurantReservation.getReservationTime() == null ? db.getReservationTime() : restaurantReservation.getReservationTime();
        if(!StringUtils.isBlank(seatName) && reservationTime != null) {
            if(hasTimeSlotConflict(seatName, reservationTime, restaurantReservation.getId())) {
                return R.error("该餐桌在此时间段已被预约，请选择其他时间段");
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
        if(!StringUtils.isBlank(date)) {
            Date now = new Date();
            String today = formatYmd(now);
            if(date.equals(today)) {
                int currentHour = getHourOfDay(now);
                List<Integer> filtered = new ArrayList<>();
                for(Integer h : hours) {
                    if(h == null) continue;
                    if(h > currentHour) {
                        filtered.add(h);
                    }
                }
                hours = filtered;
            }
        }
        List<String> slots = new ArrayList<>();
        for(Integer h : hours) {
            if(h == null) continue;
            slots.add(formatHour(h));
        }
        return R.ok().put("data", slots);
    }

    @IgnoreAuth
    @GetMapping("/booked_hours")
    public R bookedHours(@RequestParam String date, @RequestParam(required = false) String seatName, @RequestParam(required = false) String seat_name) {
        String seat = StringUtils.isBlank(seatName) ? seat_name : seatName;
        if(StringUtils.isBlank(date) || !date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return R.error("日期格式错误");
        }
        if(StringUtils.isBlank(seat)) {
            return R.ok().put("data", new ArrayList<>());
        }
        Date dayStart;
        Date dayEnd;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date base = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(base);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            dayStart = cal.getTime();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            dayEnd = cal.getTime();
        } catch (ParseException e) {
            return R.error("日期格式错误");
        }

        EntityWrapper<RestaurantReservationEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("seat_name", seat);
        wrapper.between("reservation_time", dayStart, dayEnd);
        wrapper.andNew().isNull("payment_status").or().ne("payment_status", "已退款");
        wrapper.andNew().isNull("verification_status").or().ne("verification_status", "已核销");
        List<RestaurantReservationEntity> list = restaurantReservationService.selectList(wrapper);
        Set<String> hours = new HashSet<>();
        if(list != null) {
            for(RestaurantReservationEntity e : list) {
                Date rt = e == null ? null : e.getReservationTime();
                if(rt == null) continue;
                int h = getHourOfDay(rt);
                if(h >= 0 && h <= 23) {
                    hours.add(formatHour(h));
                }
            }
        }
        List<String> out = new ArrayList<>(hours);
        Collections.sort(out);
        return R.ok().put("data", out);
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
        if(reservationDayStart.compareTo(todayStart) == 0) {
            Date now = new Date();
            if(reservationTime.before(now)) {
                return "预约时间不能早于当前时间";
            }
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
        for(int i = 0; i <= 23; i++) {
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

    private boolean hasTimeSlotConflict(String seatName, Date reservationTime, Long excludeId) {
        if(StringUtils.isBlank(seatName) || reservationTime == null) return false;
        EntityWrapper<RestaurantReservationEntity> wrapper = new EntityWrapper<RestaurantReservationEntity>();
        wrapper.eq("seat_name", seatName);
        wrapper.eq("reservation_time", reservationTime);
        if(excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        wrapper.andNew().isNull("payment_status").or().ne("payment_status", "已退款");
        wrapper.andNew().isNull("verification_status").or().ne("verification_status", "已核销");
        return restaurantReservationService.selectCount(wrapper) > 0;
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
     * 预约定金统计（按时间）
     * type: month | year
     * 统计规则：仅统计 payment_status='已支付' 且 verification_status!='已退款' 的记录
     * 分组字段：addtime
     */
    @IgnoreAuth
    @GetMapping("/stats/deposit/{type}")
    public R depositStats(@PathVariable("type") String type) {
        List<RestaurantReservationEntity> all = restaurantReservationService.selectList(new EntityWrapper<RestaurantReservationEntity>()
                .eq("payment_status", "已支付")
                .andNew().isNull("verification_status").or().ne("verification_status", "已退款"));
        Map<String, Double> bucket = new LinkedHashMap<>();
        SimpleDateFormat fmtMonth = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat fmtYear = new SimpleDateFormat("yyyy");
        for (RestaurantReservationEntity e : all) {
            if (e == null) continue;
            Date t = e.getAddtime();
            if (t == null) {
                t = e.getReservationTime();
            }
            if (t == null) continue;
            Double dep = e.getDeposit();
            double val = dep == null ? 50.0 : dep.doubleValue();
            String key;
            if ("year".equalsIgnoreCase(type) || "年".equals(type)) {
                key = fmtYear.format(t);
            } else {
                key = fmtMonth.format(t);
            }
            bucket.put(key, (bucket.getOrDefault(key, 0.0)) + val);
        }
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map.Entry<String, Double> en : bucket.entrySet()) {
            Map<String, Object> m = new HashMap<>();
            m.put("addtime", en.getKey());
            m.put("total", Math.round(en.getValue() * 100.0) / 100.0);
            out.add(m);
        }
        Collections.sort(out, (a, b) -> {
            String ka = String.valueOf(a.get("addtime"));
            String kb = String.valueOf(b.get("addtime"));
            return ka.compareTo(kb);
        });
        return R.ok().put("data", out);
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
    public R hexiao(@RequestBody Long[] ids, HttpServletRequest request){
        List<RestaurantReservationEntity> list = new ArrayList<RestaurantReservationEntity>();
        double totalRefund = 0;
        String todayYmd = formatYmd(new Date());
        String role = String.valueOf(Optional.ofNullable(com.cl.utils.CommonUtil.getPropertyByFieldNames(request.getSession(), "role")).orElse(""));
        String username = String.valueOf(Optional.ofNullable(request.getSession().getAttribute("username")).orElse(""));
        String operatorText = "";
        if (StringUtils.isNotBlank(username)) {
            if ("管理员".equals(role)) {
                operatorText = "（核销人：管理员 " + username + "）";
            } else if ("员工".equals(role)) {
                operatorText = "（核销人：员工 " + username + "）";
            } else {
                operatorText = "（核销人：" + username + "）";
            }
        }
        for(Long id : ids) {
            RestaurantReservationEntity restaurantReservation = restaurantReservationService.selectById(id);
            if(restaurantReservation == null) {
                continue;
            }
            String pay = restaurantReservation.getPaymentStatus();
            String ver = restaurantReservation.getVerificationStatus();
            if("已支付".equals(pay) && "未核销".equals(ver)) {
                Date rt = restaurantReservation.getReservationTime();
                if(rt == null) {
                    return R.error("预约时间缺失，无法核销");
                }
                String rtYmd = formatYmd(rt);
                if(rtYmd == null) {
                    return R.error("预约时间缺失，无法核销");
                }
                if(todayYmd.compareTo(rtYmd) > 0) {
                    return R.error("仅允许在预约当天或预约日前进行核销");
                }
                // 查询用户信息
                UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("login_name", restaurantReservation.getLoginName()));
                Double depositVal = restaurantReservation.getDeposit() == null ? 50.0 : restaurantReservation.getDeposit();
                if(user != null) {
                    double currentMoney = user.getBalance() == null ? 0 : user.getBalance();
                    double newMoney = Math.round((currentMoney + depositVal) * 100.0) / 100.0;
                    user.setBalance(newMoney);
                    userService.updateById(user);
                    totalRefund += depositVal;
                }
                
                String verNote = "已核销" + (StringUtils.isNotBlank(operatorText) ? operatorText : "");
                restaurantReservation.setVerificationStatus(verNote);
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
