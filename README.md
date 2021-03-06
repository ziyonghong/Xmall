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

**Xmall-xxxxx-service   某服务层实现**

​	-sellergoods-service 商家商品服务模块    [port：9001]

​	-content-service 广告管理服务模块   [port：9002]

​	-search-service 搜索服务模块 [port：9004]

​	-page-service 商品详情页  [port：9005]

**Xmall-xxxxx-web     某web工程**  

​	-manager-web  运营商管理后台    [port：9101]

​		运营商管理依赖sellergoods-service，content-service，search-service，page-service  ，所以运行它时需要先启动这些service

​	-shop-web  商家管理后台    [port：9102]

​		商家管理依赖 sellergoods-service

​	-portal-web  网站前台入口    [port：9103]

​		网站前台入口依赖 content-service

​	-search-web  搜索web   [port：9104]

​	-page-web   商品详情静态化  [port：9105]



![1614513304484](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\1614513304484.png)



引入消息中间件后：

![1614513476267](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\1614513476267.png)



Run As -> Maven build ->tomcat7:run 启动tomcat插件



##### 遗留：HDFS上传图片失败







问题记录：

1.[“Source folder is not a Java project” error in eclipse](https://stackoverflow.com/questions/11719545/source-folder-is-not-a-java-project-error-in-eclipse)  解决办法：

执行mvn eclipse:eclipse 将工程变为java项目







Doubble的注册中心Zookeeper 服务启动步骤：

1.虚拟机 pingyougou-server    用户root，密码itcast

​       项目放置于 root's Home ->  Xmall下

2.进入Xmall项目下后 cd 到 zookeeper 的bin下

​		启动服务： ./zkServer.sh start

​                关闭服务： ./zkServer.sh stop

​		查看状态：./zkServer.sh status （启动状态 **Mode：standalone**， 未启动状态： Error）





solr搜索服务启动：

window启动方式：双击在F:\project\apache-tomcat-solr\bin目录在的startup.bat

（已将加压的solr的war包放入tomcat下的webapps，并加入的ik中文分词器）

访问 http://localhost:8080/solr 



redis启动：

window启动方式： 放置于F:\project\software\Redis\redis2.8win32 目录下

双击 redis-server.exe 即可。



ActiveMQ启动：

位于虚拟机的Xmall/ apache-activemq-5.12.0下

进入其bin目录下  `./activemq start` 启动

[http://192.168.25.128:8161/](http://192.168.25.129:8161/)  即可进入ActiveMQ管理页面

点击 `Manage ActiveMQ broker`进入管理页面    用户名和密码  均为 admin 



登录账号信息：

运营商管理后台   账号：admin   密码：123456





网页静态化生成页面生成的位置：pagedir=f:\\project\\item\\