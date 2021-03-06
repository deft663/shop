package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbOrderItemExample;
import com.pinyougou.pojogroup.Cart;
import com.pinyougou.pojogroup.Order;
import entity.PageResult;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import util.IdWorker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisTemplate<String, Object>redisTemplate;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加
     */
    @Override
    public void add(TbOrder order) {
        //得到购物车数据
        List<Cart>cartList = (List<Cart>)
                redisTemplate.boundHashOps("cartList").get( order.getUserId() );
        for(Cart cart:cartList){
            long orderId = idWorker.nextId();
            System.out.println("sellerId:"+cart.getSellerId());
            TbOrder tborder=new TbOrder();//新创建订单对象
            tborder.setOrderId(orderId);//订单ID
            tborder.setUserId(order.getUserId());//用户名
            tborder.setPaymentType(order.getPaymentType());//支付类型
            tborder.setStatus("1");//状态：未付款
            tborder.setCreateTime(new Date());//订单创建日期
            tborder.setUpdateTime(new Date());//订单更新日期
            tborder.setReceiverAreaName(order.getReceiverAreaName());//地址
            tborder.setReceiverMobile(order.getReceiverMobile());//手机号
            tborder.setReceiver(order.getReceiver());//收货人
            tborder.setSourceType(order.getSourceType());//订单来源
            tborder.setSellerId(cart.getSellerId());//商家ID
            //循环购物车明细
            Double money=0.0;
            for(TbOrderItem orderItem :cart.getOrderItemList()){
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId( orderId  );//订单ID
                orderItem.setSellerId(cart.getSellerId());
                money+=orderItem.getTotalFee().doubleValue();//金额累加
                orderItemMapper.insert(orderItem);
            }
            tborder.setPayment(new BigDecimal(money));
            orderMapper.insert(tborder);
        }
        redisTemplate.boundHashOps("cartList").delete(order.getUserId());
    }


	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(String username,int pageNum, int pageSize) {
	    List<Order> orders=new ArrayList<>();
		PageHelper.startPage(pageNum, pageSize);
		TbOrderExample example=new TbOrderExample();
        Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(username);
        Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(example);
        for (TbOrder tbOrder : page.getResult()) {
            Order o=new Order();
            o.setSellerId(tbOrder.getSellerId());
            o.setSellerName(username);
            o.setTbOrder(tbOrder);
            Long orderId = tbOrder.getOrderId();
            TbOrderItemExample tbOrderItemExample=new TbOrderItemExample();
            tbOrderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<TbOrderItem> tbOrderItemList = orderItemMapper.selectByExample(tbOrderItemExample);
            o.setTbOrderItemList(tbOrderItemList);
            orders.add(o);
        }
        return new PageResult(page.getTotal(), orders);
	}



	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
}
