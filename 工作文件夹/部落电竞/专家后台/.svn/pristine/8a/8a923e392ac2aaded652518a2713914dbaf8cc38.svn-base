// JavaScript Document
;(function($){
	$.fn.extend({
		marquee: function(options){
			var defaults = {
					direction: "left", //up down left right
					auto: true,
					speed: 10,
					marqueeElem: null,
					btn: null
				};
			var options = $.extend(defaults, options || {});
			
			return this.each(function(){
				var selfJq = $(this);
				var selfWidth = selfJq.width();
				var selfHeight = selfJq.height();				
				var selfDom = selfJq.get(0);
				var marWrapJq = options.marqueeElem ? $(options.marqueeElem, selfJq) : selfJq.children();
				var marWrapDom = marWrapJq.get(0);
				var btn = options.btn ? $(options.btn) : null;
				
				if(options.direction == "left" || options.direction == "right"){
					var tmpWidth = 0;
					marWrapJq.children().each(function(){
						tmpWidth += $(this).outerWidth(true);
					})
					marWrapJq.wrap($("<div />").css("width",2*tmpWidth)).css("float","left");
				}
				
				var scrollHeight = marWrapDom.scrollHeight; //获取滚动元素初始实际高度
				var scrollWidth = marWrapDom.scrollWidth;   //获取滚动元素初始实际宽度
				
				if((options.direction == "left" || options.direction == "right") && selfWidth > scrollWidth) return;
				if((options.direction == "up" || options.direction == "down") && selfHeight > scrollHeight) return;
				
				marWrapDom.innerHTML += marWrapDom.innerHTML;
				
				function marquee(){
					switch(options.direction){
							case "up":
								if(scrollHeight - selfDom.scrollTop <= 0){//当selfDom.scrollTop等于marWrapDom初始实际高度时
									selfDom.scrollTop -= scrollHeight;    //恢复selfDom.scrollTop初始值
								}else{
									selfDom.scrollTop++;
								}
								break;
							case "right":
								if(selfDom.scrollLeft <= 0){
									selfDom.scrollLeft = scrollWidth;
								}else{
									selfDom.scrollLeft--;
								}
								break;
							case "down":
								if(selfDom.scrollTop <= 0){
									selfDom.scrollTop = scrollHeight;
								}else{
									selfDom.scrollTop--;
								}
								break;
							default:
								if(scrollWidth - selfDom.scrollLeft <= 0){
									selfDom.scrollLeft -= scrollWidth;
								}else{
									selfDom.scrollLeft++;
								}
					}
				}
				if(options.auto) var timer = setInterval(marquee,options.speed);
				selfJq.hover(function(){clearInterval(timer);},function(){timer = setInterval(marquee,options.speed);});				   
			})
		}			
	})
})(jQuery);