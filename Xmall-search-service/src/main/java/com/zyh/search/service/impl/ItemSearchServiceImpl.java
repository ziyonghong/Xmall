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
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
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
		// 1.��ѯ�б�
		map.putAll(searchList(searchMap));

		// 2.���ݹؼ��ֲ�ѯ��Ʒ����
		List categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);

		// 3.��ѯƷ�ƺ͹���б�
		String categoryName = (String) searchMap.get("category");
		if (!"".equals(categoryName)) {// ����з�������
			map.putAll(searchBrandAndSpecList(categoryName));
		} else {// ���û�з������ƣ����յ�һ����ѯ
			if (categoryList.size() > 0) {
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}
		
		return map;
	}

	/**
	 * ���ݹؼ��������б�
	 * 
	 * @param keywords
	 * @return
	 */
	private Map searchList(Map searchMap) {
		Map map = new HashMap();
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");// ���ø�������
		highlightOptions.setSimplePrefix("<em style='color:red'>");// ����ǰ׺
		highlightOptions.setSimplePostfix("</em>");// ������׺
		query.setHighlightOptions(highlightOptions);// ���ø���ѡ��
		// 1.1���չؼ��ֲ�ѯ
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);

		// 1.2������Ʒ�������
		if (!"".equals(searchMap.get("category"))) {
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		// 1.3��Ʒ��ɸѡ
		if (!"".equals(searchMap.get("brand"))) {
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		// 1.4���˹��
		if (searchMap.get("spec") != null) {
			Map<String, String> specMap = (Map) searchMap.get("spec");
			for (String key : specMap.keySet()) {
				Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
				FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}

		// ******** ��ȡ��������� *********
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		for (HighlightEntry<TbItem> h : page.getHighlighted()) {// ѭ��������ڼ���
			TbItem item = h.getEntity();// ��ȡԭʵ����
			if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));// ���ø����Ľ��
			}
		}
		map.put("rows", page.getContent());
		return map;
	}

	/**
	 * ��ѯ�����б�
	 * 
	 * @param searchMap
	 * @return
	 */
	private List searchCategoryList(Map searchMap) {
		List<String> list = new ArrayList();
		Query query = new SimpleQuery();
		// ���չؼ��ֲ�ѯ
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// ���÷���ѡ��
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		// �õ�����ҳ
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		// �����еõ���������
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		// �õ����������ҳ
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		// �õ�������ڼ���
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		for (GroupEntry<TbItem> entry : content) {
			list.add(entry.getGroupValue());// �������������Ʒ�װ������ֵ��
		}
		return list;
	}

	/**
	 * ��ѯƷ�ƺ͹���б�
	 * 
	 * @param object
	 *            ��������
	 * @return
	 */
	private Map searchBrandAndSpecList(Object object) {
		Map map = new HashMap();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(object);// ��ȡģ��ID
		if (typeId != null) {
			// ����ģ��ID��ѯƷ���б�
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			map.put("brandList", brandList);// ����ֵ���Ʒ���б�

			// System.out.println("Ʒ���б������� "+brandList.size());

			// ����ģ��ID��ѯ����б�
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
			map.put("specList", specList);
			// System.out.println("����б������� "+specList.size());
		}
		return map;
	}

}
