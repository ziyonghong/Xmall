package com.zyh.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.zyh.pojo.TbItem;
import com.zyh.search.service.ItemSearchService;

@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {
	@Autowired
	private SolrTemplate solrTemplate;

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public Map search(Map searchMap) {
		Map<String, Object> map = new HashMap<>();
		// 1.查询列表
		map.putAll(searchList(searchMap));
		

		// 2.根据关键字查询商品分类
		List categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		

		// 3.查询品牌和规格列表
		if (categoryList.size() > 0) {
			map.putAll(searchBrandAndSpecList(categoryList.get(0)));
		}
		
		return map;
	}

	/**
	 * 根据关键字搜索列表
	 * 
	 * @param keywords
	 * @return
	 */
	private Map searchList(Map searchMap) {
		Map map = new HashMap();
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");// 设置高亮的域
		highlightOptions.setSimplePrefix("<em style='color:red'>");// 高亮前缀
		highlightOptions.setSimplePostfix("</em>");// 高亮后缀
		query.setHighlightOptions(highlightOptions);// 设置高亮选项
		// 按照关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		for (HighlightEntry<TbItem> h : page.getHighlighted()) {// 循环高亮入口集合
			TbItem item = h.getEntity();// 获取原实体类
			if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));// 设置高亮的结果
			}
		}
		map.put("rows", page.getContent());
		return map;
	}

	/**
	 * 查询分类列表
	 * 
	 * @param searchMap
	 * @return
	 */
	private List searchCategoryList(Map searchMap) {
		List<String> list = new ArrayList();
		Query query = new SimpleQuery();
		// 按照关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// 设置分组选项
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		// 得到分组页
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		// 根据列得到分组结果集
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		// 得到分组结果入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		// 得到分组入口集合
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		for (GroupEntry<TbItem> entry : content) {
			list.add(entry.getGroupValue());// 将分组结果的名称封装到返回值中
		}
		return list;
	}

	

	/**
	 * 查询品牌和规格列表
	 * 
	 * @param object
	 *            分类名称
	 * @return
	 */
	private Map searchBrandAndSpecList(Object object) {
		Map map = new HashMap();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(object);// 获取模板ID
		if (typeId != null) {
			// 根据模板ID查询品牌列表
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			map.put("brandList", brandList);// 返回值添加品牌列表
			
//			System.out.println("品牌列表条数： "+brandList.size());
			
			// 根据模板ID查询规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
			map.put("specList", specList);
//			System.out.println("规格列表条数： "+specList.size());
		}
		return map;
	}

}
