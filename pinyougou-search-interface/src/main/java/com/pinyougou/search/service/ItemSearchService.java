package com.pinyougou.search.service;
import  java.util.*;
public interface ItemSearchService {
    /**
     * 搜索
     * @param searchMap 条件
     * @return
     */
    public Map<String,Object> search(Map searchMap);
    /**
     * 导入数据
     * @param list
     */
    public void importList(List list);
    /**
     * 删除数据
     * @param goodsIdList
     */
    public void deleteByGoodsIds(List goodsIdList);

}
