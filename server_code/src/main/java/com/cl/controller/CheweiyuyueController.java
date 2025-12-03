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

import com.cl.entity.CheweiyuyueEntity;
import com.cl.entity.view.CheweiyuyueView;
import com.cl.entity.YonghuEntity;

import com.cl.service.CheweiyuyueService;
import com.cl.service.YonghuService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 车位预约
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/cheweiyuyue")
public class CheweiyuyueController {
    @Autowired
    private CheweiyuyueService cheweiyuyueService;
    
    @Autowired
    private YonghuService yonghuService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CheweiyuyueEntity cheweiyuyue,
                                                                                                                                            HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                                                                        if(tableName.equals("yonghu")) {
                    cheweiyuyue.setZhanghao((String)request.getSession().getAttribute("username"));
                                    }
                                                                                                                                                    EntityWrapper<CheweiyuyueEntity> ew = new EntityWrapper<CheweiyuyueEntity>();
                                                                                                                                                                                                                        
        
        
        PageUtils page = cheweiyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cheweiyuyue), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CheweiyuyueEntity cheweiyuyue,
		HttpServletRequest request){
        EntityWrapper<CheweiyuyueEntity> ew = new EntityWrapper<CheweiyuyueEntity>();

		PageUtils page = cheweiyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cheweiyuyue), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CheweiyuyueEntity cheweiyuyue){
       	EntityWrapper<CheweiyuyueEntity> ew = new EntityWrapper<CheweiyuyueEntity>();
      	ew.allEq(MPUtil.allEQMapPre( cheweiyuyue, "cheweiyuyue")); 
        return R.ok().put("data", cheweiyuyueService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CheweiyuyueEntity cheweiyuyue){
        EntityWrapper< CheweiyuyueEntity> ew = new EntityWrapper< CheweiyuyueEntity>();
 		ew.allEq(MPUtil.allEQMapPre( cheweiyuyue, "cheweiyuyue")); 
		CheweiyuyueView cheweiyuyueView =  cheweiyuyueService.selectView(ew);
		return R.ok("查询车位预约成功").put("data", cheweiyuyueView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CheweiyuyueEntity cheweiyuyue = cheweiyuyueService.selectById(id);
		cheweiyuyue = cheweiyuyueService.selectView(new EntityWrapper<CheweiyuyueEntity>().eq("id", id));
        return R.ok().put("data", cheweiyuyue);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CheweiyuyueEntity cheweiyuyue = cheweiyuyueService.selectById(id);
		cheweiyuyue = cheweiyuyueService.selectView(new EntityWrapper<CheweiyuyueEntity>().eq("id", id));
        return R.ok().put("data", cheweiyuyue);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CheweiyuyueEntity cheweiyuyue, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cheweiyuyue);
        cheweiyuyueService.insert(cheweiyuyue);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @Transactional
    public R add(@RequestBody CheweiyuyueEntity cheweiyuyue, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cheweiyuyue);
    	
    	// 只有在支付成功后才检查冲突（避免未支付的预约占用时段）
        if("已支付".equals(cheweiyuyue.getZhifuzhuangtai())) {
            // 检查该车位在该时间段是否已被预约
            EntityWrapper<CheweiyuyueEntity> wrapper = new EntityWrapper<CheweiyuyueEntity>();
            wrapper.eq("cheweibianhao", cheweiyuyue.getCheweibianhao());
            wrapper.eq("yuyueshijian", cheweiyuyue.getYuyueshijian());
            wrapper.eq("zhifuzhuangtai", "已支付"); // 只检查已支付的预约
            wrapper.eq("hexiaozhuangtai", "未核销"); // 只检查未核销的预约
            
            int count = cheweiyuyueService.selectCount(wrapper);
            System.out.println("========== 车位预约冲突检测 ==========");
            System.out.println("车位编号: " + cheweiyuyue.getCheweibianhao());
            System.out.println("预约时间: " + cheweiyuyue.getYuyueshijian());
            System.out.println("已有预约数量: " + count);
            System.out.println("================================");
            
            if(count > 0) {
                return R.error("该车位在此时间段已被预约，请选择其他时间段");
            }
        }
        
        // 设置定金为50元
        cheweiyuyue.setDingjin(50.0);
        
        // 如果支付状态为已支付，扣除用户余额
        if("已支付".equals(cheweiyuyue.getZhifuzhuangtai())) {
            // 查询用户信息
            YonghuEntity yonghu = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", cheweiyuyue.getZhanghao()));
            if(yonghu == null) {
                return R.error("用户不存在");
            }
            
            // 检查余额是否足够
            if(yonghu.getMoney() == null || yonghu.getMoney() < 50.0) {
                return R.error("账户余额不足，请先充值");
            }
            
            // 扣除定金，使用精确计算
            double currentMoney = yonghu.getMoney();
            double newMoney = Math.round((currentMoney - 50.0) * 100.0) / 100.0; // 保留2位小数
            yonghu.setMoney(newMoney);
            yonghuService.updateById(yonghu);
            
            cheweiyuyue.setHexiaozhuangtai("未核销");
        } else {
            cheweiyuyue.setZhifuzhuangtai("未支付");
            cheweiyuyue.setHexiaozhuangtai("未核销");
        }
        
        cheweiyuyueService.insert(cheweiyuyue);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CheweiyuyueEntity cheweiyuyue, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cheweiyuyue);
        cheweiyuyueService.updateById(cheweiyuyue);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        cheweiyuyueService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 核销退费
     */
    @RequestMapping("/hexiao")
    @Transactional
    public R hexiao(@RequestBody Long[] ids){
        List<CheweiyuyueEntity> list = new ArrayList<CheweiyuyueEntity>();
        double totalRefund = 0;
        
        for(Long id : ids) {
            CheweiyuyueEntity cheweiyuyue = cheweiyuyueService.selectById(id);
            if("已支付".equals(cheweiyuyue.getZhifuzhuangtai()) && "未核销".equals(cheweiyuyue.getHexiaozhuangtai())) {
                // 查询用户信息
                YonghuEntity yonghu = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", cheweiyuyue.getZhanghao()));
                if(yonghu != null) {
                    // 退还定金到用户账户，使用精确计算
                    double currentMoney = yonghu.getMoney() == null ? 0 : yonghu.getMoney();
                    double newMoney = Math.round((currentMoney + 50.0) * 100.0) / 100.0; // 保留2位小数
                    yonghu.setMoney(newMoney);
                    yonghuService.updateById(yonghu);
                    totalRefund += 50.0;
                }
                
                cheweiyuyue.setHexiaozhuangtai("已核销");
                cheweiyuyue.setZhifuzhuangtai("已退款");
                cheweiyuyueService.updateById(cheweiyuyue);
                list.add(cheweiyuyue);
            }
        }
        
        if(list.size() > 0) {
            return R.ok().put("message", "核销退费成功，共退还" + totalRefund + "元到用户账户");
        } else {
            return R.error("没有可核销的预约");
        }
    }
    
    /**
     * 获取已预约的时段
     */
    @IgnoreAuth
    @RequestMapping("/getBookedTimeSlots")
    public R getBookedTimeSlots(@RequestParam String cheweibianhao, @RequestParam String yuyueriqi){
        // 查询指定车位在指定日期已经被预约的时段（已支付且未核销）
        EntityWrapper<CheweiyuyueEntity> wrapper = new EntityWrapper<CheweiyuyueEntity>();
        wrapper.eq("cheweibianhao", cheweibianhao);
        wrapper.like("yuyueshijian", yuyueriqi); // 匹配日期部分
        wrapper.eq("zhifuzhuangtai", "已支付");
        wrapper.eq("hexiaozhuangtai", "未核销");
        
        List<CheweiyuyueEntity> list = cheweiyuyueService.selectList(wrapper);
        List<String> bookedTimeSlots = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for(CheweiyuyueEntity entity : list) {
            if(entity.getYuyueshijian() != null) {
                try {
                    // 将 Date 转换为字符串
                    String timeStr = sdf.format(entity.getYuyueshijian());
                    // 提取时间中的小时部分，例如 "2025-11-10 08:00:00" -> "08"
                    String[] parts = timeStr.split(" ");
                    if(parts.length > 1) {
                        String[] timeParts = parts[1].split(":");
                        if(timeParts.length > 0) {
                            bookedTimeSlots.add(timeParts[0]);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return R.ok().put("bookedTimeSlots", bookedTimeSlots);
    }
	






    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,CheweiyuyueEntity cheweiyuyue, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yonghu")) {
            cheweiyuyue.setZhanghao((String)request.getSession().getAttribute("username"));
        }
        EntityWrapper<CheweiyuyueEntity> ew = new EntityWrapper<CheweiyuyueEntity>();
        int count = cheweiyuyueService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cheweiyuyue), params), params));
        return R.ok().put("data", count);
    }



}
