package com.zyh.search.service.impl;

import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zyh.pojo.TbItem;
import com.zyh.search.service.ItemSearchService;

@Component
public class ItemSearchListener implements MessageListener{
	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		System.out.println("�������յ���Ϣ...");
		try {
			TextMessage textMessage=(TextMessage)message;			
			String text = textMessage.getText();
			List<TbItem> list = JSON.parseArray(text,TbItem.class);
			for(TbItem item:list){
				System.out.println(item.getId()+" "+item.getTitle());
				Map specMap= JSON.parseObject(item.getSpec());//��spec�ֶ��е�json�ַ���ת��Ϊmap
				item.setSpecMap(specMap);//����ע����ֶθ�ֵ
			}			
			itemSearchService.importList(list);//����	
			System.out.println("�ɹ����뵽������");
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}


}