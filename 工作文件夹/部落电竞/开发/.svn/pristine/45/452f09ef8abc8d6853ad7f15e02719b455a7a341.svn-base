package com.esportzoo.esport.controller.aliyun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse.Video;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.aliyun.AliyunVideo;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.CmsContentStatus;
import com.esportzoo.esport.constants.cms.YunVideoStatus;
import com.esportzoo.esport.domain.CmsContent;
import com.esportzoo.esport.option.CmsContentParam;
import com.esportzoo.esport.util.RealIPUtils;

/**
 * @author tingting.shen
 * @date 2019/06/03
 */

@Controller
@RequestMapping("aliyunVideo")
public class AliyunVideoController {
	
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String logPrefix= "处理阿里云视频通知_";
	private static String redisKeyPrefix = "aliyunVideoProcessNotify_" ;
	
	@Autowired
	private CmsContentServiceClient cmsContentServiceClient;
	@Autowired
	private AliyunVideo aliyunVideo;
	@Autowired
	private RedisClient redisClient;

	
	@RequestMapping(value = "/processNotify")
	@ResponseBody
	public CommonResponse<Void> processNotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info(logPrefix + "开始");
			String serviceIp = RealIPUtils.getRealIp();
        	logger.info(logPrefix + "获得服务器IP为serviceIp={}", serviceIp);
        	if (serviceIp.equals("192.168.9.230")) {
        		logger.info(logPrefix + "是daily环境不处理");
        		return CommonResponse.withErrorResp("是daily环境不处理");
        	}
        	else if (serviceIp.equals("47.112.96.253") || serviceIp.equals("172.30.30.168")) {
        		logger.info(logPrefix + "是beta环境不处理");
        		return CommonResponse.withErrorResp("是beta环境不处理");
        	}
			if (!AliyunRequestValid.passValid(request, aliyunVideo)) {
				logger.info(logPrefix + "回调鉴权失败");
				return CommonResponse.withErrorResp("回调鉴权失败");
			}
			String notifyStr = getNotifyStr(request);
			if (StringUtils.isBlank(notifyStr)) {
				return CommonResponse.withErrorResp("通知穿串为空");
			}
			JSONObject jsonObject = JSON.parseObject(notifyStr);
			String eventType = jsonObject.getString("EventType");
			if (eventType.equals(AliyunEventList.fileUploadComplete)) {
				processFileUploadComplete(notifyStr);
			}
			return CommonResponse.withSuccessResp(null);
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常，exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	/** 处理视频上传完成  */
	private void processFileUploadComplete(String jsonStr) {
		String prefix = logPrefix + "处理视频上传完成_";
		try {
			logger.info(prefix + "接收到的参数jsonStr={}", jsonStr);
			FileUploadComplete fileUploadComplete = JSON.parseObject(jsonStr, FileUploadComplete.class);
			String videoId = fileUploadComplete.getVideoId();
			if (StringUtils.isBlank(videoId)) {
				logger.info(prefix + "接受到的视频id为空，不处理");
				return;
			}
			String redisKey = redisKeyPrefix + videoId;
			if (redisClient.exists(redisKey)) {
				logger.info(prefix + "redis中已经存在该缓存,不重复处理,videoId={}", videoId);
				return;
			}
			boolean setSuccess = redisClient.set(redisKey, videoId, 60);
			logger.info(prefix + "存至redis缓存成功={}，videoId={}", setSuccess, videoId);
			CmsContent cmsContent = getCmsContentByVideoId(videoId);
			if (cmsContent == null) {
				logger.info(prefix + "没有查到对应的内容，videoId={}", videoId);
				//出现这种情况的有：1文件太大，admin后台上传发生异常，阿里云后台显示正常但播放不了，避免占用空间，删除
				deleteVideo(videoId);
				return;
			}
			if (cmsContent.getYunVideoStatus().intValue() == YunVideoStatus.SUCCESS.getIndex() && StringUtils.isNotBlank(cmsContent.getYunVideoUrl())) {
				logger.info(prefix + "改视频已是成功状态，不需要再处理，videoId={}，cmsContentId={}", videoId, cmsContent.getId());
				return;
			}
			Long size = fileUploadComplete.getSize();
			logger.info(prefix + "文件大小为={}M，videoId={}，cmsContentId={}", size/1024/1024, videoId, cmsContent.getId());
			if (size==null || size.longValue()<1024*1024) {
				logger.info(prefix + "文件大小小于1M,准备删除，videoId={}，cmsContentId={}", videoId, cmsContent.getId());
				deleteVideo(videoId);
				cmsContent.setYunVideoStatus(YunVideoStatus.FAIL.getIndex());
				cmsContent.setUpdateTime(Calendar.getInstance().getTime());
				cmsContentServiceClient.updateCmsContentById(cmsContent);
				logger.info(prefix + "文件大小小于1M,修改状态为失败，videoId={}，cmsContentId={}", videoId, cmsContent.getId());
				return;
			}
			Video video = getVideoDetailInfo(videoId);
			if (video == null || video.getStatus().equalsIgnoreCase("Uploading")) {
				logger.info(prefix + "没有获得视频详情或文件是上传中,准备删除，videoId={}，cmsContentId={}", videoId, cmsContent.getId());
				deleteVideo(videoId);
				cmsContent.setYunVideoStatus(YunVideoStatus.FAIL.getIndex());
				cmsContent.setUpdateTime(Calendar.getInstance().getTime());
				cmsContentServiceClient.updateCmsContentById(cmsContent);
				logger.info(prefix + "没有获得视频详情或文件是上传中,准备删除,修改状态为失败，videoId={}，cmsContentId={}", videoId, cmsContent.getId());
				return;
			}
			
			String videoUrl = getVideoUrl(videoId);
			cmsContent.setYunVideoUrl(videoUrl);
			cmsContent.setYunVideoStatus(YunVideoStatus.SUCCESS.getIndex());
			cmsContent.setStatus(CmsContentStatus.ISSUE.getIndex());
			cmsContent.setUpdateTime(Calendar.getInstance().getTime());
			cmsContent.setYunVideoLength(video.getDuration().intValue());
			cmsContentServiceClient.updateCmsContentById(cmsContent);
			logger.info(prefix + "修改状态为成功，videoId={}，cmsContentId={}", videoId, cmsContent.getId());
			return;
		} catch (Exception e) {
			logger.info(prefix + "发生异常，jsonStr={}", jsonStr);
		}
	}
	
