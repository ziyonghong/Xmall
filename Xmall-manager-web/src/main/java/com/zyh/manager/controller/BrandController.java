package com.zyh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.pojo.TbBrand;
import com.zyh.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品牌controller
 *
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/findAll")
    private List<TbBrand> findAll(){
        return brandService.findAll();
    }
    
    @RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return brandService.findPage(page, rows);
	}
    
    /**
	 * ����
	 * @param brand
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true, "���ӳɹ�");
		} catch (Exception e) {   //�������ϸ�������ͺ���Ϣ
			e.printStackTrace();
			return new Result(false, "����ʧ��");
		}
	}
}
