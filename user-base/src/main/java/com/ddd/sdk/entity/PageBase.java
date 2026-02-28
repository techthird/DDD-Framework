package com.ddd.sdk.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分页请求基类
 */
@Data
public class PageBase {
    @ApiModelProperty(value = "当前页，从第1页开始，不传默认为1")
    private Long currentPage = 1L;

    @ApiModelProperty(value = "每页显示条数，不传默认20")
    private Long pageSize = 20L;

    @SuppressWarnings("unused")
    private Long getOffset() {
        return (currentPage - 1L) * pageSize;
    }
}
