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

import com.cl.entity.CantingyuyueEntity;
import com.cl.entity.view.CantingyuyueView;

import com.cl.service.CantingyuyueService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;
import com.cl.service.StoreupService;
import com.cl.entity.StoreupEntity;
import com.cl.service.YonghuService;
import com.cl.entity.YonghuEntity;

/**
 * 餐厅预约
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-25 17:00:58
 */
@RestController
@RequestMapping("/cantingyuyue")
public class CantingyuyueController {
    @Autowired
    private CantingyuyueService cantingyuyueService;

    @Autowired
    private StoreupService storeupService;
    
    @Autowired
    private YonghuService yonghuService;








    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CantingyuyueEntity cantingyuyue,
                                                                                                                                                                                                                HttpServletRequest request){
                    String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                        if(tableName.equals("yonghu")) {
                    cantingyuyue.setZhanghao((String)request.getSession().getAttribute("username"));
                                    }
                                                                                                                                                                                EntityWrapper<CantingyuyueEntity> ew = new EntityWrapper<CantingyuyueEntity>();
                                                                                                                                                                                                                                                                                                                                    
        
        
        PageUtils page = cantingyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cantingyuyue), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CantingyuyueEntity cantingyuyue,
		HttpServletRequest request){
        EntityWrapper<CantingyuyueEntity> ew = new EntityWrapper<CantingyuyueEntity>();

		PageUtils page = cantingyuyueService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cantingyuyue), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CantingyuyueEntity cantingyuyue){
       	EntityWrapper<CantingyuyueEntity> ew = new EntityWrapper<CantingyuyueEntity>();
      	ew.allEq(MPUtil.allEQMapPre( cantingyuyue, "cantingyuyue")); 
        return R.ok().put("data", cantingyuyueService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CantingyuyueEntity cantingyuyue){
        EntityWrapper< CantingyuyueEntity> ew = new EntityWrapper< CantingyuyueEntity>();
 		ew.allEq(MPUtil.allEQMapPre( cantingyuyue, "cantingyuyue")); 
		CantingyuyueView cantingyuyueView =  cantingyuyueService.selectView(ew);
		return R.ok("查询餐厅预约成功").put("data", cantingyuyueView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CantingyuyueEntity cantingyuyue = cantingyuyueService.selectById(id);
		cantingyuyue = cantingyuyueService.selectView(new EntityWrapper<CantingyuyueEntity>().eq("id", id));
        return R.ok().put("data", cantingyuyue);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CantingyuyueEntity cantingyuyue = cantingyuyueService.selectById(id);
		cantingyuyue = cantingyuyueService.selectView(new EntityWrapper<CantingyuyueEntity>().eq("id", id));
        return R.ok().put("data", cantingyuyue);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CantingyuyueEntity cantingyuyue, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cantingyuyue);
        // 设置定金为50元
        cantingyuyue.setDingjin(50.0);
        // 如果支付状态为已支付，自动通过审核
        if("已支付".equals(cantingyuyue.getZhifuzhuangtai())) {
            cantingyuyue.setSfsh("是");
            cantingyuyue.setShhf("预约成功");
            cantingyuyue.setHexiaozhuangtai("未核销");
        } else {
            cantingyuyue.setZhifuzhuangtai("未支付");
            cantingyuyue.setSfsh("待审核");
            cantingyuyue.setHexiaozhuangtai("未核销");
        }
        cantingyuyueService.insert(cantingyuyue);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @Transactional
    public R add(@RequestBody CantingyuyueEntity cantingyuyue, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(cantingyuyue);
        
        // 只有在支付成功后才检查冲突（避免未支付的预约占用时段）
        if("已支付".equals(cantingyuyue.getZhifuzhuangtai())) {
            // 检查该餐桌在该时间段是否已被预约
            EntityWrapper<CantingyuyueEntity> wrapper = new EntityWrapper<CantingyuyueEntity>();
            wrapper.eq("canzhuomingcheng", cantingyuyue.getCanzhuomingcheng());
            wrapper.eq("yuyueshijian", cantingyuyue.getYuyueshijian());
            wrapper.eq("zhifuzhuangtai", "已支付"); // 只检查已支付的预约
            wrapper.eq("hexiaozhuangtai", "未核销"); // 只检查未核销的预约
            
            int count = cantingyuyueService.selectCount(wrapper);
            
            if(count > 0) {
                return R.error("该餐桌在此时间段已被预约，请选择其他时间段");
            }
        }
        
        // 设置定金为50元
        cantingyuyue.setDingjin(50.0);
        // 如果支付状态为已支付，自动通过审核并扣除用户余额
        if("已支付".equals(cantingyuyue.getZhifuzhuangtai())) {
            // 查询用户信息
            YonghuEntity yonghu = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", cantingyuyue.getZhanghao()));
            if(yonghu == null) {
                return R.error("用户不存在");
            }
            // 检查余额是否足够
            if(yonghu.getMoney() == null || yonghu.getMoney() < 50.0) {
                return R.error("账户余额不足，请先充值");
            }
            // 扣除定金，使用 BigDecimal 确保精度
            double currentMoney = yonghu.getMoney();
            double newMoney = Math.round((currentMoney - 50.0) * 100.0) / 100.0; // 保留2位小数
            yonghu.setMoney(newMoney);
            yonghuService.updateById(yonghu);
            
            cantingyuyue.setSfsh("是");
            cantingyuyue.setShhf("预约成功，已扣除定金50元");
            cantingyuyue.setHexiaozhuangtai("未核销");
        } else {
            cantingyuyue.setZhifuzhuangtai("未支付");
            cantingyuyue.setSfsh("待审核");
            cantingyuyue.setHexiaozhuangtai("未核销");
        }
        cantingyuyueService.insert(cantingyuyue);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CantingyuyueEntity cantingyuyue, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cantingyuyue);
        cantingyuyueService.updateById(cantingyuyue);//全部更新
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<CantingyuyueEntity> list = new ArrayList<CantingyuyueEntity>();
        for(Long id : ids) {
            CantingyuyueEntity cantingyuyue = cantingyuyueService.selectById(id);
            cantingyuyue.setSfsh(sfsh);
            cantingyuyue.setShhf(shhf);
            list.add(cantingyuyue);
        }
        cantingyuyueService.updateBatchById(list);
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        cantingyuyueService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 核销退费
     */
    @RequestMapping("/hexiao")
    @Transactional
    public R hexiao(@RequestBody Long[] ids){
        List<CantingyuyueEntity> list = new ArrayList<CantingyuyueEntity>();
        double totalRefund = 0;
        for(Long id : ids) {
            CantingyuyueEntity cantingyuyue = cantingyuyueService.selectById(id);
            if("已支付".equals(cantingyuyue.getZhifuzhuangtai()) && "未核销".equals(cantingyuyue.getHexiaozhuangtai())) {
                // 查询用户信息
                YonghuEntity yonghu = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", cantingyuyue.getZhanghao()));
                if(yonghu != null) {
                    // 退还定金到用户账户，使用精确计算
                    double currentMoney = yonghu.getMoney() == null ? 0 : yonghu.getMoney();
                    double newMoney = Math.round((currentMoney + 50.0) * 100.0) / 100.0; // 保留2位小数
                    yonghu.setMoney(newMoney);
                    yonghuService.updateById(yonghu);
                    totalRefund += 50.0;
                }
                
                cantingyuyue.setHexiaozhuangtai("已核销");
                cantingyuyue.setZhifuzhuangtai("已退款");
                cantingyuyue.setShhf("已到店核销，定金50元已退还到账户");
                list.add(cantingyuyue);
            }
        }
        if(list.size() > 0) {
            cantingyuyueService.updateBatchById(list);
            return R.ok().put("message", "核销退费成功，共退还" + totalRefund + "元到用户账户");
        } else {
            return R.error("没有可核销的预约");
        }
    }
	











}
