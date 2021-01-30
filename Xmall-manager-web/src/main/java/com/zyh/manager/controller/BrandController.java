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
 * 鍝佺墝controller
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
	 * 增加
	 * @param brand
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			brandService.add(brand);
			return new Result(true, "增加成功");
		} catch (Exception e) {   //这里可以细化错类型和信息
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
}
