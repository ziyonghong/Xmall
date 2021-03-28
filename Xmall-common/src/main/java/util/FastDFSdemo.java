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
		// 1�����������ļ��������ļ��е����ݾ��� tracker ����ĵ�ַ��
		ClientGlobal.init("F:/project/xmall/Xmall-shop-web/src/main/resources/config/fdfs_client.conf");
		// 2������һ�� TrackerClient ����ֱ�� new һ����
		TrackerClient trackerClient = new TrackerClient();
		// 3��ʹ�� TrackerClient ���󴴽����ӣ����һ�� TrackerServer ����
		TrackerServer trackerServer = trackerClient.getConnection();
		// 4������һ�� StorageServer �����ã�ֵΪ null
		StorageServer storageServer = null;
		// 5������һ�� StorageClient ������Ҫ�������� TrackerServer ����StorageServer ������
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 6��ʹ�� StorageClient �����ϴ�ͼƬ��
		// ��չ��������.��
		String[] strings = storageClient.upload_file("F:/project/xmall/Xmall-shop-web/src/main/webapp/img/banner5.jpg",
				"jpg", null);
		// 7���������顣����������ͼƬ��·����
		for (String string : strings) {
			System.out.println(string);
		}

		
	
		// 1��ȡ�ļ�����չ��
//		String originalFilename = file.getOriginalFilename();
//		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//		try {
//			// 2������һ�� FastDFS �Ŀͻ���
//			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
//			// 3��ִ���ϴ�����
//			String path = fastDFSClient.uploadFile("F:/project/xmall/Xmall-shop-web/src/main/webapp/img/ad.jpg","jpg",null);
//			// 4��ƴ�ӷ��ص� url �� ip ��ַ��ƴװ�������� url
//			String url = "192.128.25.133" + path;
//			System.out.println("chenggong" +url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
