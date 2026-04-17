package com.cl.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.entity.SupportTicketEntity;
import com.cl.service.SupportTicketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class SupportTicketCleanupTask {
    @Autowired
    private SupportTicketService supportTicketService;

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanOldMessages() {
        List<SupportTicketEntity> tickets = supportTicketService.selectList(new EntityWrapper<SupportTicketEntity>());
        if (tickets == null || tickets.isEmpty()) return;
        Date cutoff = daysAgo(7);
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
        for (SupportTicketEntity ticket : tickets) {
            String raw = ticket.getMessages();
            if (StringUtils.isBlank(raw)) continue;
            JSONArray arr = parseArray(raw);
            if (arr == null || arr.isEmpty()) continue;
            JSONArray filtered = new JSONArray();
            for (int i = 0; i < arr.size(); i++) {
                Object o = arr.get(i);
                if (!(o instanceof JSONObject)) continue;
                JSONObject m = (JSONObject) o;
                Date d = readMessageTime(sdf, m);
                if (d == null || !d.before(cutoff)) {
                    filtered.add(m);
                }
            }
            if (filtered.size() != arr.size()) {
                ticket.setMessages(filtered.toJSONString());
                supportTicketService.updateById(ticket);
            }
        }
    }

    @PostConstruct
    public void runOnStartup() {
        try {
            cleanOldMessages();
        } catch (Exception ignored) {}
    }

    private JSONArray parseArray(String s) {
        try {
            JSONArray arr = JSONArray.parseArray(s);
            return arr == null ? new JSONArray() : arr;
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    private Date parseTime(SimpleDateFormat sdf, String s) {
        if (StringUtils.isBlank(s)) return null;
        try {
            return sdf.parse(s.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    private Date parseEpoch(String s) {
        if (StringUtils.isBlank(s)) return null;
        String t = s.trim();
        if (!t.matches("^\\d{10,}$")) return null;
        try {
            long v = Long.parseLong(t);
            if (t.length() == 10) v = v * 1000L;
            return new Date(v);
        } catch (Exception e) {
            return null;
        }
    }

    private Date readMessageTime(SimpleDateFormat sdf, JSONObject m) {
        // 尝试多种时间字段：addtime / addTime / add_time / timestamp
        String t1 = m.getString("addtime");
        Date d1 = parseTime(sdf, t1);
        if (d1 != null) return d1;
        String t2 = m.getString("addTime");
        Date d2 = parseTime(sdf, t2);
        if (d2 != null) return d2;
        String t3 = m.getString("add_time");
        Date d3 = parseTime(sdf, t3);
        if (d3 != null) return d3;
        String t4 = m.getString("timestamp");
        Date d4 = parseTime(sdf, t4);
        if (d4 != null) return d4;
        // 尝试时间戳
        Date d5 = parseEpoch(t1);
        if (d5 != null) return d5;
        Date d6 = parseEpoch(t2);
        if (d6 != null) return d6;
        Date d7 = parseEpoch(t3);
        if (d7 != null) return d7;
        Date d8 = parseEpoch(t4);
        return d8;
    }

    private Date daysAgo(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, -days);
        return c.getTime();
    }
}
