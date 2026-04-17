package com.cl.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.DishInfoEntity;
import com.cl.entity.ParkingSpotEntity;
import com.cl.entity.SupportFaqEntity;
import com.cl.entity.SupportTicketEntity;
import com.cl.entity.UserEntity;
import com.cl.service.DishInfoService;
import com.cl.service.ParkingSpotService;
import com.cl.service.SupportFaqService;
import com.cl.service.SupportTicketService;
import com.cl.service.UserService;
import com.cl.service.ConfigService;
import com.cl.entity.ConfigEntity;
import com.cl.utils.CommonUtil;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/support")
public class SupportController {
    @Autowired
    private SupportFaqService supportFaqService;

    @Autowired
    private SupportTicketService supportTicketService;

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private DishInfoService dishInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Value("${coze.enabled:false}")
    private boolean cozeEnabled;

    @Value("${coze.api-url:https://api.coze.cn/open_api/v2/chat}")
    private String cozeApiUrl;

    @Value("${coze.bot-id:}")
    private String cozeBotId;

    @Value("${coze.workflow-run-url:https://api.coze.cn/v1/workflow/run}")
    private String cozeWorkflowRunUrl;

    @Value("${coze.workflow-id:}")
    private String cozeWorkflowId;

    @Value("${coze.app-id:}")
    private String cozeAppId;

    @Value("${coze.workflow-query-key:}")
    private String cozeWorkflowQueryKey;

    @Value("${coze.token:}")
    private String cozeToken;

    @Value("${coze.token-file:}")
    private String cozeTokenFile;

    @Value("${coze.timeout-ms:8000}")
    private int cozeTimeoutMs;

    @IgnoreAuth
    @RequestMapping("/chat")
    public R chat(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String message = body == null ? null : String.valueOf(body.get("message"));
        message = message == null ? "" : message.trim();
        Object parking = body == null ? null : body.get("parking");
        Object dish = body == null ? null : body.get("dish");

        try {
            String dishSummary;
            if (dish == null) {
                dishSummary = "null";
            } else if (dish instanceof String) {
                dishSummary = (String) dish;
            } else {
                dishSummary = JSON.toJSONString(dish);
            }
        } catch (Exception ignored) {
        }

        Map<String, Object> data = new HashMap<>();
        if (StringUtils.isBlank(message)) {
            data.put("reply", "请先输入问题内容。");
            data.put("suggestTicket", false);
            return R.ok().put("data", data);
        }

        String reply = null;
        boolean suggestTicket = false;
        if (StringUtils.isBlank(reply)) {
            reply = tryFaqReply(message);
        }
        if (StringUtils.isBlank(reply)) {
            reply = tryCozeWorkflowReply(message, parking, dish);
        }
        if (StringUtils.isBlank(reply)) {
            reply = tryCozeReply(message, request);
        }
        if (StringUtils.isBlank(reply)) {
            reply = "我暂时没能准确回答你的问题。你可以点击“转人工”，我会把问题转给客服处理。";
            suggestTicket = true;
        }
        data.put("reply", reply);
        data.put("suggestTicket", suggestTicket);
        return R.ok().put("data", data);
    }

