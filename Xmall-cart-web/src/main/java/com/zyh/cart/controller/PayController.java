//package com.zyh.cart.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.zyh.order.service.OrderService;
//import com.zyh.pay.service.WeixinPayService;
//import com.zyh.pojo.TbPayLog;
//
//import entity.Result;
//import util.IdWorker;
//
//@RestController
//@RequestMapping("/pay")
//public class PayController {
//	
//	@Reference
//	private WeixinPayService weixinPayService;
//	
//	@Reference
//	private OrderService orderService;
//	
//	@RequestMapping("/createNative")
//	public Map createNative(){
//		//1.鑾峰彇褰撳墠鐧诲綍鐢ㄦ埛
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		//2.鎻愬彇鏀粯鏃ュ織锛堜粠缂撳瓨 锛�
//		TbPayLog payLog = orderService.searchPayLogFromRedis(username);
//		//3.璋冪敤寰俊鏀粯鎺ュ彛
//		if(payLog!=null){
//			return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");		
//		}else{
//			return new HashMap<>();
//		}		
//	}
//
//	
//	@RequestMapping("/queryPayStatus")
//	public Result queryPayStatus(String out_trade_no){
//		Result result=null;
//		int x=0;
//		while(true){
//			
//			Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);//璋冪敤鏌ヨ
//			if(map==null){
//				result=new Result(false, "鏀粯鍙戠敓閿欒");
//				break;
//			}
//			if(map.get("trade_state").equals("SUCCESS")){//鏀粯鎴愬姛
//				result=new Result(true, "鏀粯鎴愬姛");				
//				orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));//淇敼璁㈠崟鐘舵��
//				break;
//			}
//			
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			x++;
//			if(x>=100){				
//				result=new Result(false, "浜岀淮鐮佽秴鏃�");
//				break;				
//			}
//			
//		}
//		return result;
//	}
//	
//	
//}
