package com.zyh.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.zyh.pay.service.WeixinPayService;

import util.HttpClient;
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

	@Value("${appid}")
	private String appid;
	
	@Value("${partner}")
	private String partner;
	
	@Value("${partnerkey}")
	private String partnerkey;
	
	@Override
	public Map createNative(String out_trade_no, String total_fee) {
		//1.鍙傛暟灏佽
		Map param=new HashMap();
		param.put("appid", appid);//鍏紬璐﹀彿ID
		param.put("mch_id", partner);//鍟嗘埛
		param.put("nonce_str", WXPayUtil.generateNonceStr());//闅忔満瀛楃涓�
		param.put("body", "鍝佷紭璐�");
		param.put("out_trade_no", out_trade_no);//浜ゆ槗璁㈠崟鍙�
		param.put("total_fee", total_fee);//閲戦锛堝垎锛�
		param.put("spbill_create_ip", "127.0.0.1");
		param.put("notify_url", "http://www.itcast.cn");
		param.put("trade_type", "NATIVE");//浜ゆ槗绫诲瀷
			
		try {
			String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
			System.out.println("璇锋眰鐨勫弬鏁帮細"+xmlParam);
			
			//2.鍙戦�佽姹�			
			HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			httpClient.setHttps(true);
			httpClient.setXmlParam(xmlParam);
			httpClient.post();
			
			//3.鑾峰彇缁撴灉
			String xmlResult = httpClient.getContent();
			
			Map<String, String> mapResult = WXPayUtil.xmlToMap(xmlResult);
			System.out.println("寰俊杩斿洖缁撴灉"+mapResult);
			Map map=new HashMap<>();
			map.put("code_url", mapResult.get("code_url"));//鐢熸垚鏀粯浜岀淮鐮佺殑閾炬帴
			map.put("out_trade_no", out_trade_no);
			map.put("total_fee", total_fee);
			
			return map;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HashMap();
		}
		
	}

	@Override
	public Map queryPayStatus(String out_trade_no) {
		//1.灏佽鍙傛暟
		Map param=new HashMap();
		param.put("appid", appid);
		param.put("mch_id", partner);
		param.put("out_trade_no", out_trade_no);
		param.put("nonce_str", WXPayUtil.generateNonceStr());
		try {
			String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
			//2.鍙戦�佽姹�
			HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			httpClient.setHttps(true);
			httpClient.setXmlParam(xmlParam);
			httpClient.post();
			
			//3.鑾峰彇缁撴灉
			String xmlResult = httpClient.getContent();
			Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
			System.out.println("璋冨姩鏌ヨAPI杩斿洖缁撴灉锛�"+xmlResult);
			
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Map closePay(String out_trade_no) {
		//1.灏佽鍙傛暟
			Map param=new HashMap();
			param.put("appid", appid);
			param.put("mch_id", partner);
			param.put("out_trade_no", out_trade_no);
			param.put("nonce_str", WXPayUtil.generateNonceStr());
			try {
				String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
				//2.鍙戦�佽姹�
				HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
				httpClient.setHttps(true);
				httpClient.setXmlParam(xmlParam);
				httpClient.post();
				
				//3.鑾峰彇缁撴灉
				String xmlResult = httpClient.getContent();
				Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
				System.out.println("璋冨姩鏌ヨAPI杩斿洖缁撴灉锛�"+xmlResult);
				
				return map;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}

}
