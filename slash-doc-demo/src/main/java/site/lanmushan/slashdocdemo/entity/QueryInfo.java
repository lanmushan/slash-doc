package site.lanmushan.slashdocdemo.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用查询参数封装
 *
 * @author Administrator
 */
@ApiModel
public class QueryInfo extends SearchKeyRequest implements Serializable {
    /**
     * feign 特殊处理时的标记
     */
    @ApiModelProperty(hidden = true)
    private String queryInfoMark = infoMark.mark.value;
    @ApiModelProperty(hidden = true)
    private Map<String, Object> map = new HashMap<String, Object>(0);
    //前端传参
    @ApiModelProperty(hidden = true)
    @ApiParam(hidden = true)
    private List<QueryParam> parList = new ArrayList<QueryParam>(0);

    public QueryInfo() {

    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }


    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Integer getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }


    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<QueryParam> getParList() {
        return parList;
    }

    public void setParList(List<QueryParam> parList) {
        this.parList = parList;
    }

    public String getQueryInfoMark() {
        return queryInfoMark;
    }

    public void setQueryInfoMark(String queryInfoMark) {
        this.queryInfoMark = queryInfoMark;
    }

    public void addCondition(String key, String operation, String value) {
        QueryParam queryParam = new QueryParam();
        queryParam.setKey(key);
        queryParam.setOperate(operation);
        queryParam.setValue(value);
        this.getParList().add(queryParam);
    }

    public void addCondition(String key, String value) {
        QueryParam queryParam = new QueryParam();
        queryParam.setKey(key);
        queryParam.setOperate("=");
        queryParam.setValue(value);
        this.getParList().add(queryParam);
    }

    /**
     * 查询参数转换
     *
     * @author Administrator
     */
    public static class QueryParam {
        private String key;
        private Object value;
        private String operate;
        private int index;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getOperate() {
            return operate;
        }

        public void setOperate(String operate) {
            this.operate = operate;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public QueryParam() {
        }
    }

    public enum infoMark {
        /**
         * 标记
         */
        mark("queryInfoMark", "queryInfoMark");
        public String key;
        public String value;

        infoMark(String key, String value) {
            this.key = key;
            this.value = value;

        }
    }
}

