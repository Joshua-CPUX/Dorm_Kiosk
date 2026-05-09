package org.zhangrui.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页结果封装类
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<T> {

    private Long total;
    private Long pageNum;
    private Long pageSize;
    private Long totalPages;

    private PageResult() {
        super();
    }

    private PageResult(Long total, Long pageNum, Long pageSize, T data) {
        super(200, "操作成功", data);
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (total + pageSize - 1) / pageSize;
    }

    /**
     * 构建分页结果
     *
     * @param total    总记录数
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @param data     数据列表
     * @param <T>      数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(Long total, Long pageNum, Long pageSize, T data) {
        return new PageResult<>(total, pageNum, pageSize, data);
    }
}
