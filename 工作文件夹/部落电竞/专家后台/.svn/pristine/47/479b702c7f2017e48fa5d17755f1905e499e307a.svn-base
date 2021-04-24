var needImg = false;

/** 图片上传 fileUpload input type为file的id* */
function ajaxFileUpload(fileUpload) {
	var file = $("#" + fileUpload).val();
	if (needImg && file == "") {
		alert("请选择上传的图片");
		return;
	}
	var reg = /\.png$|\.jpg$|\.gif$|\.bmp$/i;

	if (needImg && !reg.test(file)) {
		alert('请上传正确的图片格式，如：png,jpg,gif,bmp');
		return;
	}
	$.ajaxFileUpload({
		url : '/image/upload',
		secureuri : false,
		fileElementId : fileUpload,
		dataType : 'text',
		success : function(data) {
			var json = eval('(' + data + ')');
			if(json.error == 0 ) {
				console.log(json.url);
				$("#jq_logo").val(json.url);
			}else {
				alert('上传失败！');
			}
//			var reg = /<pre.*>(.+)<\/pre>/i;
//			if (reg.test(data)) {
//				var jsonMessage = reg.exec(data)[1];
//				var json = eval('(' + jsonMessage + ')');
//				if (json.code != 0) {
//					$('#' + fileUpload + "Div").show().find('img').attr('src', json.path);
//					$('#giftImgPath').val(json.savePath);
//					alert('上传成功！');
//				} else {
//					alert('上传失败！');
//				}
//			} else {
//				alert('上传失败！')
//			}
		}
	});
	return false;
}