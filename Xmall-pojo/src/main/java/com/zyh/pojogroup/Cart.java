package com.zyh.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.zyh.pojo.TbOrderItem;
/**
 * 璐墿杞﹀璞�
 * @author Administrator
 *
 */
public class Cart implements Serializable{

	private String sellerId;//鍟嗗ID
	private String sellerName;//鍟嗗鍚嶇О
	
	private List<TbOrderItem> orderItemList;//璐墿杞︽槑缁嗛泦鍚�

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public List<TbOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sellerId == null) ? 0 : sellerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cart other = (Cart) obj;
		if (sellerId == null) {
			if (other.sellerId != null)
				return false;
		} else if (!sellerId.equals(other.sellerId))
			return false;
		return true;
	}
	
	
	
}
