package site.lanmushan.slashdocdemo.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Administrator
 */
public class SearchKeyRequest extends PageRequest {
    @ApiModelProperty(value = "查询关键字")
    protected String searchKey;
}
