package com.zyh.cart.service;

import java.util.List;

import com.zyh.pojogroup.Cart;

/**
 * 璐墿杞︽湇鍔℃帴鍙�
 * @author Administrator
 *
 */
public interface CartService {

	/**
	 * 娣诲姞鍟嗗搧鍒拌喘鐗╄溅
	 * @param list
	 * @param itemId
	 * @param num
	 * @return
	 */
	public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);
	
	/**
	 * 浠巖edis涓彁鍙栬喘鐗╄溅鍒楄〃
	 * @param username
	 * @return
	 */
	public List<Cart> findCartListFromRedis(String username);
	
	/**
	 * 灏嗚喘鐗╄溅鍒楄〃瀛樺叆redis
	 * @param username
	 * @param cartList
	 */
	public void saveCartListToRedis(String username,List<Cart> cartList);
	
	/**
	 * 鍚堝苟璐墿杞�
	 * @param cartList1
	 * @param cartList2
	 * @return
	 */
	public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
	 
}
