package org.zhangrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zhangrui.model.entity.Address;

/**
 * 收货地址Mapper接口
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}
