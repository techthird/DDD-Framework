package com.ddd.sdk.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {

    @ApiModelProperty(value = "总条数")
    private Long totalCount;

    @ApiModelProperty(value = "总页数")
    private Long totalPage;

    @ApiModelProperty(value = "数据")
    @Builder.Default
    private List<T> data = new ArrayList<>();


}
