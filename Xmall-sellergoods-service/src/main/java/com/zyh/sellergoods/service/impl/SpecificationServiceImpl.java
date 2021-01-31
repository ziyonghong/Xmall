package com.zyh.sellergoods.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyh.mapper.TbSpecificationMapper;
import com.zyh.mapper.TbSpecificationOptionMapper;
import com.zyh.pojo.TbSpecification;
import com.zyh.pojo.TbSpecificationExample;
import com.zyh.pojo.TbSpecificationExample.Criteria;
import com.zyh.pojo.TbSpecificationOption;
import com.zyh.pojo.TbSpecificationOptionExample;
import com.zyh.pojogroup.Specification;
import com.zyh.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 鏈嶅姟瀹炵幇灞�
 * 
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	/**
	 * 鏌ヨ鍏ㄩ儴
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 鎸夊垎椤垫煡璇�
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 澧炲姞
	 */
	@Override
	public void add(TbSpecification specification) {
		specificationMapper.insert(specification);
	}

	/**
	 * 淇敼
	 */
	@Override
	public void update(Specification specification) {
		// 保存修改的规格
		specificationMapper.updateByPrimaryKey(specification.getSpecification());// 保存规格
		// 删除原有的规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.zyh.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specification.getSpecification().getId());// 指定规格ID为条件
		specificationOptionMapper.deleteByExample(example);// 删除
		// 循环插入规格选项
		for (TbSpecificationOption specificationOption : specification.getSpecificationOptionList()) {
			specificationOption.setSpecId(specification.getSpecification().getId());
			specificationOptionMapper.insert(specificationOption);
		}
	}

	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id) {
		// 查询规格
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		// 查询规格选项列表
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.zyh.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);// 根据规格ID查询
		List<TbSpecificationOption> optionList = specificationOptionMapper.selectByExample(example);
		// 构建组合实体类返回结果
		Specification spec = new Specification();
		spec.setSpecification(tbSpecification);
		spec.setSpecificationOptionList(optionList);
		return spec;
	}

	/**
	 * 鎵归噺鍒犻櫎
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			//删除规格表数据
			specificationMapper.deleteByPrimaryKey(id);
			
			//删除规格选项表数据		
			TbSpecificationOptionExample example=new TbSpecificationOptionExample();
			com.zyh.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);//指定规格ID为条件
			specificationOptionMapper.deleteByExample(example);//删除
		}
	}

	@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSpecificationExample example = new TbSpecificationExample();
		Criteria criteria = example.createCriteria();

		if (specification != null) {
			if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
				criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
			}

		}

		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void add(Specification specification) {
		specificationMapper.insert(specification.getSpecification());// 插入规格
		// 循环插入规格选项
		for (TbSpecificationOption specificationOption : specification.getSpecificationOptionList()) {
			specificationOption.setSpecId(specification.getSpecification().getId());// 设置规格ID
			specificationOptionMapper.insert(specificationOption);
		}
	}

}
