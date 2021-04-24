(function (factory) {
    "use strict";
    if (typeof define === 'function' && define.amd) {
        // using AMD; register as anon module
        define(['jquery'], factory);
    } else {
        // no AMD; invoke directly
        factory( (typeof(jQuery) != 'undefined') ? jQuery : window.Zepto );
    }
}(function($) {
	"use strict";
	
	//分页方法
	$.fn.JieDuPage = function(options){
		var settings = $.extend({}, defaults, options);

		return {
			render : function(options){
				options = options || {};
				settings = $.extend( {}, settings, options);
				render(settings);
			}
		}
	};
	
	//分页默认参数
	var defaults = {
		// doT分页列表	
		dotList : {containerId : "listContainer", dotId : "dot-list"},
		// doT分页页码
		dotPage : {containerId : "pageContainer", dotId : "dot-page"},	
		// 请求URL
		url : "",
		// 请求参数
		data : {},
		// 每页数量
    	pageSize : 10,
    	// 当前页数
    	pageNo : 1,
    	//请求成功后回调方法
    	afterSuccess: function(json){}
	};
	
	//渲染页面
	var render = function(settings){
		var data = $.extend({}, settings.data, {
			pageSize : settings.pageSize,
    		pageNo : settings.pageNo
    	});
		
		$.getJSON(settings.url, data, function(json){
			if(json.isSuccess){
				settings = $.extend({}, settings, {
					pageSize : json.model.pageSize,
		    		pageNo : json.model.pageNo
		    	});
				
				$("#" + settings.dotList.containerId).html(doT.template($("#" + settings.dotList.dotId).html())(json.model));
				$("#" + settings.dotPage.containerId).html(doT.template($("#" + settings.dotPage.dotId).html())(json.model));
				 
				if(json.model.totalCount > 0){
					$("#" + settings.dotPage.containerId + " [data-num]").unbind("click").bind("click", function(){
						var num = $(this).data("num");
						if(num){
							settings.pageNo = num;
							render(settings);
						}
					});
				}
				
				//回调
				settings.afterSuccess(json);
			}else if('NOT_LOGIN' == json.code){
				window.location.href = "/login";
			}
		});
	}
}));