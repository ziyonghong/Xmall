<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title>浜у搧璇︽儏椤�</title>
	 <link rel="icon" href="assets/img/favicon.ico">

    <link rel="stylesheet" type="text/css" href="css/webbase.css" />
    <link rel="stylesheet" type="text/css" href="css/pages-item.css" />
    <link rel="stylesheet" type="text/css" href="css/pages-zoom.css" />
    <link rel="stylesheet" type="text/css" href="css/widget-cartPanelView.css" />
    
    <script type="text/javascript" src="plugins/angularjs/angular.min.js"> </script>
    <script type="text/javascript" src="js/base.js"> </script>
    <script type="text/javascript" src="js/controller/itemController.js"> </script>
    <script>
        var skuList=[
          <#list itemList as item>
           {
             id:${item.id?c},
             title:'${item.title}',
             price:${item.price?c},
             spec:${item.spec}
           } ,
          </#list>   
        ];
        
    </script>
</head>

<body ng-app="xmall" ng-controller="itemController" ng-init="num=1;loadSku()">

<!--椤甸潰椤堕儴 寮�濮�-->
<#include "head.ftl">
<#--鍥剧墖鍒楄〃-->
<#assign imageList=goodsDesc.itemImages?eval>
<#--鎵╁睍灞炴��-->
<#assign customAttributeList=goodsDesc.customAttributeItems?eval>
<#--瑙勬牸-->
<#assign specificationList=goodsDesc.specificationItems?eval>


<!--椤甸潰椤堕儴 缁撴潫-->
	<div class="py-container">
		<div id="item">
			<div class="crumb-wrap">
				<ul class="sui-breadcrumb">
					<li>
						<a href="#">${itemCat1}</a>
					</li>
					<li>
						<a href="#">${itemCat2}</a>
					</li>
					<li>
						<a href="#">${itemCat3}</a>
					</li>	
							
				</ul>
			</div>
			<!--product-info-->
			<div class="product-info">
				<div class="fl preview-wrap">
					<!--鏀惧ぇ闀滄晥鏋�-->
					<div class="zoom">
						<!--榛樿绗竴涓瑙�-->
						<div id="preview" class="spec-preview">
							<span class="jqzoom">
							 <#if (imageList?size>0)>
							     <img jqimg="${imageList[0].url}" src="${imageList[0].url}" width="400px" height="400px" />
							 </#if>
							</span>
						</div>
						<!--涓嬫柟鐨勭缉鐣ュ浘-->
						<div class="spec-scroll">
							<a class="prev">&lt;</a>
							<!--宸﹀彸鎸夐挳-->
							<div class="items">
								<ul>
								   <#list imageList as item>
									  <li><img src="${item.url}" bimg="${item.url}" onmousemove="preview(this)" /></li>
								   </#list>
								</ul>
							</div>
							<a class="next">&gt;</a>
						</div>
					</div>
				</div>
				<div class="fr itemInfo-wrap">
					<div class="sku-name">
						<h4>{{sku.title}}</h4>
					</div>
					<div class="news"><span>${goods.caption} </span></div>
					<div class="summary">
						<div class="summary-wrap">
							<div class="fl title">
								<i>浠枫��銆�鏍�</i>
							</div>
							<div class="fl price">
								<i>楼</i>
								<em>{{sku.price}}</em>
								<span>闄嶄环閫氱煡</span>
							</div>
							<div class="fr remark">
								<i>绱璇勪环</i><em>612188</em>
							</div>
						</div>
						<div class="summary-wrap">
							<div class="fl title">
								<i>淇冦��銆�閿�</i>
							</div>
							<div class="fl fix-width">
								<i class="red-bg">鍔犱环璐�</i>
								<em class="t-gray">婊�999.00鍙﹀姞20.00鍏冿紝鎴栨弧1999.00鍙﹀姞30.00鍏冿紝鎴栨弧2999.00鍙﹀姞40.00鍏冿紝鍗冲彲鍦ㄨ喘鐗╄溅鎹�
