package com.esportzoo.esport.expert.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping(value="/fileupload")
public class FileUploadController {
	
	private transient final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	private static String prefix= "上传图片_";
	
	private static List<String> allowPicTypes = new ArrayList<>();
    static {
        allowPicTypes.add("jpg");  
        allowPicTypes.add("jpeg");  
        allowPicTypes.add("gif");  
        allowPicTypes.add("png"); 
    }

    @Value("${uploadRoot}")
    private String uploadRoot;
    
    @Value("${uploadURL}")
    private String uploadURL;
    
    
    @RequestMapping(value="/uploadImage", method={RequestMethod.POST})
    @ResponseBody 
    public Map<String, Object> uploadImage(HttpServletRequest request, HttpServletResponse response){
    	Map<String, Object> map = new HashMap<>();
    	try {
    		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
    		Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
    		Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
    		List<String> dataList = new ArrayList<>();
    		while (it.hasNext()) {
    			Map.Entry<String, MultipartFile> mapEnt = it.next();
    			MultipartFile file = mapEnt.getValue();
    			if (file != null && file.getSize() > 0) {
    				 String originalfileName = file.getOriginalFilename();
    		         String ext = originalfileName.substring(originalfileName.lastIndexOf(".") + 1, originalfileName.length());
    		         logger.info(prefix + "originalfileName={},ext={}", originalfileName, ext);
    		         Calendar now = Calendar.getInstance();
    		         originalfileName = now.getTimeInMillis() + originalfileName;
    		         if(allowPicTypes.contains(ext.toLowerCase())) {  //如果扩展名属于允许上传的类型，则创建文件  
    		        	 String fileName = originalfileName.substring(0, originalfileName.lastIndexOf("."))+ "." + ext;
    		        	 String datePath = now.get(Calendar.YEAR) + "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.DATE) + "/";
    		        	 String pathDir = uploadRoot + datePath;
    		        	 File pathDirFile = new File(pathDir);
    		        	 String path = pathDir + fileName;
    		        	 String picUrl = uploadURL + datePath + fileName;
    		        	 logger.info(prefix + "path={},picUrl={}", path, picUrl);
    		        	 if (!pathDirFile.exists()){
    		        		 pathDirFile.mkdirs();
    		        	 }
    		        	 File tofile = new File(path);  
    		        	 file.transferTo(tofile); 
    		        	 ImageIO.read(new FileInputStream(tofile));
    		        	 dataList.add(picUrl);
    		         }
    			}
    		}
    		logger.info(prefix + "循环后");
    		logger.info(prefix + JSON.toJSONString(dataList.toArray()));
    		map.put("errno", 0);
    		map.put("data", dataList.toArray());
    		return map;
		} catch (Exception e) {
			logger.info(prefix + "发生异常excetion={}", e.getMessage(), e);
			map.put("errno", "-100");//0 表示没有错误   非0表示有错误
			map.put("errMsg", e.getMessage());
			return map;
		}
    }
    
}
