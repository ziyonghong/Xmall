package com.zyh.sellergoods.service;

import com.zyh.pojo.TbBrand;

import entity.PageResult;

import java.util.List;


public interface BrandService {

  
    public List<TbBrand> findAll();
    
    //���ط�ҳ�б�
    public PageResult findPage(int pageNum,int pageSize);
    
    //����
    public void add(TbBrand brand);
}
