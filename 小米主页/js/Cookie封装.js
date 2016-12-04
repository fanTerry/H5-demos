
function setCookie(img,mKey,mValue,mExpires){
	var cookieStr = "";
	if (mKey && mValue) {
		// key=value
		cookieStr = cookieStr + img + ','+ encodeURIComponent(mKey) + ',' + encodeURIComponent(mValue);
	}
	
	// 数据类型判断 instanceof   
	// mExpires 必须是Date时间类型
	if (mExpires instanceof Date) {
		// key=value; expires=date
		cookieStr = cookieStr + "; expires=" + mExpires;
	}
	
	// 设置cookie
	document.cookie = cookieStr;
	return document.cookie;
}

// 提供一个key就去现有的Cookie中寻找与key对应的value并且返回
function getCookieWithKey(){
	// "name1=1; name2=2; name3=3"
	if (document.cookie.length == 0) {
		return;
	}
	// cookie在拆分数组时使用的分隔符"; "
	// 对编码过的cooike进行解码后再分割
	// console.log(document.cookie);// %E5%95%86%E5%93%814=%2448
	var arr = decodeURIComponent(document.cookie).split("; ");
	// console.log(decodeURIComponent(document.cookie));// 商品4=$48
	for (var i = 0;i < arr.length;i++) {
		// name1=1
		var subArr = arr[i].split(",");
		// 找到了cookie中与mKey对应的value 返回[key,value]
			return subArr[1];
	}
	return;
}
function getCookieWithValue(){
	// "name1=1; name2=2; name3=3"
	if (document.cookie.length == 0) {
		return;
	}
	// cookie在拆分数组时使用的分隔符"; "
	// 对编码过的cooike进行解码后再分割
	// console.log(document.cookie);// %E5%95%86%E5%93%814=%2448
	var arr = decodeURIComponent(document.cookie).split("; ");
	// console.log(decodeURIComponent(document.cookie));// 商品4=$48
	for (var i = 0;i < arr.length;i++) {
		// name1=1
		var subArr = arr[i].split(",");
		// 找到了cookie中与mKey对应的value 返回[key,value]
			return subArr[2];
	}
	return;
}
function getCookieWithImg(){
	// "name1=1; name2=2; name3=3"
	if (document.cookie.length == 0) {
		return;
	}
	// cookie在拆分数组时使用的分隔符"; "
	// 对编码过的cooike进行解码后再分割
	// console.log(document.cookie);// %E5%95%86%E5%93%814=%2448
	var arr = decodeURIComponent(document.cookie).split("; ");
	// console.log(decodeURIComponent(document.cookie));// 商品4=$48
	for (var i = 0;i < arr.length;i++) {
		// name1=1
		var subArr = arr[i].split(",");
		// 找到了cookie中与mKey对应的value 返回[key,value]
			return subArr[0];
	}
	return;
}

// 删除cooike  
function removeCookie(mKey){
	var d = new Date();
	// 删除这个键值对   不在乎value是什么   
	document.cookie = "";
}



