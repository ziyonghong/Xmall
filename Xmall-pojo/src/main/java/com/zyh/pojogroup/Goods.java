package com.zyh.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.zyh.pojo.TbGoods;
import com.zyh.pojo.TbGoodsDesc;
import com.zyh.pojo.TbItem;

//�������ʵ����
public class Goods implements Serializable{
	private TbGoods goods;//��ƷSPU
	private TbGoodsDesc goodsDesc;//��Ʒ��չ
	private List<TbItem> itemList;//��ƷSKU�б�	
	public TbGoods getGoods() {
		return goods;
	}
	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}
	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public List<TbItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}
	
	
}
