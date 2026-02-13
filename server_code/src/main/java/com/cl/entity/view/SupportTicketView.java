package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.SupportTicketEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@TableName("message")
public class SupportTicketView extends SupportTicketEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public SupportTicketView() {
    }

    public SupportTicketView(SupportTicketEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
