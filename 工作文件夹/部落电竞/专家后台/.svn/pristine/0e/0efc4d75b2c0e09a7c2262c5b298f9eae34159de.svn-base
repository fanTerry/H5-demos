<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty voiceAuth}">
<div class="intro_reason intro_reason2" id="improVoiceDiv" style="display: none;">
	<span class="fl fs14" style="display: block; width: 142px;">语音宣传（非必填）：</span>
	<div class="profit_price" style="display: inline-block; vertical-align: middle; margin-left: 50px;">
		<input type="radio" name="enableSummaryVoice" value="0" checked="true" /> 不启用 <input type="radio"
			name="enableSummaryVoice" value="1" /> 启用
	</div>
	&nbsp;&nbsp;&nbsp;&nbsp;语音宣传所有用户可免费收听!内容不得含违法，虚假内容或联系方式

	<div class="jd_zhaiyao ml40 pt30" id="summaryDiv" style="display: none">
		<div class="profit_price" style="margin-left: 220px; padding-bottom: 10px">
			<input type="file" id="summaryAudioFile" style="display: none;" name="summaryAudioFile" /> <span
				id="uploadSummaryMsg" style="display: none;">开始上传</span>
			<button id="summaryFileUpload" style="width: 80px; height: 30px; margin-left: 20px; font-size: 12;">选择文件</button>
			&nbsp;&nbsp;&nbsp;&nbsp;格式支持mp3，文件大小不超过20M，语音时长不超过20分钟
		</div>
		<div class="profit_price" id="summaryPlayAudio"
			style="float: left; width: 100%; margin-left: 220px; vertical-align: middle;">
			<video id="summaryPlayer" controls="controls" style="height:50px;width:400px" type="video/mp4"></video>
			<span id="summaryFileInfo" style="display: none;height:50px;font-size:13pt"></span>
		</div>
		<input type="hidden" id="improvVoiceUrl" name="improvVoiceUrl" /> 
		<input type="hidden" id="improvVoiceSecond" name="improvVoiceSecond" />
	</div>
</div>
</c:if>