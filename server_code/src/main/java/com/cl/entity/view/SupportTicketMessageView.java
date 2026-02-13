package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.SupportTicketMessageEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@TableName("support_ticket_message")
public class SupportTicketMessageView extends SupportTicketMessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public SupportTicketMessageView() {
    }

    public SupportTicketMessageView(SupportTicketMessageEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

