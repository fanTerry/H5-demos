<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/x-dot-template" id="dot-page">
{{? it.dataList.length > 0}}
	{{? it.totalPages <= 5}}
		{{var start=1, end=it.totalPages;}}
	{{?? it.pageNo <= 2}}
		{{var start=1, end=5;}}
	{{?? it.totalPages -it.pageNo <= 2}}
		{{var start=it.totalPages-4, end=it.totalPages;}}
	{{??}}
		{{var start=it.pageNo-2, end=it.pageNo+2;}}
	{{?}}

	<div class="paging clearfix">
		<ul class="clearfix">
			<li class="pre_li" data-num={{=it.pageNo > 1 ? it.pageNo-1 : it.pageNo}}>上一页</li>
			
			{{ for(var i=start; i<=end; i++){ }}
				{{? i == it.pageNo}}
					<li class="paging_border">{{=i}}</li>
				{{??}}
					<li data-num="{{=i}}">{{=i}}</li>
				{{?}}
			{{ } }}

			<li class="pre_li" data-num={{=it.pageNo < it.totalPages ? it.pageNo+1 : it.pageNo}}>下一页</li>
		</ul>
		<div class="paging_total">共<em>{{=it.totalPages}}</em>页</div>
	</div>
{{?}}
</script>