package site.lanmushan.slashdocstarter.api;

import lombok.Data;

/**
 * @author dy
 */
@Data
public class ApiMapping {
    private Object key;
    private Object describe;

    public ApiMapping(Object key, Object describe) {
        this.key = key;
        this.describe = describe;
    }

    @Override
    public String toString() {
        if (key == null) {
            key = "";
        }
        if (describe == null) {
            describe = "";
        }
        return key.toString() + ":" + describe.toString();
    }
}