璐儹閿�鍟嗗搧</em>
							</div>
						</div>
					</div>
					<div class="support">
						<div class="summary-wrap">
							<div class="fl title">
								<i>鏀��銆�鎸�</i>
							</div>
							<div class="fl fix-width">
								<em class="t-gray">浠ユ棫鎹㈡柊锛岄棽缃墜鏈哄洖鏀�  4G濂楅瓒呭�兼姠  绀煎搧璐�</em>
							</div>
						</div>
						<div class="summary-wrap">
							<div class="fl title">
								<i>閰� 閫� 鑷�</i>
							</div>
							<div class="fl fix-width">
								<em class="t-gray">婊�999.00鍙﹀姞20.00鍏冿紝鎴栨弧1999.00鍙﹀姞30.00鍏冿紝鎴栨弧2999.00鍙﹀姞40.00鍏冿紝鍗冲彲鍦ㄨ喘鐗╄溅鎹㈣喘鐑攢鍟嗗搧</em>
							</div>
						</div>
					</div>
					<div class="clearfix choose">
						<div id="specification" class="summary-wrap clearfix">
							<#list specificationList as spec>
							
							  <dl>
								<dt>
									<div class="fl title">
									<i>${spec.attributeName}</i>
								</div>
								</dt>
								
								<#list spec.attributeValue as item>
								  <dd><a href="javascript:;" 
								        class="{{isSelected('${spec.attributeName}','${item}')?'selected':''}}" 
								        ng-click="selectSpecification('${spec.attributeName}','${item}')">${item}
								        <span title="鐐瑰嚮鍙栨秷閫夋嫨">&nbsp;</span>
								        </a></dd>
								</#list>
							  </dl>
							</#list>
							
						</div>
					
						<div class="summary-wrap">
							<div class="fl title">
								<div class="control-group">
									<div class="controls">
										<input autocomplete="off" ng-model="num" type="text" value="1" minnum="1" class="itxt" />
										<a href="javascript:void(0)" class="increment plus" ng-click="addNum(1)">+</a>
										<a href="javascript:void(0)" class="increment mins" ng-click="addNum(-1)">-</a>
									</div>
								</div>
							</div>
							<div class="fl">
								<ul class="btn-choose unstyled">
									<li>
										<a  class="sui-btn  btn-danger addshopcar" ng-click="addToCart()">鍔犲叆璐墿杞�</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--product-detail-->
			<div class="clearfix product-detail">
				<div class="fl aside">
					<ul class="sui-nav nav-tabs tab-wraped">
						<li class="active">
							<a href="#index" data-toggle="tab">
								<span>鐩稿叧鍒嗙被</span>
							</a>
						</li>
						<li>
							<a href="#profile" data-toggle="tab">
								<span>鎺ㄨ崘鍝佺墝</span>
							</a>
						</li>
					</ul>
					<div class="tab-content tab-wraped">
						<div id="index" class="tab-pane active">
							<ul class="part-list unstyled">
								<li>鎵嬫満</li>
								<li>鎵嬫満澹�</li>
								<li>鍐呭瓨鍗�</li>
								<li>Iphone閰嶄欢</li>
								<li>璐磋啘</li>
								<li>鎵嬫満鑰虫満</li>
								<li>绉诲姩鐢垫簮</li>
								<li>骞虫澘鐢佃剳</li>
							</ul>
							<ul class="goods-list unstyled">
								<li>
									<div class="list-wrap">
										<div class="p-img">
											<img src="img/_/part01.png" />
										</div>
										<div class="attr">
											<em>Apple鑻规灉iPhone 6s (A1699)</em>
										</div>
										<div class="price">
											<strong>
											<em>楼</em>
											<i>6088.00</i>
										</strong>
										</div>
										<div class="operate">
											<a href="javascript:void(0);" class="sui-btn btn-bordered">鍔犲叆璐墿杞�</a>
										</div>
									</div>
								</li>
								<li>
									<div class="list-wrap">
										<div class="p-img">
											<img src="img/_/part02.png" />
										</div>
										<div class="attr">
											<em>Apple鑻规灉iPhone 6s (A1699)</em>
										</div>
										<div class="price">
											<strong>
											<em>楼</em>
											<i>6088.00</i>
										</strong>
										</div>
										<div class="operate">
											<a href="javascript:void(0);" class="sui-btn btn-bordered">鍔犲叆璐墿杞�</a>
										</div>
									</div>
								</li>
								<li>
									<div class="list-wrap">
										<div class="p-img">
											<img src="img/_/part03.png" />
										</div>
										<div class="attr">
											<em>Apple鑻规灉iPhone 6s (A1699)</em>
										</div>
										<div class="price">
											<strong>
											<em>楼</em>
											<i>6088.00</i>
										</strong>
										</div>
										<div class="operate">
											<a href="javascript:void(0);" class="sui-btn btn-bordered">鍔犲叆璐墿杞�</a>
										</div>
									</div>
									<div class="list-wrap">
										<div class="p-img">
											<img src="img/_/part02.png" />
										</div>
										<div class="attr">
											<em>Apple鑻规灉iPhone 6s (A1699)</em>
										</div>
										<div class="price">
											<strong>
											<em>楼</em>
											<i>6088.00</i>
										</strong>
										</div>
										<div class="operate">
											<a href="javascript:void(0);" class="sui-btn btn-bordered">鍔犲叆璐墿杞�</a>
										</div>
									</div>
									<div class="list-wrap">
										<div class="p-img">
											<img src="img/_/part03.png" />
										</div>
										<div class="attr">
											<em>Apple鑻规灉iPhone 6s (A1699)</em>
										</div>
										<div class="price">
											<strong>
											<em>楼</em>
											<i>6088.00</i>
										</strong>
										</div>
										<div class="operate">
											<a href="javascript:void(0);" class="sui-btn btn-bordered">鍔犲叆璐墿杞�</a>
										</div>
									</div>
								</li>
							</ul>
						</div>
						<div id="profile" class="tab-pane">
							<p>鎺ㄨ崘鍝佺墝</p>
						</div>
					</div>
				</div>
				<div class="fr detail">
					<div class="clearfix fitting">
						<h4 class="kt">閫夋嫨鎼厤</h4>
						<div class="good-suits">
							<div class="fl master">
								<div class="list-wrap">
									<div class="p-img">
										<img src="img/_/l-m01.png" />
									</div>
									<em>锟�5299</em>
									<i>+</i>
								</div>
							</div>
							<div class="fl suits">
								<ul class="suit-list">
									<li class="">
										<div id="">
											<img src="img/_/dp01.png" />
										</div>
										<i>Feless璐瑰嫆鏂疺R</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>39</span>
  </label>
									</li>
									<li class="">
										<div id=""><img src="img/_/dp02.png" /> </div>
										<i>Feless璐瑰嫆鏂疺R</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>50</span>
  </label>
									</li>
									<li class="">
										<div id=""><img src="img/_/dp03.png" /></div>
										<i>Feless璐瑰嫆鏂疺R</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>59</span>
  </label>
									</li>
									<li class="">
										<div id=""><img src="img/_/dp04.png" /></div>
										<i>Feless璐瑰嫆鏂疺R</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>99</span>
  </label>
									</li>
								</ul>
							</div>
							<div class="fr result">
								<div class="num">宸查�夎喘0浠跺晢鍝�</div>
								<div class="price-tit"><strong>濂楅浠�</strong></div>
								<div class="price">锟�5299</div>
								<button class="sui-btn  btn-danger addshopcar">鍔犲叆璐墿杞�</button>
							</div>
						</div>
					</div>
					<div class="tab-main intro">
						<ul class="sui-nav nav-tabs tab-wraped">
							<li class="active">
								<a href="#one" data-toggle="tab">
									<span>鍟嗗搧浠嬬粛</span>
								</a>
							</li>
							<li>
								<a href="#two" data-toggle="tab">
									<span>瑙勬牸涓庡寘瑁�</span>
								</a>
							</li>
							<li>
								<a href="#three" data-toggle="tab">
									<span>鍞悗淇濋殰</span>
								</a>
							</li>
							<li>
								<a href="#four" data-toggle="tab">
									<span>鍟嗗搧璇勪环</span>
								</a>
							</li>
							<li>
								<a href="#five" data-toggle="tab">
									<span>鎵嬫満绀惧尯</span>
								</a>
							</li>
						</ul>
						<div class="clearfix"></div>
						<div class="tab-content tab-wraped">
							<div id="one" class="tab-pane active">
								<ul class="goods-intro unstyled">
								   <#list customAttributeList as item>
								      <#if item.value??>
									      <li>${item.text}锛�${item.value}</li>
									  </#if>
								   </#list>	
								</ul>
								<div class="intro-detail">
									${goodsDesc.introduction}
								</div>
							</div>
							<div id="two" class="tab-pane">
								<p>${goodsDesc.packageList}</p>
							</div>
							<div id="three" class="tab-pane">
								<p>${goodsDesc.saleService}</p>
							</div>
							<div id="four" class="tab-pane">
								<p>鍟嗗搧璇勪环</p>
							</div>
							<div id="five" class="tab-pane">
								<p>鎵嬫満绀惧尯</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--like-->
			<div class="clearfix"></div>
			<div class="like">
				<h4 class="kt">鐚滀綘鍠滄</h4>
				<div class="like-list">
					<ul class="yui3-g">
						<li class="yui3-u-1-6">
							<div class="list-wrap">
								<div class="p-img">
									<img src="img/_/itemlike01.png" />
								</div>
								<div class="attr">
									<em>DELL鎴村皵Ins 15MR-7528SS 15鑻卞 閾惰壊 绗旇鏈�</em>
								</div>
								<div class="price">
									<strong>
											<em>楼</em>
											<i>3699.00</i>
										</strong>
								</div>
								<div class="commit">
									<i class="command">宸叉湁6浜鸿瘎浠�</i>
								</div>
							</div>
						</li>
						<li class="yui3-u-1-6">
							<div class="list-wrap">
								<div class="p-img">
									<img src="img/_/itemlike02.png" />
								</div>
								<div class="attr">
									<em>Apple鑻规灉iPhone 6s/6s Plus 16G 64G 128G</em>
								</div>
								<div class="price">
									<strong>
											<em>楼</em>
											<i>4388.00</i>
										</strong>
								</div>
								<div class="commit">
									<i class="command">宸叉湁700浜鸿瘎浠�</i>
								</div>
							</div>
						</li>
						<li class="yui3-u-1-6">
							<div class="list-wrap">
								<div class="p-img">
									<img src="img/_/itemlike03.png" />
								</div>
								<div class="attr">
									<em>DELL鎴村皵Ins 15MR-7528SS 15鑻卞 閾惰壊 绗旇鏈�</em>
								</div>
								<div class="price">
									<strong>
											<em>楼</em>
											<i>4088.00</i>
										</strong>
								</div>
								<div class="commit">
									<i class="command">宸叉湁700浜鸿瘎浠�</i>
								</div>
							</div>
						</li>
						<li class="yui3-u-1-6">
							<div class="list-wrap">
								<div class="p-img">
									<img src="img/_/itemlike04.png" />
								</div>
								<div class="attr">
									<em>DELL鎴村皵Ins 15MR-7528SS 15鑻卞 閾惰壊 绗旇鏈�</em>
								</div>
								<div class="price">
									<strong>
											<em>楼</em>
											<i>4088.00</i>
										</strong>
								</div>
								<div class="commit">
									<i class="command">宸叉湁700浜鸿瘎浠�</i>
								</div>
							</div>
						</li>
						<li class="yui3-u-1-6">
							<div class="list-wrap">
								<div class="p-img">
									<img src="img/_/itemlike05.png" />
								</div>
								<div class="attr">
									<em>DELL鎴村皵Ins 15MR-7528SS 15鑻卞 閾惰壊 绗旇鏈�</em>
								</div>
								<div class="price">
									<strong>
											<em>楼</em>
											<i>4088.00</i>
										</strong>
								</div>
								<div class="commit">
									<i class="command">宸叉湁700浜鸿瘎浠�</i>
								</div>
							</div>
						</li>
						<li class="yui3-u-1-6">
							<div class="list-wrap">
								<div class="p-img">
									<img src="img/_/itemlike06.png" />
								</div>
								<div class="attr">
									<em>DELL鎴村皵Ins 15MR-7528SS 15鑻卞 閾惰壊 绗旇鏈�</em>
								</div>
								<div class="price">
									<strong>
											<em>楼</em>
											<i>4088.00</i>
										</strong>
								</div>
								<div class="commit">
									<i class="command">宸叉湁700浜鸿瘎浠�</i>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- 搴曢儴鏍忎綅 -->
	
<!--椤甸潰搴曢儴  寮�濮� -->
<#include "foot.ftl">
<!--椤甸潰搴曢儴  缁撴潫 -->
</body>

</html>