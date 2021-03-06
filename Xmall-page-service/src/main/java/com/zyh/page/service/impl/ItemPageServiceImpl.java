package com.zyh.page.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;


import com.zyh.mapper.TbGoodsDescMapper;
import com.zyh.mapper.TbGoodsMapper;
import com.zyh.mapper.TbItemCatMapper;
import com.zyh.mapper.TbItemMapper;
import com.zyh.page.service.ItemPageService;
import com.zyh.pojo.TbGoods;
import com.zyh.pojo.TbGoodsDesc;
import com.zyh.pojo.TbItem;
import com.zyh.pojo.TbItemExample;
import com.zyh.pojo.TbItemExample.Criteria;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageService {

	@Value("${pagedir}")
	private String pagedir;
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbItemMapper itemMapper;
		
	@Override
	public boolean genItemHtml(Long goodsId){				
		try {
			Configuration configuration = freeMarkerConfig.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Map dataModel=new HashMap<>();			
			//1.������Ʒ������
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);			
			//2.������Ʒ��չ������			
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);						
			
			//3.��Ʒ����
			String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat1", itemCat1);
			dataModel.put("itemCat2", itemCat2);
			dataModel.put("itemCat3", itemCat3);
			
			//4.SKU�б�			
			TbItemExample example=new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andStatusEqualTo("1");//״̬Ϊ��Ч
			criteria.andGoodsIdEqualTo(goodsId);//ָ��SPU ID
			example.setOrderByClause("is_default desc");//����״̬���򣬱�֤��һ��ΪĬ��			
			List<TbItem> itemList = itemMapper.selectByExample(example);		
			dataModel.put("itemList", itemList);
			
			Writer out=new FileWriter(pagedir+goodsId+".html");
			template.process(dataModel, out);
			out.close();
			return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteItemHtml(Long[] goodsIds) {
		try {
			for(Long goodsId:goodsIds){
				new File(pagedir+goodsId+".html").delete();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
}
