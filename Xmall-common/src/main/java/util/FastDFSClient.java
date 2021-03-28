package util;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FastDFSClient {
//	public static void main(String[] args) throws Exception {
//		FastDFSClient fs=new FastDFSClient("classpath:config/fdfs_client.conf");
//		String string=fs.uploadFile("F:/project/xmall/Xmall-shop-web/src/main/webapp/img/ad.jpg",
//				"jpg", null);
//		System.out.println(string);
//	}

	private TrackerClient trackerClient = null;
	private TrackerServer trackerServer = null;
	private StorageServer storageServer = null;
	private StorageClient1 storageClient = null;
	
	public FastDFSClient(String conf) throws Exception {
		if (conf.contains("classpath:")) {
			conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
		}
//		System.out.println(conf);
		ClientGlobal.init(conf);
		trackerClient = new TrackerClient();
		trackerServer = trackerClient.getConnection();
		storageServer = null;
		storageClient = new StorageClient1(trackerServer, storageServer);
	}
	
	/**
	 * 涓婁紶鏂囦欢鏂规硶
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileName 鏂囦欢鍏ㄨ矾寰�
	 * @param extName 鏂囦欢鎵╁睍鍚嶏紝涓嶅寘鍚紙.锛�
	 * @param metas 鏂囦欢鎵╁睍淇℃伅
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
		String result = storageClient.upload_file1(fileName, extName, metas);
		return result;
	}
	
	public String uploadFile(String fileName) throws Exception {
		return uploadFile(fileName, null, null);
	}
	
	public String uploadFile(String fileName, String extName) throws Exception {
		return uploadFile(fileName, extName, null);
	}
	
	/**
	 * 涓婁紶鏂囦欢鏂规硶
	 * <p>Title: uploadFile</p>
	 * <p>Description: </p>
	 * @param fileContent 鏂囦欢鐨勫唴瀹癸紝瀛楄妭鏁扮粍
	 * @param extName 鏂囦欢鎵╁睍鍚�
	 * @param metas 鏂囦欢鎵╁睍淇℃伅
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
		
		String result = storageClient.upload_file1(fileContent, extName, metas);
		return result;
	}
	
	public String uploadFile(byte[] fileContent) throws Exception {
		return uploadFile(fileContent, null, null);
	}
	
	public String uploadFile(byte[] fileContent, String extName) throws Exception {
		return uploadFile(fileContent, extName, null);
	}
}
