# Xmall



**Xmall-parent 聚合工程（父工程）**

**Xmall-pojo 通用实体类层**

**Xmall-dao 通用数据访问层**

**Xmall-common 通用工具类模块**

**Xmall-xxxxx-interface  某服务层接口** 

​	-sellergoods-interface 商家商品服务接口模块

​	-content-interface 广告管理服务接口模块

**Xmall-xxxxx-service   某服务层实现**

​	-sellergoods-service 商家商品服务模块    [port：9001]

​	-content-service 广告管理服务模块   [port：9002]

**Xmall-xxxxx-web     某web工程**  

​	-manager-web  运营商管理后台    [port：9101]

​		运营商管理依赖sellergoods-service和content-service ，所以运行它时需要先启动这两个service

​	-shop-web  商家管理后台    [port：9102]

​		商家管理依赖 sellergoods-service



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