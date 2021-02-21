package com.zyh.solrutil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.zyh.mapper.TbItemMapper;
import com.zyh.pojo.TbItem;
import com.zyh.pojo.TbItemExample;
import com.zyh.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {
	@Autowired
	private TbItemMapper itemMapper;
	
	/**
	 * ������Ʒ����
	 */
	public void importItemData(){
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");//�����
		List<TbItem> itemList = itemMapper.selectByExample(example);
		System.out.println("===��Ʒ�б�===");
		for(TbItem item:itemList){
			System.out.println(item.getTitle());			
		}		
		System.out.println("===����===");			
	}	

	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();
	}
}
