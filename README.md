# Xmall



**Xmall-parent 聚合工程（父工程）**

**Xmall-pojo 通用实体类层**

**Xmall-dao 通用数据访问层**

**Xmall-common 通用工具类模块**

**Xmall-xxxxx-interface  某服务层接口** 

​	-sellergoods-interface 商家商品服务接口模块

​	-content-interface 广告管理服务接口模块

​	-search-interface 搜索服务接口模块

​	-page-interface 商品详情页 （用**Freemarker**实现网页静态化）

​	-user-interface  用户服务接口（用阿里大于实现短信验证）

​	-cart-interface 购物车服务接口

​	-order-interface 订单接口

​	-pay-interface 支付接口

​	-seckill-interface 秒杀接口

**Xmall-xxxxx-service   某服务层实现**

​	-sellergoods-service 商家商品服务模块    [tomcat port：9001] [dubbo port：20881]

​	-content-service 广告管理服务模块   [port：9002]

​	-search-service 搜索服务模块 [port：9004]

​	-page-service 商品详情页  [port：9005]

​	-user-service 用户服务实现 [port：9006]

​	-cart-service 购物车服务 [port：9007]

​	-order-service 订单服务 [port：9008]

​	-pay-service 支付服务 [port：9000]

​	-seckill-service 秒杀服务 [port：9009]

**Xmall-xxxxx-web     某web工程**  

​	-manager-web  运营商管理后台    [port：9101]

​		运营商管理依赖sellergoods-service，content-service，search-service，page-service  ，所以运行它时需要先启动这些service

​	-shop-web  商家管理后台    [port：9102]

​		商家管理依赖 sellergoods-service

​	-portal-web  网站前台入口    [port：9103]

​		网站前台入口依赖 content-service

​	-search-web  搜索web   [port：9104]

​	-page-web   商品详情静态化  [port：9105]

​	-user-web  用户中心web  [port：9106]

​	-cart-web 购物车 [port：9107]

  		购物车依赖cart-service，user-service，order-service，pay-service

​	-seckill-web 秒杀web  [port：9109]



![1614513304484](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\1614513304484.png)



引入消息中间件[SpringJms]后：

![1614513476267](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\1614513476267.png)



Run As -> Maven build ->tomcat7:run 启动tomcat插件



##### 遗留todo：1.FastDFS上传图片失败

报错：ClassNotFoundException: org.csource.fastdfs.ClientGlobal

##### 2.solr搜索已解决，但前端展示问题需要优化

[title太长，需要在前端截取比较合理，但目前对前端不熟悉。如果在后端做截取的话就不太合理]

有重复的数据展示问题，不知道是不是我执行了过多次solrutil，但只是部分图片有重复，问题根源待排查



图片logo问题：在webbase.css中，有时候更改无效可能是缓存问题











Doubble的注册中心Zookeeper 服务启动步骤：

1.虚拟机 xmall-server    用户root，密码itcast

​       项目放置于 root's Home ->  Xmall下

2.进入Xmall项目下后 cd 到 zookeeper 的bin下

​		启动服务： ./zkServer.sh start

​                关闭服务： ./zkServer.sh stop

​		查看状态：./zkServer.sh status （启动状态 **Mode：standalone**， 未启动状态： Error）

zookeeper管理端：http://192.168.25.128:8080/dubbo-admin/ ,登录用户名和密码均为root 进入首页。 (192.168.25.128:)是我部署的linux主机地址。



solr搜索服务启动：

window启动方式：双击在F:\project\apache-tomcat-solr\bin目录在的startup.bat

（已将加压的solr的war包放入tomcat下的webapps，并加入的ik中文分词器）

访问 http://localhost:8080/solr 



CAS实现单点登录：

（已将解压的solr的war包放入tomcat下的webapps）

启动tomcat后访问 http://localhost:8080/cas/login

用户名和密码 casuser/Mellon  [这个是在WEB-INF下的deployerConfigContext.xml]

后面配置了是通过tb_user验证



redis启动：

window启动方式： 放置于F:\project\software\Redis\redis2.8win32 目录下

双击 redis-server.exe 即可。



ActiveMQ启动：

位于虚拟机的Xmall/ apache-activemq-5.12.0下

进入其bin目录下  `./activemq start` 启动

[http://192.168.25.128:8161/](http://192.168.25.129:8161/)  即可进入ActiveMQ管理页面

点击 `Manage ActiveMQ broker`进入管理页面    用户名和密码  均为 admin 



网页静态化生成页面生成的位置：pagedir=f:\\project\\item\\



登录账号信息：

运营商管理后台   账号：admin   密码：123456

user 登录入口  账号：zhubajie  123456

商家使用过程：进入http://localhost:9102/register.html 申请入驻，会跳到login.html

但要管理员申请后才能登录。 管理员申请：









###### 问题记录：

1.[“Source folder is not a Java project” error in eclipse](https://stackoverflow.com/questions/11719545/source-folder-is-not-a-java-project-error-in-eclipse)  解决办法：

执行mvn eclipse:eclipse 将工程变为java项目

2.VMWare遇到“Cannot open the disk 'XXX' or one of the snapshot disks it depends on. reason: failed to ”

https://blog.csdn.net/bottlerun/article/details/7477517