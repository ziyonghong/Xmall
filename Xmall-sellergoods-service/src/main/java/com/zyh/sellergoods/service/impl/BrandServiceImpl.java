package com.zyh.sellergoods.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyh.mapper.TbBrandMapper;
import com.zyh.pojo.TbBrand;
import com.zyh.sellergoods.service.BrandService;

import entity.PageResult;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }
    
	@Override
	public PageResult findPage(int pageNum, int pageSize) {		
		PageHelper.startPage(pageNum, pageSize); //·ÖÒ³		
		Page<TbBrand> page=   (Page<TbBrand>) brandMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void add(TbBrand brand) {
		brandMapper.insert(brand);		
	}
    
   
}
