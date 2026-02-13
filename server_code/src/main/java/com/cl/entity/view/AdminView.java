package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.AdminEntity;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

@TableName("users")
public class AdminView extends AdminEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public AdminView() {
	}

	public AdminView(AdminEntity adminEntity) {
		try {
			BeanUtils.copyProperties(this, adminEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
