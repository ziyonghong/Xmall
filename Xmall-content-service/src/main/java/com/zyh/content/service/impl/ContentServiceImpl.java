package com.zyh.content.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyh.mapper.TbContentMapper;
import com.zyh.pojo.TbContent;
import com.zyh.pojo.TbContentExample;
import com.zyh.pojo.TbContentExample.Criteria;
import com.zyh.content.service.ContentService;

import entity.PageResult;

/**
 * 鏈嶅姟瀹炵幇灞�
 * 
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	/**
	 * 鏌ヨ鍏ㄩ儴
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 鎸夊垎椤垫煡璇�
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 澧炲姞
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);
		//清除缓存
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}

	/**
	 * 淇敼
	 */
	@Override
	public void update(TbContent content) {
		//查询修改前的分类Id
		Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
		redisTemplate.boundHashOps("content").delete(categoryId);
		contentMapper.updateByPrimaryKey(content);
		//如果分类ID发生了修改,清除修改后的分类ID的缓存
		if(categoryId.longValue()!=content.getCategoryId().longValue()){
			redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		}	
	}

	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id) {
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 鎵归噺鍒犻櫎
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			
			//清除缓存
			Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();//广告分类ID
			redisTemplate.boundHashOps("content").delete(categoryId);
			contentMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();

		if (content != null) {
			if (content.getTitle() != null && content.getTitle().length() > 0) {
				criteria.andTitleLike("%" + content.getTitle() + "%");
			}
			if (content.getUrl() != null && content.getUrl().length() > 0) {
				criteria.andUrlLike("%" + content.getUrl() + "%");
			}
			if (content.getPic() != null && content.getPic().length() > 0) {
				criteria.andPicLike("%" + content.getPic() + "%");
			}
			if (content.getStatus() != null && content.getStatus().length() > 0) {
				criteria.andStatusLike("%" + content.getStatus() + "%");
			}

		}

		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {

		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
		if (contentList == null) {
			System.out.println("从数据库读取数据放入缓存");
			// 根据广告分类ID查询广告列表
			TbContentExample contentExample = new TbContentExample();
			Criteria criteria2 = contentExample.createCriteria();
			criteria2.andCategoryIdEqualTo(categoryId); // 指定条件分类ID
			criteria2.andStatusEqualTo("1");// 指定有效，开启状态
			contentExample.setOrderByClause("sort_order");// 排序
			contentList = contentMapper.selectByExample(contentExample);// 获取广告列表
			redisTemplate.boundHashOps("content").put(categoryId, contentList);// 存入缓存
		} else {
			System.out.println("从缓存读取数据");
		}
		return contentList;
	}

}
