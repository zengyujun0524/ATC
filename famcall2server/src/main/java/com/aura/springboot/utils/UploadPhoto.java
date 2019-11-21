package com.aura.springboot.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.aura.springboot.entity.Constant;
import com.aura.springboot.web.UserController;

public class UploadPhoto {

	private static Logger log = LoggerFactory.getLogger(UserController.class);
       
	
	/**
	 * 	上传图片
	 * @param file		图片文件
	 * @param folder	文件夹名称
	 * @return
	 * @throws Exception
	 */
	public static String upload(MultipartFile file,String folder, Integer userId) throws Exception {

		String fileName = "";
		if (!file.isEmpty()) {
			
			BufferedOutputStream out = null;
			fileName = file.getOriginalFilename();
			// 获取上传文件的后缀
			String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			//更改文件名字
			fileName = userId +"_"+ System.currentTimeMillis() + "." + fileSuffix;
			// 这个路径是服务器上tomcat的路径 
			// /data0/mccree/tomcat/tomcat8/webapps  阿里云
			// /home/mccree/tomcat8/tomcat/webapps	238内部服务器
			Path path = Paths.get("/home/mccree/tomcat8/tomcat/webapps").resolve(folder);
			String pathString = path.toAbsolutePath().toString();

			log.info("文件存储路径 ==============》" + pathString);
			File fileSourcePath = new File(pathString);
			if (!fileSourcePath.exists()) {
				fileSourcePath.mkdirs();
			}
			out = new BufferedOutputStream(new FileOutputStream(new File(fileSourcePath, fileName)));
			out.write(file.getBytes());
			out.flush();
			out.close();
			log.info("用户："+userId+"，上传的文件名为==============》" + fileName);
		}
		//图片请求地址 http://192.168.1.238:8080/ + fileName
		// http://47.75.47.133:8080/images/2.png
		String photoPath  = "http://192.168.1.238:8080/"+folder+"/" + fileName;
		return photoPath;

	}
}
