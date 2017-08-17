package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import redis.clients.jedis.JedisCluster;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Value("${ORDER_ID_GEN_KEY")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_START")
    private String ORDER_ID_START;
    @Value("${ORDER_DETAIL_ID_GEN_KEY")
    private String ORDER_DETAIL_ID_GEN_KEY;
    @Autowired
    private JedisCluster jedisCluster;
    /**
     * 生成订单
     */
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		if(!jedisCluster.exists(ORDER_ID_GEN_KEY)){
			jedisCluster.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisCluster.incr(ORDER_ID_GEN_KEY).toString();
		orderInfo.setOrderId(orderId);
		//1:未付款
		orderInfo.setStatus(1);
		orderInfo.setPostFee("0");
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		orderMapper.insert(orderInfo);
		/**
		 * 订单商品详情
		 */
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			Long tbOrderItemId = jedisCluster.incr(ORDER_DETAIL_ID_GEN_KEY);
			tbOrderItem.setId(tbOrderItemId.toString());
			tbOrderItem.setItemId(orderId);
			orderItemMapper.insert(tbOrderItem);
		}
		/**
		 * 接受地址等信息
		 */
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		return E3Result.ok(orderId);
	}

}
