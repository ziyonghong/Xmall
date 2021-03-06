package com.zyh.page.service.impl;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zyh.page.service.ItemPageService;

// �����ࣨ����������ҳ��
@Component
public class PageListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;


	@Override
	public void onMessage(Message message) {
		TextMessage textMessage= (TextMessage)message;
		try {
			String text = textMessage.getText();
			System.out.println("���յ���Ϣ��"+text);
			boolean b = itemPageService.genItemHtml(Long.parseLong(text));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}

}
