package com.zyh.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyh.content.service.ContentService;
import com.zyh.pojo.TbContent;

@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;
	
	/**
	 * ���ݹ�����ID��ѯ����б�
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/findByCategoryId")
	public List<TbContent> findByCategoryId(Long categoryId) {
		return contentService.findByCategoryId(categoryId);
	}		
}
