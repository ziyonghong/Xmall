package com.zyh.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.zyh.cart.service.CartService;
import com.zyh.mapper.TbItemMapper;
import com.zyh.pojo.TbItem;
import com.zyh.pojo.TbOrderItem;
import com.zyh.pojogroup.Cart;
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		
		//1.根据商品SKU ID查询SKU商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if(item==null){
			throw new RuntimeException("商品不存在");
		}
		if(!item.getStatus().equals("1")){
			throw new RuntimeException("商品状态无效");
		}		
		//2.获取商家ID
		String sellerId = item.getSellerId();//鍟嗗ID
		
		//3.根据商家ID判断购物车列表中是否存在该商家的购物车
		Cart cart = searchCartBySellerId(cartList,sellerId);
		
		if(cart==null){//4.如果购物车列表中不存在该商家的购物车
			
			//4.1 新建购物车对象
			cart=new Cart();
			cart.setSellerId(sellerId);//鍟嗗ID
			cart.setSellerName(item.getSeller());//鍟嗗鍚嶇О			
			List<TbOrderItem> orderItemList=new ArrayList();//鍒涘缓璐墿杞︽槑缁嗗垪琛�
			TbOrderItem orderItem = createOrderItem(item,num);			
			orderItemList.add(orderItem);			
			cart.setOrderItemList(orderItemList);
			
			//4.2将购物车对象添加到购物车列表
			cartList.add(cart);
			
		}else{//5.如果购物车列表中存在该商家的购物车			
			// 判断购物车明细列表中是否存在该商品
			TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(),itemId);
			if(orderItem==null){
				//5.1  如果没有，新增购物车明细
				orderItem=createOrderItem(item,num);
				cart.getOrderItemList().add(orderItem);				
				
			}else{
				//5.2 如果有，在原购物车明细上添加数量，更改金额
				orderItem.setNum(orderItem.getNum()+num);//鏇存敼鏁伴噺
				//閲戦
				orderItem.setTotalFee( new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum() ) );
				//如果数量操作后小于等于0，则移除
				if(orderItem.getNum()<=0){
					cart.getOrderItemList().remove(orderItem);					
				}
				//如果移除后cart的明细数量为0，则将cart移除
				if(cart.getOrderItemList().size()==0){
					cartList.remove(cart);
				}				
			}
			
		}
		
		return cartList;
	}
	
	/**
	 * 鏍规嵁鍟嗗ID鍦ㄨ喘鐗╄溅鍒楄〃涓煡璇㈣喘鐗╄溅瀵硅薄
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
		for(Cart cart:cartList){
			if(cart.getSellerId().equals(sellerId)){
				return cart;
			}
		}
		return null;		
	}
	
	/**
	 * 鏍规嵁skuID鍦ㄨ喘鐗╄溅鏄庣粏鍒楄〃涓煡璇㈣喘鐗╄溅鏄庣粏瀵硅薄
	 * @param orderItemList
	 * @param itemId
	 * @return
	 */
	public TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList,Long itemId){
		for(TbOrderItem orderItem:orderItemList){
			if(orderItem.getItemId().longValue()==itemId.longValue()){
				return orderItem;
			}			
		}
		return null;
	}
	
	/**
	 * 鍒涘缓璐墿杞︽槑缁嗗璞�
	 * @param item
	 * @param num
	 * @return
	 */
	private TbOrderItem createOrderItem(TbItem item,Integer num){
		//鍒涘缓鏂扮殑璐墿杞︽槑缁嗗璞�
		TbOrderItem orderItem=new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setSellerId(item.getSellerId());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(  new BigDecimal(item.getPrice().doubleValue()*num) );
		return orderItem;
	}
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<Cart> findCartListFromRedis(String username) {
		System.out.println("浠巖edis涓彁鍙栬喘鐗╄溅"+username);
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
		if(cartList==null){
			cartList=new ArrayList();
		}		
		return cartList;
	}

	@Override
	public void saveCartListToRedis(String username, List<Cart> cartList) {
		System.out.println("鍚憆edis涓瓨鍏ヨ喘鐗╄溅"+username);
		redisTemplate.boundHashOps("cartList").put(username, cartList);
		
	}

	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
		// cartList1.addAll(cartList2);  涓嶈兘绠�鍗曞悎骞� 		
		for(Cart cart:cartList2){
			for( TbOrderItem orderItem :cart.getOrderItemList() ){
				cartList1=addGoodsToCartList(cartList1,orderItem.getItemId(),orderItem.getNum());
			}
		}
		return cartList1;		
	}

}
