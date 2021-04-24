<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty voiceAuth}">
<div class="jd_zhaiyao ml40 pt30" id="enableVoiceDiv">
	<span class="fl fs14" style="display: block; width: 142px;">语音内容（选填）：</span>
	<div class="profit_price" style="display: inline-block; vertical-align: middle;">
		<input type="radio" name="enableVoice" value="0" checked="true" /> 不启用 <input type="radio" name="enableVoice"
			value="1" /> 启用
	</div>
</div>
<div class="jd_zhaiyao ml40 pt30" style="display: none;" id="voiceUrlDiv">
	<div class="profit_price"
		style="display: inline-block; vertical-align: middle; margin-left: 203px; padding-bottom: 20px">
		<input type="radio" name="freeSecond" value="0" checked="true" />付费前免费听 <input type="text" id="setFreeSecond"
			name="setFreeSecond" value="10" style="width: 50px; margin-left: 0" />秒 <input type="radio" name="freeSecond"
			value="1" />付费前整条均可免费听 &nbsp;&nbsp;&nbsp;&nbsp;注意：当解读属于部分用户免费时，这条语音也可以免费听</span>
	</div>

	<div class="profit_price" style="margin-left: 180px; padding-bottom: 10px">
		<input type="file" id="audioFile" style="display: none;" name="audioFile" /> <span id="uploadMsg"
			style="display: none;">开始上传</span>
		<button id="fileUpload" style="width: 80px; height: 30px; margin-left: 20px; font-size: 12;">选择文件</button>
		&nbsp;&nbsp;&nbsp;&nbsp;格式支持mp3，文件大小不超过20M，语音时长不超过20分钟
	</div>
	<div class="profit_price" id="playAudio" style="float: left; width: 100%; margin-left: 220px; vertical-align: middle;">
		<video id="player" controls="controls" style="height:50px;width:400px" type="video/mp4"></video>
		<span id="fileInfo" style="display: none;height:50px;font-size:13pt"></span>
	</div>
	<input type="hidden" id="voiceUrl" name="voiceUrl" /> 
	<input type="hidden" id="voiceSecond" name="voiceSecond" />
	<input type="hidden" id="voiceFreeSecond" name="voiceFreeSecond" />
</div>
</c:if>