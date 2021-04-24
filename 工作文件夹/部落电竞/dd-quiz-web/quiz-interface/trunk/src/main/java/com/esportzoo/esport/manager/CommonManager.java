package com.esportzoo.esport.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.hutool.core.util.ObjectUtil;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.common.ClientAdPicServiceClient;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.CmsTypeDefineConstant;
import com.esportzoo.esport.domain.ClientAdPic;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.util.VideoUtils;

/**
 * @description: 公共数据查询
 *
 * @author: Haitao.Li
 *
 * @create: 2019-07-25 15:18
 **/
@Component
public class CommonManager {

	private transient static final Logger logger = LoggerFactory.getLogger(CommonManager.class);
	@Autowired
	@Qualifier("clientAdPicServiceClient")
	ClientAdPicServiceClient clientAdPicServiceClient;
	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;
	public static long max_file_size_picture = 5 * 1024 * 1024L;
	public static long max_file_size_video = 100 * 1024 * 1024L;

	public List<ClientAdPic> getClientAdPic(Integer bannerType, Integer clientType) {
		if (bannerType == null) {
			logger.error("查询广告出错，bannerType.is.null");
			return null;
		}
		ModelResult<List<ClientAdPic>> listAdResult = clientAdPicServiceClient.queryListByAdType(bannerType);
		if (clientType == null) {
			clientType = ClientType.WXXCY.getIndex();
		}
		List<ClientAdPic> clientAdPicList = listAdResult.getModel();
		Integer finalClientType = clientType;
		if (clientAdPicList.size() > 1) {
			clientAdPicList = clientAdPicList.stream().filter(x -> x.getClientType().equals(finalClientType))
					.collect(Collectors.toList());
		}

		return clientAdPicList;

	}

	/** 根据clientType,渠道号和key 取系统配置参数 */
	public SysConfigProperty getSysConfigByKey(String key, Integer clientType,Long agentId) {
		if (null == ClientType.valueOf(clientType)) {
			clientType = ClientType.UNKNOW.getIndex();
		}
		if(agentId == null){
			agentId = 0L;
		}
		SysConfigProperty sysConfigProperty = sysConfigPropertyServiceClient.getSysConfigPropertyByKey(key,clientType,agentId);
		return sysConfigProperty;
	}

	/**
	 * 上传图片
	 * 
	 * @param files
	 * @param userId
	 * @return
	 */
	public String uploadImage(CommonsMultipartFile[] files, String uploadPath, String resPath, Integer typeId) {
		String res = "";
		List<String> imgList = new ArrayList<>();
		// 统一根据当前时间再创建一级文件夹
		String nowDay = DateUtil.getDateString3(new Date());
		uploadPath = uploadPath + "/" + nowDay;
		resPath = resPath + "/" + nowDay;
		long max_file_size = null != typeId && typeId.intValue() == CmsTypeDefineConstant.USER_VIDEO.getIndex() ? max_file_size_video : max_file_size_picture;
		try {
			for (int i = 0; i < files.length; i++) {
				String imageUrl = "";
				if (files[i].getSize() > max_file_size) {
					logger.info("上传类型[{}]大于当前最大值", typeId);
					continue;
				}
				String fileName = files[i].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					// 创建输出文件对象
					String suffix = "";
					if (fileName.contains(".jpeg")) {
						suffix = fileName.substring(fileName.length() - 5, fileName.length());
					} else {
						suffix = fileName.substring(fileName.length() - 4, fileName.length());
					}

					File outFile = FileUtils.getFile(
							uploadPath + File.separator + System.currentTimeMillis() + "_" + i + suffix);
					// 拷贝文件到输出文件对象
					FileUtils.copyInputStreamToFile(files[i].getInputStream(), outFile);
					logger.info("文件名{},替换{},url{}", outFile.getPath(), uploadPath, resPath);
					imageUrl = outFile.getPath().replace(uploadPath, resPath);
					imgList.add(imageUrl);
				}
			}
			if (!imgList.isEmpty()) {
				res = StringUtils.join(imgList, ",");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("上传图片出错", e.getMessage());
		}
		return res;

	}

	/**
	 * @param uploadImage
	 * @param uploadPath
	 * @param resPath
	 * @return
	 */
	public String getTitleImgByVideo(String uploadImage, String uploadPath, String resPath) {
		String res = "";
		try {
			logger.info("上传视频第一帧图片,视频源地址:{}", uploadImage);
			String fileName = System.currentTimeMillis() + "";
			String nowDay = DateUtil.getDateString3(new Date());
			uploadPath = uploadPath + "/" + nowDay;
			VideoUtils.randomGrabberFFmpegImage(uploadImage, uploadPath, fileName);
			res = resPath + "/" + nowDay + "/" + fileName + ".jpg";
			logger.info("上传视频第一帧图片:{}", res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
