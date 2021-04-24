package com.esportzoo.esport.controller.aliyun;

import java.io.Serializable;

/**
 * 视频上传完成bean
 * @author tingting.shen
 * @date 2019/06/04
 */
public class FileUploadComplete implements Serializable{

	private static final long serialVersionUID = 1091477305369719372L;
	
	/** 事件产生时间, 为UTC时间：yyyy-MM-ddTHH:mm:ssZ */
	private String EventTime;
	/** 事件类型，固定为FileUploadComplete */
	private String EventType;
	/** 视频ID */
	private String VideoId;
	/** 上传的文件大小，单位：Byte(字节) */
	private Long Size;
	/** 上传文件的Url地址 */
	private String FileUrl;


	public String getEventTime() {
		return EventTime;
	}

	public String getEventType() {
		return EventType;
	}

	public String getVideoId() {
		return VideoId;
	}

	public Long getSize() {
		return Size;
	}

	public String getFileUrl() {
		return FileUrl;
	}

	public void setEventTime(String eventTime) {
		EventTime = eventTime;
	}

	public void setEventType(String eventType) {
		EventType = eventType;
	}

	public void setVideoId(String videoId) {
		VideoId = videoId;
	}

	public void setSize(Long size) {
		Size = size;
	}

	public void setFileUrl(String fileUrl) {
		FileUrl = fileUrl;
	}
	
}
