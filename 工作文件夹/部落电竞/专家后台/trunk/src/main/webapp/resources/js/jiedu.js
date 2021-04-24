var jiedu = {};
jiedu.dialog = {
	alert : function(message){
		$("#dialog-alert p.success_word").text(message);
		return !webAlert({title:false, content:$("#dialog-alert")[0]});
	},
	reload : function(message){
		$("#dialog-reload p.success_word").text(message);
		return !webAlert({title:false, content:$("#dialog-reload")[0]});
	},
	success : function(url){
		$("#dialog-success a.success_look").prop('href', url);
		return !webAlert({title:false, content:$("#dialog-success")[0]});
	},
	closeWebdialog: function() { 	//关闭页面弹层.
		if(webAlert&&webAlert.list){
		  for(var id in webAlert.list){
			  webAlert.list[id].close();
		  }
		 } 
	},
	errorInfo : function(message){
		$("#dialog-error p.return_word").text(message);
		return !webAlert({title:false, content:$("#dialog-error")[0]});
	},
	error : function(){
		return !webAlert({title:false, content:$("#dialog-error")[0]});
	}
};

/** 计算长度 汉字占2个，字符占1个 
 * 把双字节的替换成两个单字节的然后再获得长度
 * */
jiedu.getLength = function(str){
	if (str == null) return 0;
	  if (typeof str != "string"){
	    str += "";
	  }
	  return str.replace(/[^\x00-\xff]/g,"01").length;
}