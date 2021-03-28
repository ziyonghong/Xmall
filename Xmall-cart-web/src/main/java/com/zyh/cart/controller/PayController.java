package com.zyh.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.order.service.OrderService;
import com.zyh.pay.service.WeixinPayService;
import com.zyh.pojo.TbPayLog;

import entity.Result;
import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {
	
	@Reference
	private WeixinPayService weixinPayService;
	
	@Reference
	private OrderService orderService;
	
	@RequestMapping("/createNative")
	public Map createNative(){
		//1.获取当前用户
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//2.到redis查询支付日志
		TbPayLog payLog = orderService.searchPayLogFromRedis(username);
		//3.判断支付日志存在
		if(payLog!=null){
			return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");		
		}else{
			return new HashMap<>();
		}		
	}

	
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no){
		Result result=null;
		int x=0;
		while(true){
			
			Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);//璋冪敤鏌ヨ
			if(map==null){
				result=new Result(false, "鏀粯鍙戠敓閿欒");
				break;
			}
			if(map.get("trade_state").equals("SUCCESS")){//如果成功
				result=new Result(true, "鏀粯鎴愬姛");				
				orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));//修改订单状态
				break;
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为5分钟
			x++;
			if(x>=100){				
				result=new Result(false, "二维码超时");
				break;				
			}
			
		}
		return result;
	}
	
	
}
