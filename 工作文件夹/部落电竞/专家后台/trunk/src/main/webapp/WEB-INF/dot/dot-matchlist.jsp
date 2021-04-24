<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/x-dot-template" id="dot-matchlist">
	<table class="BMTable">
    	<tr style="background-color: #f5f5f5" class="th12">
			<th></th>
			<th>序号</th>
			<th>联赛名称</th>
			<th>系列赛名称</th>
			<th>锦标赛名称</th>
			<th>主队名称</th>
			<th>主队logo</th>
			<th>比分</th>
			<th>客队logo</th>
			<th>客队名称</th>
			<th>开始时间</th>
			<th>比赛状态</th>
		</tr>
		<tbody>
		{{? it.length <= 0}}
			<tr><td colspan="7">暂时没有数据</td></tr>
		{{??}}
			{{~ it:value:index }}
 			 <tr>
				<td class="game_number"><input name="matchId" type="checkbox" value="{{=value.matchId}}"/></td>
				<td class="game_number">{{=index+1}}</td>
				<td class="game_match">{{=value.leagueName}}</td>
				<td class="game_match">{{=value.serieName}}</td>
				<td class="game_match">{{=value.tournamentName}}</td>
				<td class="game_host">{{=value.homeTeamName}}</td>
				<td class="game_host"><image src="{{=value.homeTeamLogo}}" width="30px" height="30px"/></td>
				<td class="game_score">VS</td>
				<td class="game_guest"><image src="{{=value.awayTeamLogo}}" width="30px" height="30px"/></td>
				<td class="game_guest">{{=value.awayTeamName}}</td>
				<td class="game_start">{{=value.beginAt}}</td>
				<td class="game_number">
					{{? value.status == 0}}未开赛{{?}}
					{{? value.status == 1}}比赛中{{?}}
					{{? value.status == 2}}已完场{{?}}
				</td>
			</tr>
			{{~}}
		{{?}}
		</tbody>
	</table>
</script>