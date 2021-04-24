package com.esportzoo.esport.controller.aliyun;

import java.io.Serializable;

/**
 * URL上传视频完成
 * @author tingting.shen
 * @date 2019/06/04
 */
public class UploadByURLComplete implements Serializable{

	private static final long serialVersionUID = -4410774808714676526L;
	
	/** 事件产生时间, 为UTC时间：yyyy-MM-ddTHH:mm:ssZ */
	private String EventTime;
	/** 事件类型，固定为UploadByURLComplete */
	private String EventType;
	/** 视频ID，当视频拉取成功后会有该字段 */
	private String VideoId;
	/** 上传任务ID */
	private String JobId;
	/** 源文件URL地址 */
	private String SourceURL;
	/** 处理结果，取值：success(成功)，fail(失败) */
	private String Status;
	/** 失败错误码，当Status为fail时会有该字段 */
	private String ErrorCode;
	/** 失败错误信息，当Status为fail时会有该字段 */
	private String ErrorMessage;
	
	
	public String getEventTime() {
		return EventTime;
	}
	public String getEventType() {
		return EventType;
	}
	public String getVideoId() {
		return VideoId;
	}
	public String getJobId() {
		return JobId;
	}
	public String getSourceURL() {
		return SourceURL;
	}
	public String getStatus() {
		return Status;
	}
	public String getErrorCode() {
		return ErrorCode;
	}
	public String getErrorMessage() {
		return ErrorMessage;
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
	public void setJobId(String jobId) {
		JobId = jobId;
	}
	public void setSourceURL(String sourceURL) {
		SourceURL = sourceURL;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}
	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	
}
