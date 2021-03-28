package com.zyh.sellergoods.controller;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

@RestController
public class UploadController {
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;//�ļ���������ַ

	@RequestMapping("/upload")
	public Result upload( MultipartFile file){				
		//1��ȡ�ļ�����չ��
		String originalFilename = file.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		try {
//           //2������һ�� FastDFS �Ŀͻ���
//			FastDFSClient fastDFSClient  = new FastDFSClient("classpath:config/fdfs_client.conf");
//			//3��ִ���ϴ�����
//			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
//			//4��ƴ�ӷ��ص� url �� ip ��ַ��ƴװ�������� url
//			String url = FILE_SERVER_URL + path;
			
			//1�����������ļ�����
			ClientGlobal.init("classpath:config/fdfs_client.conf");
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
			String[] strings = storageClient.upload_file(file.getBytes(), extName,null);
			// 7���������顣����������ͼƬ��·����
			for (String string : strings) {
				System.out.println(string);
			}
			String url = FILE_SERVER_URL + strings[1];
			return new Result(true,url);			
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "�ϴ�ʧ��");
		}		
	}	
}
