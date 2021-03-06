package com.zyh.manager.controller;

import java.util.Arrays;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
//import com.zyh.page.service.ItemPageService;
import com.zyh.pojo.TbGoods;
import com.zyh.pojo.TbItem;
import com.zyh.pojogroup.Goods;
//import com.zyh.search.service.ItemSearchService;
import com.zyh.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 * 
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
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll() {
		return goodsService.findAll();
	}

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return goodsService.findPage(page, rows);
	}

	/**
	 * 增加
	 * 
	 * @param goods
	 * @return
	 */
	// @RequestMapping("/add")
	// public Result add(@RequestBody TbGoods goods){
	// try {
	// goodsService.add(goods);
	// return new Result(true, "增加成功");
	// } catch (Exception e) {
	// e.printStackTrace();
	// return new Result(false, "增加失败");
	// }
	// }

	/**
	 * 修改
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods) {
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
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id) {
		return goodsService.findOne(id);
	}

	@Autowired
	private Destination queueSolrDeleteDestination;// �û�����������ɾ����¼
	@Autowired
	private Destination topicPageDeleteDestination;//����ɾ����̬��ҳ����Ϣ
	
	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long[] ids) {
		try {
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
			//ɾ��ҳ��
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {		
				@Override
				public Message createMessage(Session session) throws JMSException {	
					return session.createObjectMessage(ids);
				}
			});	
			// goodsService.delete(ids);
			// itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 * 
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
		return goodsService.findPage(goods, page, rows);
	}

	/**
	 * ����״̬
	 * 
	 * @param ids
	 * @param status
	 */

	// @Reference
	// private ItemSearchService itemSearchService;

	@Autowired
	private Destination queueSolrDestination;// ���ڷ���solr�������Ϣ
	@Autowired
	private Destination topicPageDestination;//����������Ʒ��ϸҳ����ϢĿ��(��������)

	@Autowired
	private JmsTemplate jmsTemplate;

	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status) {
		try {
			goodsService.updateStatus(ids, status);

			// ����SPU ID��ѯ SKU�б�(״̬Ϊ1)
			if (status.equals("1")) {// ���ͨ��
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids, status);
				// ���������ӿ�ʵ��������������
				if (itemList.size() > 0) {
					// itemSearchService.importList(itemList);

					final String jsonString = JSON.toJSONString(itemList);
					jmsTemplate.send(queueSolrDestination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(jsonString);
						}
					});

				} else {
					System.out.println("û����ϸ����");
				}
				//****������Ʒ��ϸҳ
				for(final Long goodsId:ids){
					//	itemPageService.genItemHtml(goodsId);
					jmsTemplate.send(topicPageDestination, new MessageCreator() {
						
						@Override
						public Message createMessage(Session session) throws JMSException {							
							return session.createTextMessage(goodsId+"");
						}
					});					
				}
			}

			return new Result(true, "�ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "ʧ��");
		}
	}

	// @Reference(timeout=40000)
	// private ItemPageService itemPageService;
	// /**
	// * ���ɾ�̬ҳ�����ԣ�
	// * @param goodsId
	// */
	// @RequestMapping("/genHtml")
	// public void genHtml(Long goodsId){
	// itemPageService.genItemHtml(goodsId);
	// }

}
