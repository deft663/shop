package com.pinyougou.search.service;
import  java.util.Map;
public interface ItemSearchService {
    /**
     * 搜索
     * @param searchMap 条件
     * @return
     */
    public Map<String,Object> search(Map searchMap);

}
