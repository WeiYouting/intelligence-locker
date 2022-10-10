package com.wyt.intelligencelocker.controller.util;

import lombok.Builder;
import lombok.Data;

/**
 * @Author WeiYouting
 * @create 2022/10/2 23:25
 * @Email Wei.youting@qq.com
 */
@Data
@Builder
public class PageResult<T> {

    private Integer pageIndex;

    private Integer pageSize;

    private Long startRow;

    private Long endRow;

    private Long total;

    private Integer pages;

    private T data;
}
