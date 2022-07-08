package site.lanmushan.slashdocdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import site.lanmushan.slashdocdemo.enums.EnabledEnum;
import site.lanmushan.slashdocstarter.annotations.ApiModelEnumMapping;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author dy
 */
@Data
@ApiModel("xxx")
public class SysTbApp {
    private String appWebUrl;
    @ApiModelProperty(value = "接口地址")
    @NotBlank
    private String appApiUrl;
    @ApiModelProperty(value = "启用状态", required = true)
    @ApiModelEnumMapping(value = EnabledEnum.class)
    private Integer enabled;
    @ApiModelProperty(value = "字典")
    private SysTbDict tbDict;
    @ApiModelProperty("测试范围")
    @Min(value = 10)
    private Integer aaa;
}
