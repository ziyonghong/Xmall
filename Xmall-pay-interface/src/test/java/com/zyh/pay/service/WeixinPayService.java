package com.zyh.pay.service;

import java.util.Map;

public interface WeixinPayService {

	/**
	 * 鐢熸垚浜岀淮鐮�
	 * @param out_trade_no
	 * @param total_fee
	 * @return
	 */
	public Map createNative(String out_trade_no,String total_fee);
	
	/**
	 * 鏌ヨ鏀粯璁㈠崟鐘舵��
	 * @param out_trade_no
	 * @return
	 */
	public Map queryPayStatus(String out_trade_no);
	
	
	/**
	 * 鍏抽棴璁㈠崟
	 * @param out_trade_no
	 * @return
	 */
	public Map closePay(String out_trade_no);
	
}
