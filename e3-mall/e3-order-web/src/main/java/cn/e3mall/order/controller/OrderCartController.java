package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderCartController {
   
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	/**
	 * 展示订单确认页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		
	    TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> cartList = cartService.getCartList(user.getId());
		request.setAttribute("cartList", cartList);
		return "order-cart";
		
	}
	/**
	 * 确认订单
	 * @param orderInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/create",method = RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo ,HttpServletRequest request){
		
		TbUser user = (TbUser) request.getAttribute("user");
		
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		
		E3Result result = orderService.createOrder(orderInfo);
		String  orderId = result.getData().toString();
		request.setAttribute("orderId", orderId);
		request.setAttribute("payment", orderInfo.getPayment());
		
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
		
		return "success";
		
	}
}
