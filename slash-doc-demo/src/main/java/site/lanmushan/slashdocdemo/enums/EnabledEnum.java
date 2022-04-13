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
