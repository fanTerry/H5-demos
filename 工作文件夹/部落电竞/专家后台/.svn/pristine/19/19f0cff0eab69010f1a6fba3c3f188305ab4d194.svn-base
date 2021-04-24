<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/x-dot-template" id="dot-articleList">
	<table class="BMTable">
    	<tr style="background-color: #f5f5f5" class="th12">
			<th>编号</th>
			<th>标题</th>
			<th>免费</th>
			<th>单价</th>
			<th>付费总额</th>
			<th>次数</th>
			<th>创建时间</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
		<tbody>
		{{? it.dataList.length <= 0}}
			<tr><td colspan="9">暂时没有数据</td></tr>
		{{??}}
			{{~ it.dataList:value:index }}
 			 <tr>
				<td class="game_match">{{=value.articleNo}}</td>				
				<td class="game_match">{{=value.title}}</td>				
				<td class="game_match">
					{{? value.isFree == 0}}否{{?}}
					{{? value.isFree == 1}}是{{?}}
				</td>
				<td class="game_match">{{=value.price}}</td>
				<td class="game_match">{{=value.amount}}</td>
				<td class="game_match">{{=value.views}}</td>
				<td class="game_match">{{=value.createTime}}</td>
				<td class="game_match">
					{{? value.status == -1}}删除{{?}}
					{{? value.status == 0}}禁用{{?}}
					{{? value.status == 1}}启用{{?}}
					{{? value.status == 2}}待审核{{?}}
					{{? value.status == 3}}审核不通过{{?}}
				</td>
				<td class="game_match">
					<a href="/article/detail/{{=value.id}}" style="color: #0082ff;">详情 
				</td>
			</tr>
			{{~}}
		{{?}}
		</tbody>
	</table>
</script>