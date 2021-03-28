package util;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;



public class FastDFSdemo {
	public static void main(String[] args) throws IOException, MyException {
		// 1、加载配置文件，配置文件中的内容就是 tracker 服务的地址。
		ClientGlobal.init("F:/project/xmall/Xmall-shop-web/src/main/resources/config/fdfs_client.conf");
		// 2、创建一个 TrackerClient 对象。直接 new 一个。
		TrackerClient trackerClient = new TrackerClient();
		// 3、使用 TrackerClient 对象创建连接，获得一个 TrackerServer 对象。
		TrackerServer trackerServer = trackerClient.getConnection();
		// 4、创建一个 StorageServer 的引用，值为 null
		StorageServer storageServer = null;
		// 5、创建一个 StorageClient 对象，需要两个参数 TrackerServer 对象、StorageServer 的引用
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 6、使用 StorageClient 对象上传图片。
		// 扩展名不带“.”
		String[] strings = storageClient.upload_file("F:/project/xmall/Xmall-shop-web/src/main/webapp/img/banner5.jpg",
				"jpg", null);
		// 7、返回数组。包含组名和图片的路径。
		for (String string : strings) {
			System.out.println(string);
		}

		
	
		// 1、取文件的扩展名
//		String originalFilename = file.getOriginalFilename();
//		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//		try {
//			// 2、创建一个 FastDFS 的客户端
//			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
//			// 3、执行上传处理
//			String path = fastDFSClient.uploadFile("F:/project/xmall/Xmall-shop-web/src/main/webapp/img/ad.jpg","jpg",null);
//			// 4、拼接返回的 url 和 ip 地址，拼装成完整的 url
//			String url = "192.128.25.133" + path;
//			System.out.println("chenggong" +url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
