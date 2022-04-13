## 简介 
基于knife4j,swagger增强处理
## 使用方法

## 增强内容
### springmvc增强
直接在PostMapping，GetMapping写入name名称即可，省去原来的@ApiOperation注解，如果写了取原始的名称
```java
   @PostMapping(name = "测试测试", value = "/test")
    public void PostMappingSaveGroup(@RequestBody SysTbApp sysTbApp, @RequestParam String test) {
        return;
    }
```
### @Validated和@Valid校验增强,
根据方法中@Validated和@Valid的注解，判断实体类内部相关校验注解
```java
    @PostMapping(name = "Validated分组测试UpdateGroup", value = "/PostMappingUpdateGroup")
    public void PostMappingUpdateGroup(@RequestBody @Validated(UpdateGroup.class)
                                               SysTbApp sysTbApp, @RequestParam String test) {
        return;
    }

    @PostMapping(name = "Valid测试", value = "/PostMappingValid")
    public void PostMapping3(@RequestBody @Valid SysTbApp sysTbApp, @RequestParam String test) {
        return;
    }
```
### 字典映射增强
在属性上增加 @ApiModelEnumMapping注解,可将枚举值直接映射到doc中
```java
@Data
@ApiModel
public class SysTbApp {
    private String appWebUrl;
    @ApiModelProperty(value = "接口地址")
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
```
```java
package site.lanmushan.slashdocdemo.enums;

import site.lanmushan.slashdocstarter.api.ApiMapping;
import site.lanmushan.slashdocstarter.api.ApiModelEnumMappingProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dy
 */

public enum EnabledEnum implements ApiModelEnumMappingProvider {
    /**
     * 测试
     */
    enable(1, "启用"),
    disabled(0, "禁用");
    private int type;
    private String value;

    EnabledEnum(int s, String s1) {
        this.type = s;
        this.value = s1;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public List<ApiMapping> readMapping() {
        EnabledEnum[] enabledEnums = EnabledEnum.values();
        List<ApiMapping> arrList = new ArrayList<>();
        for (int i = 0; i < enabledEnums.length; i++) {
            EnabledEnum userTypeEnum = enabledEnums[i];
            arrList.add(new ApiMapping(userTypeEnum.type, userTypeEnum.value));
        }
        return arrList;
    }
}

```