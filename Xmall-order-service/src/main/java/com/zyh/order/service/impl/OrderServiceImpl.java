package com.zyh.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyh.mapper.TbOrderItemMapper;
import com.zyh.mapper.TbOrderMapper;
import com.zyh.mapper.TbPayLogMapper;
import com.zyh.pojo.TbOrder;
import com.zyh.pojo.TbOrderExample;
import com.zyh.pojo.TbOrderExample.Criteria;
import com.zyh.pojo.TbOrderItem;
import com.zyh.pojo.TbPayLog;
import com.zyh.pojogroup.Cart;
import com.zyh.order.service.OrderService;

import entity.PageResult;
import util.IdWorker;

/**
 * 鏈嶅姟瀹炵幇灞�
 * @author Administrator
 *
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbPayLogMapper payLogMapper;
	
	/**
	 * 鏌ヨ鍏ㄩ儴
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 鎸夊垎椤垫煡璇�
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	/**
	 * 澧炲姞
	 */
	@Override
	public void add(TbOrder order) {
		
		//1.浠巖edis涓彁鍙栬喘鐗╄溅鍒楄〃
		List<Cart> cartList= (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		
		List<String> orderIdList=new ArrayList();//璁㈠崟ID闆嗗悎
		double total_money=0;//鎬婚噾棰�
		//2.寰幆璐墿杞﹀垪琛ㄦ坊鍔犺鍗�
		for(Cart  cart:cartList){
			TbOrder tbOrder=new TbOrder();
			long orderId = idWorker.nextId();	//鑾峰彇ID		
			tbOrder.setOrderId(orderId);
			tbOrder.setPaymentType(order.getPaymentType());//鏀粯绫诲瀷
			tbOrder.setStatus("1");//鏈粯娆� 
			tbOrder.setCreateTime(new Date());//涓嬪崟鏃堕棿
			tbOrder.setUpdateTime(new Date());//鏇存柊鏃堕棿
			tbOrder.setUserId(order.getUserId());//褰撳墠鐢ㄦ埛
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//鏀惰揣浜哄湴鍧�
			tbOrder.setReceiverMobile(order.getReceiverMobile());//鏀惰揣浜虹數璇�
			tbOrder.setReceiver(order.getReceiver());//鏀惰揣浜�
			tbOrder.setSourceType(order.getSourceType());//璁㈠崟鏉ユ簮
			tbOrder.setSellerId(order.getSellerId());//鍟嗗ID
			
			double money=0;//鍚堣鏁�
			//寰幆璐墿杞︿腑姣忔潯鏄庣粏璁板綍
			for(TbOrderItem orderItem:cart.getOrderItemList()  ){
				orderItem.setId(idWorker.nextId());//涓婚敭
				orderItem.setOrderId(orderId);//璁㈠崟缂栧彿
				orderItem.setSellerId(cart.getSellerId());//鍟嗗ID
				orderItemMapper.insert(orderItem);				
				money+=orderItem.getTotalFee().doubleValue();
			}
			
			tbOrder.setPayment(new BigDecimal(money));//鍚堣
			
			
			orderMapper.insert(tbOrder);
			
			orderIdList.add(orderId+"");
			total_money+=money;
		}
		
		//娣诲姞鏀粯鏃ュ織
		if("1".equals(order.getPaymentType())){
			TbPayLog payLog=new TbPayLog();
			
			payLog.setOutTradeNo(idWorker.nextId()+"");//鏀粯璁㈠崟鍙�
			payLog.setCreateTime(new Date());
			payLog.setUserId(order.getUserId());//鐢ㄦ埛ID
			payLog.setOrderList(orderIdList.toString().replace("[", "").replace("]", ""));//璁㈠崟ID涓�
			payLog.setTotalFee( (long)( total_money*100)   );//閲戦锛堝垎锛�
			payLog.setTradeState("0");//浜ゆ槗鐘舵��
			payLog.setPayType("1");//寰俊
			payLogMapper.insert(payLog);
			
			redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);//鏀惧叆缂撳瓨 
		}
		
		
		//3.娓呴櫎redis涓殑璐墿杞�
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
	}

	
	/**
	 * 淇敼
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 鏍规嵁ID鑾峰彇瀹炰綋
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 鎵归噺鍒犻櫎
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public TbPayLog searchPayLogFromRedis(String userId) {		
		return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
	}

	@Override
	public void updateOrderStatus(String out_trade_no, String transaction_id) {
		//1.淇敼鏀粯鏃ュ織鐨勭姸鎬佸強鐩稿叧瀛楁
		TbPayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);
		payLog.setPayTime(new Date());//鏀粯鏃堕棿
		payLog.setTradeState("1");//浜ゆ槗鎴愬姛
		payLog.setTransactionId(transaction_id);//寰俊鐨勪氦鏄撴祦姘村彿
		
		payLogMapper.updateByPrimaryKey(payLog);//淇敼
		//2.淇敼璁㈠崟琛ㄧ殑鐘舵��
		String orderList = payLog.getOrderList();// 璁㈠崟ID 涓�
		String[] orderIds = orderList.split(",");
		
		for(String orderId:orderIds){
			TbOrder order = orderMapper.selectByPrimaryKey(Long.valueOf(orderId));
			order.setStatus("2");//宸蹭粯娆剧姸鎬�
			order.setPaymentTime(new Date());//鏀粯鏃堕棿
			orderMapper.updateByPrimaryKey(order);			
		}
		
		//3.娓呴櫎缂撳瓨涓殑payLog
		redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
		
	}
	
}
