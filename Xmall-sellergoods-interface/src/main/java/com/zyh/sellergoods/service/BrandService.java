package com.zyh.sellergoods.service;

import com.zyh.pojo.TbBrand;

import entity.PageResult;

import java.util.List;
import java.util.Map;


public interface BrandService {

  
    public List<TbBrand> findAll();
    
    //返回分页列表
    public PageResult findPage(int pageNum,int pageSize);
    
    //增加
    public void add(TbBrand brand);
    
    // 修改
	public void update(TbBrand brand);
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);
	
	/**
	 * 条件查询，并分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);

	/**
	 * 品牌下拉框数据
	 */
	public List<Map> selectOptionList();


}
