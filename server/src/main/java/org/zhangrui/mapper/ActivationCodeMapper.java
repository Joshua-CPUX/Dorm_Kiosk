package org.zhangrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zhangrui.model.entity.ActivationCode;

/**
 * 激活码Mapper接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Mapper
public interface ActivationCodeMapper extends BaseMapper<ActivationCode> {
}
