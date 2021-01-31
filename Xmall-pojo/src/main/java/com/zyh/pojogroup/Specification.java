package com.zyh.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.zyh.pojo.TbSpecification;
import com.zyh.pojo.TbSpecificationOption;

/**
 * ������ʵ���� 
 * @author Administrator
 *
 */
public class Specification implements Serializable {	
	private TbSpecification specification;
	private List<TbSpecificationOption> specificationOptionList;
	
	public TbSpecification getSpecification() {
		return specification;
	}
	public void setSpecification(TbSpecification specification) {
		this.specification = specification;
	}
	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}
	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}	
}
