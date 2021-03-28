package com.zyh.solrutil;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zyh.pojo.TbItem;

public class HtmlParseUtil {
	public static void main(String[] args) throws Exception {
		List<TbItem> list = new HtmlParseUtil().parseJD("redis");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Title=" + list.get(i).getTitle() + " Image=" + list.get(i).getImage());
		}
	}

	public List<TbItem> parseJD(String keywords) throws Exception {
		// 获取请求 https://search.jd.com/Search?keyword=java
		String url = "https://search.jd.com/Search?keyword=" + keywords;
		// 解析网页
		Document document = Jsoup.parse(new URL(url), 30000);
		Element element = document.getElementById("J_goodsList");
		Elements elements = element.getElementsByTag("li");
		// System.out.println(element.html());
		// 获取所有的li元素

		ArrayList<TbItem> itemList = new ArrayList<>();
		long i = 1;
		for (Element el : elements) {
			String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
			//这里爬取到的数据price是这样的 ￥2499.00 需要把前面那个去掉
			String price = el.getElementsByClass("p-price").eq(0).text();
			BigDecimal pricebd = new BigDecimal(price.substring(1));
			String title = el.getElementsByClass("p-name").eq(0).text();
			// System.out.println("===============================================");
			// System.out.println(img);
			// System.out.println(price);
			// System.out.println(title);
			TbItem tbItem = new TbItem();
			tbItem.setId(2500 + (i++));
			tbItem.setImage(img);
			tbItem.setPrice(pricebd);
			tbItem.setTitle(title);
			itemList.add(tbItem);
		}
		return itemList;
	}

}
