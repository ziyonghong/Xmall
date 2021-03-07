package com.zyh.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zyh.cart.service.CartService;
import com.zyh.pojogroup.Cart;

import entity.Result;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Reference(timeout=6000)
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(){
		//得到登陆人账号,判断当前是否有人登陆
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("褰撳墠鐧诲綍浜猴細"+username);
		
		String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if(cartListString==null || cartListString.equals("")){
			cartListString="[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		
		if(username.equals("anonymousUser")){//濡傛灉鏈櫥褰�
			//浠巆ookie涓彁鍙栬喘鐗╄溅
			System.out.println("浠巆ookie涓彁鍙栬喘鐗╄溅");
						
			return cartList_cookie;
			
		}else{//濡傛灉宸茬櫥褰�
			//鑾峰彇redis璐墿杞�
			List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
			if(cartList_cookie.size()>0){//鍒ゆ柇褰撴湰鍦拌喘鐗╄溅涓瓨鍦ㄦ暟鎹�
				//寰楀埌鍚堝苟鍚庣殑璐墿杞�
				List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
				//灏嗗悎骞跺悗鐨勮喘鐗╄溅瀛樺叆redis 
				cartService.saveCartListToRedis(username, cartList);
				//鏈湴璐墿杞︽竻闄�
				util.CookieUtil.deleteCookie(request, response, "cartList");
				System.out.println("鎵ц浜嗗悎骞惰喘鐗╄溅鐨勯�昏緫");
				return cartList;
			}						
			return cartList_redis;
		}
				
	}
	
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:9105")
	public Result addGoodsToCartList(Long itemId,Integer num){
		
		//response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");//鍙互璁块棶鐨勫煙(褰撴鏂规硶涓嶉渶瑕佹搷浣渃ookie)
		//response.setHeader("Access-Control-Allow-Credentials", "true");//濡傛灉鎿嶄綔cookie锛屽繀椤诲姞涓婅繖鍙ヨ瘽
		
		//褰撳墠鐧诲綍浜鸿处鍙�
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("褰撳墠鐧诲綍浜猴細"+name);
		
	
		
		try {
			//鎻愬彇璐墿杞�
			List<Cart> cartList = findCartList();
			//璋冪敤鏈嶅姟鏂规硶鎿嶄綔璐墿杞�
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			
			if(name.equals("anonymousUser")){//濡傛灉鏈櫥褰�
				//灏嗘柊鐨勮喘鐗╄溅瀛樺叆cookie
				String cartListString = JSON.toJSONString(cartList);
				util.CookieUtil.setCookie(request, response, "cartList", cartListString, 3600*24, "UTF-8");
				System.out.println("鍚慶ookie瀛樺偍璐墿杞�");		
				
			}else{//濡傛灉鐧诲綍				
				cartService.saveCartListToRedis(name, cartList);				
			}

			return new Result(true, "瀛樺叆璐墿杞︽垚鍔�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "瀛樺叆璐墿杞﹀け璐�");
		}
		
		
	}
	
	
}
