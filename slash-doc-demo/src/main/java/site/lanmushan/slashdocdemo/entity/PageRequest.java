package site.lanmushan.slashdocdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
@ApiModel
public class PageRequest {
    @ApiModelProperty(value = "分页大小")
    protected Integer pageSize = 10;
    @ApiModelProperty(value = "当前页面")
    protected Integer currentPage = 1;
    @ApiModelProperty(value = "排序方式")
    private String sort = "desc";
    @ApiModelProperty(value = "排序字段")
    private String fixed = "id";

}
