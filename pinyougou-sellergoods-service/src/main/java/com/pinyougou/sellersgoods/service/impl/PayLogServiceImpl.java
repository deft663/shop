package com.pinyougou.sellersgoods.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojo.TbPayLogExample;
import com.pinyougou.pojo.TbPayLogExample.Criteria;
import com.pinyougou.sellergoods.service.PayLogService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class PayLogServiceImpl implements PayLogService {

	@Autowired
	private TbPayLogMapper payLogMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbPayLog> findAll() {
		return payLogMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbPayLog> page=   (Page<TbPayLog>) payLogMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbPayLog payLog) {
		payLogMapper.insert(payLog);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbPayLog payLog){
		payLogMapper.updateByPrimaryKey(payLog);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbPayLog findOne(String id){
		return payLogMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(String[] ids) {
		for(String id:ids){
			payLogMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbPayLog payLog, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbPayLogExample example=new TbPayLogExample();
		Criteria criteria = example.createCriteria();
		
		if(payLog!=null){			
				
		}
		
		Page<TbPayLog> page= (Page<TbPayLog>)payLogMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