	private CmsContent getCmsContentByVideoId(String videoId) {
		try {
			CmsContentParam condition = new CmsContentParam();
			condition.setYunVideoId(videoId);
			ModelResult<List<CmsContent>> modelResult = cmsContentServiceClient.queryList(condition);
			if (!modelResult.isSuccess()) {
				logger.info(logPrefix + "根据阿里云视频id获取内容接口返回错误， videoId={}, errorMsg={}", videoId, modelResult.getModel());
				return null;
			}
			List<CmsContent> list = modelResult.getModel();
			if (list==null || list.size()<=0) {
				logger.info(logPrefix + "根据阿里云视频id没有查询到内容， videoId={}", videoId);
				return null;
			}
			CmsContent cmsContent = list.get(0);
			logger.info(logPrefix + "根据阿里云视频id查到内容数量为{}，取第一个cmsContentId={}, videoId={}", list.size(), cmsContent.getId(), videoId);
			return cmsContent;
		} catch (Exception e) {
			logger.info(logPrefix + "根据阿里云视频id获取内容发生异常， e={}, videoId={}", e.getMessage(), videoId, e);
			return null;
		}
		
	}
	
	/**得到通知字符串*/
	private String getNotifyStr(HttpServletRequest request) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) { 
			  sb.append(temp);
			}
			br.close();
			logger.info(logPrefix + "得到通知字符串={}", sb.toString());
			return sb.toString();
		} catch (Exception e) {
			logger.info(logPrefix + "得到通知字符串发生异常，e={}", e.getMessage());
			return null;
		}
	}
	
	private String getVideoUrl(String videoId) {
		try {
			String videoUrl = "";
			DefaultAcsClient client = initVodClient(aliyunVideo.getAccessKeyId(), aliyunVideo.getAccessKeySecret(), aliyunVideo.getRegionId());
			GetPlayInfoRequest getPlayInfoRequest = new GetPlayInfoRequest();
			getPlayInfoRequest.setVideoId(videoId);
			GetPlayInfoResponse getPlayInfoResponse = client.getAcsResponse(getPlayInfoRequest);
			List<GetPlayInfoResponse.PlayInfo> playInfoList = getPlayInfoResponse.getPlayInfoList();
			logger.info(logPrefix + "获得视频地址，playInfoList.size={}", playInfoList.size());
			for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
				logger.info("获得视频地址，PlayURL={}", playInfo.getPlayURL());
				videoUrl = playInfo.getPlayURL();
			}
			logger.info(logPrefix + "获得最终视频地址={}", videoUrl);
			return videoUrl;
		} catch (Exception e) {
			logger.info(logPrefix + "获得视频地址发生异常e={}, videoId= {}", e.getMessage(), videoId, e);
			return null;
		}
	}
	
	private void deleteVideo(String videoId) {
		try {
			DefaultAcsClient client = initVodClient(aliyunVideo.getAccessKeyId(), aliyunVideo.getAccessKeySecret(), aliyunVideo.getRegionId());
			DeleteVideoRequest request = new DeleteVideoRequest();
		    //request.setVideoIds("VideoId1,VideoId2");支持传入多个视频ID，多个用逗号分隔
		    request.setVideoIds(videoId);
		    client.getAcsResponse(request);
		    logger.info(logPrefix + "删除视频成功， videoIds={}", videoId);
		} catch (Exception e) {
			logger.info(logPrefix + "删除视频发生异常，e={}, videoIds={}", e.getMessage(), videoId, e);
		}
	}
	
	private Video getVideoDetailInfo(String videoId) {
		try {
			DefaultAcsClient client = initVodClient(aliyunVideo.getAccessKeyId(), aliyunVideo.getAccessKeySecret(), aliyunVideo.getRegionId());
			GetVideoInfoRequest request = new GetVideoInfoRequest();
		    request.setVideoId(videoId);
		    GetVideoInfoResponse response = client.getAcsResponse(request);
		    Video video = response.getVideo();
		    return video;
		} catch (Exception e) {
			logger.info(logPrefix + "得到视频详情信息发生异常，exception={},videoId={}", e.getMessage(), videoId, e);
			return null;
		}
	}
	
	private DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret, String regionId) {
	    DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
	    DefaultAcsClient client = new DefaultAcsClient(profile);
	    return client;
	}
	
}
