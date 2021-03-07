package com.zyh.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyh.mapper.TbUserMapper;
import com.zyh.pojo.TbUser;
import com.zyh.pojo.TbUserExample;
import com.zyh.pojo.TbUserExample.Criteria;
import com.zyh.user.service.UserService;

import entity.PageResult;

/**
 * 鏈嶅姟瀹炵幇灞�
 * 
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	/**
	 * 鏌ヨ鍏ㄩ儴
	 */
	@Override
	public List<TbUser> findAll() {
		return userMapper.selectByExample(null);
	}

	/**
	 * 鎸夊垎椤垫煡璇�
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 澧炲姞
	 */
	@Override
	public void add(TbUser user) {

		user.setCreated(new Date());// 鐢ㄦ埛娉ㄥ唽鏃堕棿
		user.setUpdated(new Date());// 淇敼鏃堕棿
		user.setSourceType("1");// 娉ㄥ唽鏉ユ簮
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));// 瀵嗙爜鍔犲瘑

		userMapper.insert(user);
	}

	/**
	 * 淇敼
	 */
	@Override
	public void update(TbUser user) {
		userMapper.updateByPrimaryKey(user);
	}

	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbUser findOne(Long id) {
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 鎵归噺鍒犻櫎
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			userMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbUser user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();

		if (user != null) {
			if (user.getUsername() != null && user.getUsername().length() > 0) {
				criteria.andUsernameLike("%" + user.getUsername() + "%");
			}
			if (user.getPassword() != null && user.getPassword().length() > 0) {
				criteria.andPasswordLike("%" + user.getPassword() + "%");
			}
			if (user.getPhone() != null && user.getPhone().length() > 0) {
				criteria.andPhoneLike("%" + user.getPhone() + "%");
			}
			if (user.getEmail() != null && user.getEmail().length() > 0) {
				criteria.andEmailLike("%" + user.getEmail() + "%");
			}
			if (user.getSourceType() != null && user.getSourceType().length() > 0) {
				criteria.andSourceTypeLike("%" + user.getSourceType() + "%");
			}
			if (user.getNickName() != null && user.getNickName().length() > 0) {
				criteria.andNickNameLike("%" + user.getNickName() + "%");
			}
			if (user.getName() != null && user.getName().length() > 0) {
				criteria.andNameLike("%" + user.getName() + "%");
			}
			if (user.getStatus() != null && user.getStatus().length() > 0) {
				criteria.andStatusLike("%" + user.getStatus() + "%");
			}
			if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
				criteria.andHeadPicLike("%" + user.getHeadPic() + "%");
			}
			if (user.getQq() != null && user.getQq().length() > 0) {
				criteria.andQqLike("%" + user.getQq() + "%");
			}
			if (user.getIsMobileCheck() != null && user.getIsMobileCheck().length() > 0) {
				criteria.andIsMobileCheckLike("%" + user.getIsMobileCheck() + "%");
			}
			if (user.getIsEmailCheck() != null && user.getIsEmailCheck().length() > 0) {
				criteria.andIsEmailCheckLike("%" + user.getIsEmailCheck() + "%");
			}
			if (user.getSex() != null && user.getSex().length() > 0) {
				criteria.andSexLike("%" + user.getSex() + "%");
			}

		}

		Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination smsDestination;

//	@Value("${template_code}")
//	private String template_code;

//	@Value("${sign_name}")
//	private String sign_name;

	@Override
	public void createSmsCode(final String phone) {
		//1. 生成6位随机数
		final String smscode = (long) (Math.random() * 1000000) + "";
		System.out.println("验证码是：" + smscode);
		//2. 存入缓存
		redisTemplate.boundHashOps("smscode").put(phone, smscode);
		//3.发送到activeMQ	
		jmsTemplate.send(smsDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				message.setString("mobile", phone);// 手机号
//				message.setString("template_code", template_code);// 模板编号
//				message.setString("sign_name", sign_name);// 签名
				Map map = new HashMap();
				map.put("number", smscode);
				message.setString("param", JSON.toJSONString(map));				
				return message;
			}
		});

	}

	@Override
	public boolean checkSmsCode(String phone, String code) {

		String systemcode = (String) redisTemplate.boundHashOps("smscode").get(phone);
		if (systemcode == null) {
			return false;
		}
		if (!systemcode.equals(code)) {
			return false;
		}

		return true;
	}

}