    @Transactional
    @RequestMapping("/ticket/submit")
    public R submitTicket(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long userId = getSessionUserId(request);
        if (userId == null) {
            return R.error(401, "请先登录后再发起会话");
        }

        String content = body == null ? null : String.valueOf(body.get("content"));
        content = content == null ? "" : content.trim();
        if (StringUtils.isBlank(content)) {
            return R.error("会话内容不能为空");
        }

        SupportTicketEntity existing = supportTicketService.selectOne(new EntityWrapper<SupportTicketEntity>().eq("user_id", userId));
        Date now = new Date();
        JSONArray messages;
        SupportTicketEntity ticket;
        if (existing == null) {
            ticket = new SupportTicketEntity();
            ticket.setUserId(userId);
            ticket.setAddtime(now);
            messages = new JSONArray();
            messages.add(buildTicketMessage(ticket, "用户", userId, content, now, messages));
            ticket.setMessages(messages.toJSONString());
            supportTicketService.insert(ticket);
        } else {
            ticket = existing;
            messages = parseTicketMessages(ticket.getMessages());
            if (messages == null) {
                messages = new JSONArray();
            }
            messages.add(buildTicketMessage(ticket, "用户", userId, content, now, messages));
            ticket.setMessages(messages.toJSONString());
            supportTicketService.updateById(ticket);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("ticketId", ticket.getId());
        return R.ok().put("data", data);
    }

    @RequestMapping("/ticket/my")
    public R myTickets(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long userId = getSessionUserId(request);
        if (userId == null) {
            return R.error(401, "请先登录后再查看会话");
        }
        EntityWrapper<SupportTicketEntity> ew = new EntityWrapper<>();
        ew.eq("user_id", userId);
        PageUtils page = supportTicketService.queryPage(params, ew);
        return R.ok().put("data", page);
    }

    @RequestMapping("/ticket/page")
    public R ticketPage(@RequestParam Map<String, Object> params, SupportTicketEntity ticket, HttpServletRequest request) {
        String role = getSessionRole(request);
        if (role == null) {
            return R.error(401, "未登录或登录过期");
        }
        if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        EntityWrapper<SupportTicketEntity> ew = new EntityWrapper<>();
        Object accountObj = params == null ? null : params.get("login_name");
        if (accountObj == null) {
            accountObj = params == null ? null : params.get("loginName");
        }
        if (accountObj == null) {
            accountObj = params == null ? null : params.get("username");
        }
        if (accountObj == null) {
            accountObj = params == null ? null : params.get("account");
        }
        String account = accountObj == null ? null : String.valueOf(accountObj).trim();
        if (StringUtils.isNotBlank(account)) {
            EntityWrapper<UserEntity> userEw = new EntityWrapper<>();
            userEw.like("login_name", account);
            List<UserEntity> users = userService.selectList(userEw);
            if (users == null || users.isEmpty()) {
                ew.eq("user_id", -1);
            } else {
                List<Long> userIds = new java.util.ArrayList<>();
                for (UserEntity u : users) {
                    if (u != null && u.getId() != null) {
                        userIds.add(u.getId());
                    }
                }
                if (userIds.isEmpty()) {
                    ew.eq("user_id", -1);
                } else {
                    ew.in("user_id", userIds);
                }
            }
        }
        PageUtils page = supportTicketService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, ticket), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/ticket/{ticketId}/messages")
    public R ticketMessages(@PathVariable("ticketId") Long ticketId, HttpServletRequest request) {
        if (ticketId == null) {
            return R.error("参数错误");
        }
        String role = getSessionRole(request);
        if (role == null) {
            return R.error(401, "未登录或登录过期");
        }
        SupportTicketEntity ticket = supportTicketService.selectById(ticketId);
        if (ticket == null) {
            return R.error("会话不存在");
        }
        if ("用户".equals(role)) {
            Long userId = getSessionUserId(request);
            if (userId == null) {
                return R.error(401, "未登录或登录过期");
            }
            if (!userId.equals(ticket.getUserId())) {
                return R.error(403, "无权限操作该会话");
            }
        } else if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        JSONArray messages = parseTicketMessages(ticket.getMessages());
        if (messages != null && !messages.isEmpty() && ticket.getUserId() != null) {
            for (int i = 0; i < messages.size(); i++) {
                Object o = messages.get(i);
                if (!(o instanceof JSONObject)) {
                    continue;
                }
                JSONObject m = (JSONObject) o;
                normalizeSenderRole(m, ticket.getUserId());
            }
        }
        return R.ok().put("data", messages == null ? new JSONArray() : messages);
    }

    @Transactional
    @RequestMapping("/ticket/{ticketId}/reply")
    public R replyTicket(@PathVariable("ticketId") Long ticketId, @RequestBody Map<String, Object> body, HttpServletRequest request) {
        if (ticketId == null) {
            return R.error("参数错误");
        }
        Long userId = getSessionUserId(request);
        if (userId == null) {
            return R.error(401, "请先登录后再回复");
        }
        String content = body == null ? null : String.valueOf(body.get("content"));
        content = content == null ? "" : content.trim();
        if (StringUtils.isBlank(content)) {
            return R.error("回复内容不能为空");
        }

        SupportTicketEntity ticket = supportTicketService.selectById(ticketId);
        if (ticket == null) {
            return R.error("会话不存在");
        }
        if (!userId.equals(ticket.getUserId())) {
            return R.error(403, "无权限操作该会话");
        }

        JSONArray messages = parseTicketMessages(ticket.getMessages());
        if (messages == null) {
            messages = new JSONArray();
        }
        messages.add(buildTicketMessage(ticket, "用户", userId, content, new Date(), messages));
        ticket.setMessages(messages.toJSONString());
        supportTicketService.updateById(ticket);
        return R.ok();
    }

    @Transactional
    @RequestMapping("/ticket/{ticketId}/staffReply")
    public R staffReplyTicket(@PathVariable("ticketId") Long ticketId, @RequestBody Map<String, Object> body, HttpServletRequest request) {
        if (ticketId == null) {
            return R.error("参数错误");
        }
        String role = getSessionRole(request);
        if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        Long operatorId = getSessionUserId(request);
        if (operatorId == null) {
            return R.error(401, "请先登录后再操作");
        }

        String content = body == null ? null : String.valueOf(body.get("content"));
        content = content == null ? "" : content.trim();
        if (StringUtils.isBlank(content)) {
            return R.error("回复内容不能为空");
        }

        SupportTicketEntity ticket = supportTicketService.selectById(ticketId);
        if (ticket == null) {
            return R.error("会话不存在");
        }

        JSONArray messages = parseTicketMessages(ticket.getMessages());
        if (messages == null) {
            messages = new JSONArray();
        }
        messages.add(buildTicketMessage(ticket, role, operatorId, content, new Date(), messages));
        ticket.setMessages(messages.toJSONString());
        supportTicketService.updateById(ticket);
        return R.ok();
    }

    

    @RequestMapping("/ticket/info/{id}")
    public R ticketInfo(@PathVariable("id") Long id, HttpServletRequest request) {
        if (id == null) {
            return R.error("参数错误");
        }
        String role = getSessionRole(request);
        if (role == null) {
            return R.error(401, "未登录或登录过期");
        }
        SupportTicketEntity ticket = supportTicketService.selectById(id);
        if (ticket == null) {
            return R.error("会话不存在");
        }
        if ("用户".equals(role)) {
            Long userId = getSessionUserId(request);
            if (userId == null) {
                return R.error(401, "未登录或登录过期");
            }
            if (!userId.equals(ticket.getUserId())) {
                return R.error(403, "无权限操作该会话");
            }
        } else if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        return R.ok().put("data", ticket);
    }

    @RequestMapping("/faq/page")
    public R faqPage(@RequestParam Map<String, Object> params, SupportFaqEntity faq, HttpServletRequest request) {
        String role = getSessionRole(request);
        if (role == null) {
            return R.error(401, "未登录或登录过期");
        }
        if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        EntityWrapper<SupportFaqEntity> ew = new EntityWrapper<>();
        PageUtils page = supportFaqService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, faq), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/faq/info/{id}")
    public R faqInfo(@PathVariable("id") Long id, HttpServletRequest request) {
        String role = getSessionRole(request);
        if (role == null) {
            return R.error(401, "未登录或登录过期");
        }
        if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        SupportFaqEntity faq = supportFaqService.selectById(id);
        if (faq == null) {
            return R.error("FAQ不存在");
        }
        return R.ok().put("data", faq);
    }

    @Transactional
    @RequestMapping("/faq/save")
    public R saveFaq(@RequestBody SupportFaqEntity faq, HttpServletRequest request) {
        String role = getSessionRole(request);
        if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        if (faq == null) {
            return R.error("参数错误");
        }
        if (StringUtils.isBlank(faq.getQuestion()) || StringUtils.isBlank(faq.getAnswer())) {
            return R.error("问题和答案不能为空");
        }
        if (faq.getEnabled() == null) {
            faq.setEnabled(1);
        }
        if (faq.getAddtime() == null) {
            faq.setAddtime(new Date());
        }
        supportFaqService.insert(faq);
        return R.ok();
    }

    @Transactional
    @RequestMapping("/faq/update")
    public R updateFaq(@RequestBody SupportFaqEntity faq, HttpServletRequest request) {
        String role = getSessionRole(request);
        if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        if (faq == null || faq.getId() == null) {
            return R.error("参数错误");
        }
        supportFaqService.updateById(faq);
        return R.ok();
    }

    @RequestMapping("/faq/delete")
    public R deleteFaq(@RequestBody Long[] ids, HttpServletRequest request) {
        String role = getSessionRole(request);
        if (!"管理员".equals(role) && !"员工".equals(role)) {
            return R.error(403, "无权限操作");
        }
        if (ids == null || ids.length == 0) {
            return R.error("参数错误");
        }
        supportFaqService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    private Long getSessionUserId(HttpServletRequest request) {
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj == null) {
            return null;
        }
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        }
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(userIdObj));
        } catch (Exception e) {
            return null;
        }
    }

    @IgnoreAuth
    private String getSessionRole(HttpServletRequest request) {
        Object roleObj = request.getSession().getAttribute("role");
        return roleObj == null ? null : String.valueOf(roleObj);
    }

    private String getSessionTableName(HttpServletRequest request) {
        Object tableNameObj = request.getSession().getAttribute("tableName");
        return tableNameObj == null ? null : String.valueOf(tableNameObj);
    }

    @RequestMapping("/faq/search")
    public R faqSearch(@RequestParam Map<String, Object> params) {
        String keyword = params == null ? null : (params.get("keyword") == null ? null : String.valueOf(params.get("keyword")));
        keyword = keyword == null ? "" : keyword.trim();
        EntityWrapper<SupportFaqEntity> ew = new EntityWrapper<>();
        ew.eq("enabled", 1);
        if (StringUtils.isNotBlank(keyword)) {
            ew.andNew().like("question", keyword).or().like("keywords", keyword).or().like("answer", keyword);
        }
        ew.orderBy("id", true);
        List<SupportFaqEntity> list = supportFaqService.selectList(ew);
        return R.ok().put("data", list);
    }

    private String tryRuleReply(String message) {
        String msg = message.toLowerCase();

        if (msg.contains("车位") || msg.contains("停车")) {
            int count = parkingSpotService.selectCount(new EntityWrapper<ParkingSpotEntity>().eq("spot_status", "空闲"));
            return "当前空闲车位数量：" + count + " 个。你可以进入“车位信息”查看具体位置与状态。";
        }

        if (msg.contains("定金") || msg.contains("预约")) {
            return "餐桌预约提交后需支付订金，支付成功后预约生效；到店核销后按规则处理订金。你可以在“餐厅预约”中查看预约记录。";
        }

        if (msg.contains("计费") || msg.contains("收费") || msg.contains("多少钱")) {
            return "停车费用按时间计费，离场时系统自动计算费用并更新车位状态。具体费用以系统计算结果为准。";
        }

        if (msg.contains("推荐") || msg.contains("好吃") || msg.contains("菜品")) {
            List<DishInfoEntity> list = dishInfoService.selectList(new EntityWrapper<DishInfoEntity>()
                .orderBy("storeup_number", false)
                .orderBy("click_time", false)
                .last("limit 5"));
            if (list == null || list.isEmpty()) {
                return "你可以在“美食信息”中按分类浏览菜品，也支持关键词搜索。";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("为你推荐这些热门菜品：");
            for (int i = 0; i < list.size(); i++) {
                DishInfoEntity d = list.get(i);
                if (d == null) continue;
                if (i > 0) sb.append("；");
                sb.append(d.getDishName());
                if (d.getPrice() != null) {
                    sb.append("（").append(d.getPrice()).append("元）");
                }
            }
            sb.append("。");
            return sb.toString();
        }

        return null;
    }

    private String tryFaqReply(String message) {
        List<SupportFaqEntity> faqs = supportFaqService.selectList(new EntityWrapper<SupportFaqEntity>()
            .eq("enabled", 1)
            .orderBy("id", true)
            .last("limit 50"));
        if (faqs == null || faqs.isEmpty()) {
            return null;
        }
        int bestScore = 0;
        SupportFaqEntity best = null;
        String msg = message.toLowerCase();
        for (SupportFaqEntity faq : faqs) {
            if (faq == null) continue;
            int score = 0;
            if (faq.getQuestion() != null && msg.contains(faq.getQuestion().toLowerCase())) {
                score += 3;
            }
            if (faq.getKeywords() != null) {
                String[] keys = faq.getKeywords().toLowerCase().split("[,，;；\\s]+");
                for (String k : keys) {
                    String key = k == null ? "" : k.trim();
                    if (key.length() < 2) continue;
                    if (msg.contains(key)) score += 2;
                }
            }
            if (faq.getCategory() != null && msg.contains(faq.getCategory().toLowerCase())) {
                score += 1;
            }
            if (score > bestScore) {
                bestScore = score;
                best = faq;
            }
        }
        if (best != null && bestScore >= 2 && StringUtils.isNotBlank(best.getAnswer())) {
            return best.getAnswer();
        }
        return null;
    }

    private boolean isCozeEnabled() {
        try {
            ConfigEntity override = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", "coze.enabled"));
            if (override != null && StringUtils.isNotBlank(override.getValue())) {
                String v = override.getValue().trim();
                if ("true".equalsIgnoreCase(v) || "1".equals(v) || "yes".equalsIgnoreCase(v) || "on".equalsIgnoreCase(v)) {
                    return true;
                }
                if ("false".equalsIgnoreCase(v) || "0".equals(v) || "no".equalsIgnoreCase(v) || "off".equalsIgnoreCase(v)) {
                    return false;
                }
            }
        } catch (Exception ignored) {
        }
        return cozeEnabled;
    }

    private String tryCozeReply(String message, HttpServletRequest request) {
        if (!isCozeEnabled()) {
            return null;
        }
        String token = resolveCozeToken();
        if (StringUtils.isBlank(cozeApiUrl) || StringUtils.isBlank(token) || StringUtils.isBlank(cozeBotId)) {
            return null;
        }
        String user = null;
        Long userId = getSessionUserId(request);
        if (userId != null) {
            user = String.valueOf(userId);
        } else {
            Object usernameObj = request.getSession().getAttribute("username");
            if (usernameObj != null) {
                user = String.valueOf(usernameObj);
            }
        }
        if (StringUtils.isBlank(user)) {
            user = "guest";
        }

        JSONObject payload = new JSONObject();
        payload.put("bot_id", cozeBotId);
        payload.put("user", user);
        payload.put("query", message);
        payload.put("stream", false);

        try {
            String respText = HttpRequest.post(cozeApiUrl)
                .timeout(cozeTimeoutMs <= 0 ? 8000 : cozeTimeoutMs)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(payload.toJSONString())
                .execute()
                .body();
            if (StringUtils.isBlank(respText)) {
                return null;
            }
            JSONObject resp = JSON.parseObject(respText);
            return extractCozeReply(resp);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String resolveCozeToken() {
        if (StringUtils.isNotBlank(cozeToken)) {
            return cozeToken.trim();
        }
        String tokenFromFile = readFirstNonBlankLine(cozeTokenFile);
        if (StringUtils.isNotBlank(tokenFromFile)) {
            return tokenFromFile;
        }
        tokenFromFile = readFirstNonBlankLine("./coze.token");
        if (StringUtils.isNotBlank(tokenFromFile)) {
            return tokenFromFile;
        }
        tokenFromFile = readFirstNonBlankLine("./coze-token.txt");
        if (StringUtils.isNotBlank(tokenFromFile)) {
            return tokenFromFile;
        }
        return null;
    }

    private String readFirstNonBlankLine(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        try {
            File f = new File(path.trim());
            if (!f.exists() || !f.isFile()) {
                return null;
            }
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            if (lines == null || lines.isEmpty()) {
                return null;
            }
            for (String line : lines) {
                String v = line == null ? "" : line.trim();
                if (v.isEmpty()) {
                    continue;
                }
                if (v.startsWith("\uFEFF")) {
                    v = v.substring(1).trim();
                }
                return v.isEmpty() ? null : v;
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private String extractCozeReply(JSONObject resp) {
        if (resp == null) {
            return null;
        }
        Integer code = null;
        try {
            code = resp.getInteger("code");
        } catch (Exception ignored) {
        }
        if (code != null && code != 0) {
            return null;
        }

        JSONArray messages = null;
        JSONObject data = resp.getJSONObject("data");
        if (data != null) {
            messages = data.getJSONArray("messages");
            if (messages == null) {
                JSONObject msgObj = data.getJSONObject("message");
                if (msgObj != null && StringUtils.isNotBlank(msgObj.getString("content"))) {
                    return msgObj.getString("content").trim();
                }
            }
        }
        if (messages == null) {
            messages = resp.getJSONArray("messages");
        }
        if (messages == null || messages.isEmpty()) {
            String answer = resp.getString("answer");
            return StringUtils.isBlank(answer) ? null : answer.trim();
        }
        String best = null;
        for (int i = 0; i < messages.size(); i++) {
            JSONObject m = messages.getJSONObject(i);
            if (m == null) continue;
            String role = m.getString("role");
            String content = m.getString("content");
            if (StringUtils.isBlank(content)) continue;
            if ("assistant".equalsIgnoreCase(role)) {
                best = content;
            } else if (best == null) {
                best = content;
            }
        }
        return StringUtils.isBlank(best) ? null : best.trim();
    }

    private String tryCozeWorkflowReply(String message, Object parking, Object dish) {
        if (!isCozeEnabled()) {
            return null;
        }
        String token = resolveCozeToken();
        if (StringUtils.isBlank(token) || StringUtils.isBlank(cozeWorkflowRunUrl) || StringUtils.isBlank(cozeWorkflowId)) {
            return null;
        }

        JSONObject payload = new JSONObject();
        payload.put("workflow_id", cozeWorkflowId);
        String effectiveAppId = StringUtils.isNotBlank(cozeAppId) ? cozeAppId : null;
        if (StringUtils.isNotBlank(effectiveAppId)) {
            payload.put("app_id", effectiveAppId);
        }

        JSONObject parameters = new JSONObject();
        if (parking != null) {
            Integer p = null;
            try {
                if (parking instanceof Number) {
                    p = ((Number) parking).intValue();
                } else {
                    p = Integer.parseInt(String.valueOf(parking));
                }
            } catch (Exception ignored) {
            }
            parameters.put("parking", p == null ? 0 : p);
        } else {
            parameters.put("parking", 0);
        }
        if (dish != null) {
            String dishStr;
            if (dish instanceof String) {
                dishStr = (String) dish;
            } else {
                try {
                    dishStr = JSON.toJSONString(dish);
                } catch (Exception e) {
                    dishStr = String.valueOf(dish);
                }
            }
            parameters.put("dish", dishStr);
        } else {
            parameters.put("dish", "[]");
        }
        String queryKey = StringUtils.isNotBlank(cozeWorkflowQueryKey) ? cozeWorkflowQueryKey.trim() : "query";
        parameters.put(queryKey, message);
        if (!"query".equalsIgnoreCase(queryKey)) {
            parameters.put("query", message);
        }
        if (!"question".equalsIgnoreCase(queryKey)) {
            parameters.put("question", message);
        }
        parameters.put("input", message);
        parameters.put("text", message);
        if (!parameters.isEmpty()) {
            payload.put("parameters", parameters);
        }

        try {
            String respText = HttpRequest.post(cozeWorkflowRunUrl)
                .timeout(cozeTimeoutMs <= 0 ? 8000 : cozeTimeoutMs)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(payload.toJSONString())
                .execute()
                .body();
            if (StringUtils.isBlank(respText)) {
                return null;
            }
            JSONObject resp = JSON.parseObject(respText);
            String reply = null;
            try {
                reply = extractWorkflowReply(resp);
            } catch (Exception e) {
            }
            if (StringUtils.isBlank(reply)) {
                String dataStr = null;
                try {
                    dataStr = resp.getString("data");
                } catch (Exception ignored) {
                }
                String fallback = quickExtractFromDataString(dataStr);
                if (StringUtils.isNotBlank(fallback)) {
                    reply = fallback.trim();
                }
            }
            return reply;
        } catch (Exception ignored) {
            return null;
        }
    }

    private String extractWorkflowReply(JSONObject resp) {
        if (resp == null) {
            return null;
        }
        Integer code = null;
        try {
            code = resp.getInteger("code");
        } catch (Exception ignored) {
        }
        if (code != null && code != 0) {
            return null;
        }

        Object outputObj = null;
        JSONObject data = resp.getJSONObject("data");
        if (data != null) {
            outputObj = data.get("output");
            if (outputObj == null) {
                outputObj = data.get("data");
            }
            if (outputObj == null) {
                outputObj = data.get("result");
            }
        }
        if (outputObj == null) {
            outputObj = resp.get("output");
        }
        if (outputObj == null) {
            outputObj = resp.get("data");
        }

        String direct = coerceToText(outputObj);
        if (StringUtils.isBlank(direct)) {
            String deep = deepFindFirstString(outputObj != null ? outputObj : (data != null ? data : resp));
            return StringUtils.isBlank(deep) ? null : deep.trim();
        }

        String v = direct.trim();
        String extracted = extractFromNestedOutput(v);
        if (StringUtils.isNotBlank(extracted)) {
            return extracted.trim();
        }
        String dataStr = null;
        try {
            dataStr = resp.getString("data");
        } catch (Exception ignored) {
        }
        if (StringUtils.isNotBlank(dataStr) && looksLikeJson(dataStr.trim())) {
            try {
                JSONObject inner = JSON.parseObject(dataStr.trim());
                String pref = tryPreferredKeys(inner);
                if (StringUtils.isNotBlank(pref)) {
                    return pref.trim();
                }
                String any = firstStringValue(inner);
                if (StringUtils.isNotBlank(any)) {
                    return any.trim();
                }
            } catch (Exception ignored) {
            }
        }
        return v;
    }

    private String quickExtractFromDataString(String s) {
        try {
            String t = s == null ? null : s.trim();
            if (StringUtils.isBlank(t)) {
                return null;
            }
            if (looksLikeJson(t)) {
                JSONObject inner = JSON.parseObject(t);
                String o = inner.getString("output");
                if (StringUtils.isNotBlank(o)) {
                    return o.trim();
                }
                String any = firstStringValue(inner);
                return StringUtils.isBlank(any) ? null : any.trim();
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }
    private String truncateForLog(String s, int max) {
        if (s == null) {
            return null;
        }
        if (max <= 0) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
    private String deepFindFirstString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            String s = ((String) obj).trim();
            return StringUtils.isBlank(s) ? null : s;
        }
        if (obj instanceof JSONObject) {
            JSONObject o = (JSONObject) obj;
            String p = tryPreferredKeys(o);
            if (StringUtils.isNotBlank(p)) {
                return p.trim();
            }
            for (String k : o.keySet()) {
                Object v = o.get(k);
                String r = deepFindFirstString(v);
                if (StringUtils.isNotBlank(r)) {
                    return r.trim();
                }
            }
            return null;
        }
        if (obj instanceof JSONArray) {
            JSONArray arr = (JSONArray) obj;
            for (int i = 0; i < arr.size(); i++) {
                String r = deepFindFirstString(arr.get(i));
                if (StringUtils.isNotBlank(r)) {
                    return r.trim();
                }
            }
            return null;
        }
        String s = String.valueOf(obj);
        s = s == null ? "" : s.trim();
        if (looksLikeJson(s)) {
            try {
                JSONObject o = JSON.parseObject(s);
                return deepFindFirstString(o);
            } catch (Exception ignored) {
            }
        }
        return StringUtils.isBlank(s) ? null : s;
    }

    private String tryPreferredKeys(JSONObject obj) {
        if (obj == null || obj.isEmpty()) {
            return null;
        }
        List<String> keys = Arrays.asList(
            "answer",
            "content",
            "text",
            "Output",
            "output",
            "result",
            "reasoning_content",
            "str_output",
            "str_reasoning_content",
            "sit_output",
            "sit_reasoning_content"
        );
        for (String k : keys) {
            Object v = obj.get(k);
            String s = coerceToText(v);
            if (StringUtils.isNotBlank(s)) {
                return s.trim();
            }
        }
        return null;
    }
    private String extractFromNestedOutput(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        String v = text.trim();
        if (!looksLikeJson(v)) {
            return null;
        }
        try {
            JSONObject obj = JSON.parseObject(v);
            Object out = obj.get("Output");
            if (out == null) {
                out = obj.get("output");
            }
            if (out == null) {
                out = obj.get("result");
            }
            if (out == null) {
                out = obj.get("reasoning_content");
            }
            if (out == null) {
                String maybe = firstStringValue(obj);
                return StringUtils.isBlank(maybe) ? null : maybe;
            }
            String outText = coerceToText(out);
            if (StringUtils.isBlank(outText)) {
                return null;
            }
            String outTrim = outText.trim();
            if (looksLikeJson(outTrim)) {
                JSONObject inner = JSON.parseObject(outTrim);
                String data = inner.getString("data");
                if (StringUtils.isNotBlank(data)) {
                    return data;
                }
                String answer = inner.getString("answer");
                if (StringUtils.isNotBlank(answer)) {
                    return answer;
                }
                String rc = inner.getString("reasoning_content");
                if (StringUtils.isNotBlank(rc)) {
                    return rc;
                }
                String any = firstStringValue(inner);
                return StringUtils.isBlank(any) ? outTrim : any;
            }
            return outTrim;
        } catch (Exception ignored) {
            return null;
        }
    }

    private boolean looksLikeJson(String text) {
        if (text == null) {
            return false;
        }
        String t = text.trim();
        return (t.startsWith("{") && t.endsWith("}")) || (t.startsWith("[") && t.endsWith("]"));
    }

    private String coerceToText(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof JSONObject) {
            return ((JSONObject) obj).toJSONString();
        }
        if (obj instanceof JSONArray) {
            return ((JSONArray) obj).toJSONString();
        }
        return String.valueOf(obj);
    }

    private String firstStringValue(JSONObject obj) {
        if (obj == null || obj.isEmpty()) {
            return null;
        }
        for (String k : obj.keySet()) {
            Object v = obj.get(k);
            if (v instanceof String && StringUtils.isNotBlank((String) v)) {
                return ((String) v).trim();
            }
        }
        return null;
    }

    private JSONArray parseTicketMessages(String messagesJson) {
        if (StringUtils.isBlank(messagesJson)) {
            return new JSONArray();
        }
        try {
            JSONArray arr = JSONArray.parseArray(messagesJson);
            return arr == null ? new JSONArray() : arr;
        } catch (Exception ignored) {
            return new JSONArray();
        }
    }

    private JSONObject buildTicketMessage(SupportTicketEntity ticket, String senderRole, Long senderId, String content, Date addtime, JSONArray existing) {
        JSONObject msg = new JSONObject();
        msg.put("id", nextMessageId(existing));
        msg.put("senderRole", senderRole == null ? "" : senderRole.trim());
        msg.put("senderId", senderId);
        msg.put("content", content);
        msg.put("addtime", formatTicketMessageTime(addtime));
        normalizeSenderRole(msg, ticket == null ? null : ticket.getUserId());
        return msg;
    }

    private void normalizeSenderRole(JSONObject msg, Long ticketUserId) {
        if (msg == null) {
            return;
        }
        String r = msg.getString("senderRole");
        r = r == null ? "" : r.trim();
        String rl = r.toLowerCase();

        Long senderId = null;
        try {
            senderId = msg.getLong("senderId");
        } catch (Exception ignored) {
        }
        boolean isUserById = senderId != null && ticketUserId != null && ticketUserId.equals(senderId);
        if (StringUtils.isBlank(r)) {
            msg.put("senderRole", isUserById ? "用户" : "员工");
            return;
        }

        if ("用户".equals(r) || "user".equals(rl)) {
            msg.put("senderRole", "用户");
        } else if ("管理员".equals(r) || "admin".equals(rl)) {
            msg.put("senderRole", "管理员");
        } else if ("员工".equals(r) || "staff".equals(rl)) {
            msg.put("senderRole", "员工");
        } else if (isUserById) {
            msg.put("senderRole", "用户");
        } else {
            msg.put("senderRole", r);
        }
    }

    private String formatTicketMessageTime(Date d) {
        if (d == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
    }

    private Long nextMessageId(JSONArray existing) {
        long id = System.currentTimeMillis() * 1000L + (long) (Math.random() * 1000);
        if (existing == null || existing.isEmpty()) {
            return id;
        }
        HashSet<Long> used = new HashSet<>();
        for (int i = 0; i < existing.size(); i++) {
            Object o = existing.get(i);
            if (!(o instanceof JSONObject)) {
                continue;
            }
            JSONObject m = (JSONObject) o;
            try {
                Long mid = m.getLong("id");
                if (mid != null) {
                    used.add(mid);
                }
            } catch (Exception ignored) {
            }
        }
        while (used.contains(id)) {
            id++;
        }
        return id;
    }

    
}
