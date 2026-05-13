package org.zhangrui.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统配置实体类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
@TableName("sys_config")
public class SysConfig implements Serializable {

    @TableId
    private Long id;

    private String configKey;

    private String configValue;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
