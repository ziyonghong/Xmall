package com.zyh.page.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zyh.page.service.ItemPageService;

@Component
public class PageDeleteListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {		
		ObjectMessage objectMessage= (ObjectMessage)message;		
		try {
			Long[] goodsIds = (Long[]) objectMessage.getObject();
			System.out.println("ItemDeleteListener�������յ���Ϣ..."+goodsIds);
			boolean b = itemPageService.deleteItemHtml(goodsIds);
			System.out.println("��ҳɾ�������"+b);			
		} catch (JMSException e) {
			e.printStackTrace();
		}			
	}
}