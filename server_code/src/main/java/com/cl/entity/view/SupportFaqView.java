package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.SupportFaqEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@TableName("faq")
public class SupportFaqView extends SupportFaqEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public SupportFaqView() {
    }

    public SupportFaqView(SupportFaqEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
