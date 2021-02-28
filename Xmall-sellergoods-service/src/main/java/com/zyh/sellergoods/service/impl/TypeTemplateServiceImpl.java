package com.zyh.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyh.mapper.TbSpecificationOptionMapper;
import com.zyh.mapper.TbTypeTemplateMapper;
import com.zyh.pojo.TbSpecificationOption;
import com.zyh.pojo.TbSpecificationOptionExample;
import com.zyh.pojo.TbTypeTemplate;
import com.zyh.pojo.TbTypeTemplateExample;
import com.zyh.pojo.TbTypeTemplateExample.Criteria;
import com.zyh.sellergoods.service.TypeTemplateService;

import entity.PageResult;

/**
 * 鏈嶅姟瀹炵幇灞�
 * @author Administrator
 *
 */
@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	/**
	 * 鏌ヨ鍏ㄩ儴
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 鎸夊垎椤垫煡璇�
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbTypeTemplate> page=   (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 澧炲姞
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 淇敼
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 鎵归噺鍒犻櫎
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
		
		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);		
		
		saveToRedis();//存入数据到缓存
		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public List<Map> findSpecList(Long id) {
			//查询模板
			TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
			
			List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(), Map.class)  ;
			for(Map map:list){
				//查询规格选项列表
				TbSpecificationOptionExample example=new TbSpecificationOptionExample();
				com.zyh.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
				criteria.andSpecIdEqualTo( new Long( (Integer)map.get("id") ) );
				List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
				map.put("options", options);
			}		
			return list;
		}
		
		@Autowired
		private RedisTemplate redisTemplate;
		
		/**
		 * 将数据存入缓存
		 */
		private void saveToRedis(){
			//获取模板数据
			List<TbTypeTemplate> typeTemplateList = findAll();
			//循环模板
			for(TbTypeTemplate typeTemplate :typeTemplateList){				
				//存储品牌列表		
				List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);			
				redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), brandList);
				//存储规格列表
				List<Map> specList = findSpecList(typeTemplate.getId());//根据模板ID查询规格列表
				redisTemplate.boundHashOps("specList").put(typeTemplate.getId(), specList);		
			}
		}
	
}
