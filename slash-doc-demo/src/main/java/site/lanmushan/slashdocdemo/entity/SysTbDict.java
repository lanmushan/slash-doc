package site.lanmushan.slashdocdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author $author
 */
@Data
@ApiModel
public class SysTbDict {
    private String example;
    @ApiModelProperty(value = "映射名称")
    private String dictName;

    @ApiModelProperty(value = "映射编码")
    private String dictCode;

    @ApiModelProperty(value = "所属分组")
    private String fkDictGroupCode;

    @ApiModelProperty(value = "启用")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer orderIndex;

    @ApiModelProperty(value = "允许编辑")
    private Integer allowEdit;

    @ApiModelProperty(value = "允许删除")
    private Integer allowDel;


}
