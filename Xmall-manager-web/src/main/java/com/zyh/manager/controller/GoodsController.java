package com.zyh.manager.controller;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.page.service.ItemPageService;
import com.zyh.pojo.TbGoods;
import com.zyh.pojo.TbItem;
import com.zyh.pojogroup.Goods;
import com.zyh.search.service.ItemSearchService;
import com.zyh.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
//	@RequestMapping("/add")
//	public Result add(@RequestBody TbGoods goods){
//		try {
//			goodsService.add(goods);
//			return new Result(true, "增加成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new Result(false, "增加失败");
//		}
//	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
	/**
	 * ����״̬
	 * @param ids
	 * @param status
	 */
	
	@Reference
	private ItemSearchService itemSearchService;
	
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){		
		try {
			goodsService.updateStatus(ids, status);
			
			//����SPU ID��ѯ SKU�б�(״̬Ϊ1)		
			if(status.equals("1")){//���ͨ��
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids, status);						
				//���������ӿ�ʵ��������������
				if(itemList.size()>0){				
					itemSearchService.importList(itemList);
				}else{
					System.out.println("û����ϸ����");
				}
				//��̬ҳ���� ��Ʒ����ҳ
				for(Long goodsId:ids){
					itemPageService.genItemHtml(goodsId);
				}		
			}
			
			return new Result(true, "�ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "ʧ��");
		}
	}
	
	@Reference(timeout=40000)
	private ItemPageService itemPageService;
	/**
	 * ���ɾ�̬ҳ�����ԣ�
	 * @param goodsId
	 */
	@RequestMapping("/genHtml")
	public void genHtml(Long goodsId){
		itemPageService.genItemHtml(goodsId);	
	}
	
}
