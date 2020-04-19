package com.ego.manage.service.impl;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ego.commons.utils.FtpUtil;
import com.ego.manage.service.PicService;

@Service
public class PicServiceImpl implements PicService {
	@Value("${ftpclient.host}")
	private String host;
	@Value("${ftpclient.port}")
	private int port;
	@Value("${ftpclient.username}")
	private String username;
	@Value("${ftpclient.password}")
	private String password;
	@Value("${ftpclient.basePath}")
	private String basePath;
	@Value("${ftpclient.filepath}")
	private String filePath;
	@Override
	public Map<String, Object> upload(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		String filename = UUID.randomUUID().toString()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		boolean result = false;
		try {
			result = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (result) {
				map.put("error", 0);
				map.put("url", "http://"+host+":80"+filePath+filename);
			}else {
				map.put("error", 1);
				map.put("msg", "图片上传失败");
			}
		}
		return map;
	}

}
