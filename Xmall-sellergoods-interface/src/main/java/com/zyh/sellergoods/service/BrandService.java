package com.zyh.sellergoods.service;

import com.zyh.pojo.TbBrand;

import entity.PageResult;

import java.util.List;
import java.util.Map;


public interface BrandService {

  
    public List<TbBrand> findAll();
    
    //���ط�ҳ�б�
    public PageResult findPage(int pageNum,int pageSize);
    
    //����
    public void add(TbBrand brand);
    
    // �޸�
	public void update(TbBrand brand);
	
	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	 * ����ɾ��
	 * @param ids
	 */
	public void delete(Long [] ids);
	
	/**
	 * ������ѯ������ҳ
	 * @param pageNum ��ǰҳ ��
	 * @param pageSize ÿҳ��¼��
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);

	/**
	 * Ʒ������������
	 */
	public List<Map> selectOptionList();


}
