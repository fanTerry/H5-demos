<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/x-dot-template" id="dot-incomelist">
	<table class="BMTable">
    	<tr style="background-color: #f5f5f5" class="th12">
			<th>文章编号</th>
			<th>标题</th>
			<th>单价</th>
			<th>付费总额</th>
			<th>分成总额</th>
			<th>次数</th>
			<th>创建时间</th>
		</tr>
		<tbody>
        {{? it.dataList.length <= 0}}
			<tr><td colspan="9">暂时没有数据</td></tr>
		{{??}}
        {{~ it.dataList:value:index }}
 			 <tr>
                 <td class="">{{=value.articleNo}}</td>
                 <td class="">{{=value.title}}</td>
				<td class="">
                    {{=value.price}}
				</td>
				<td class="">{{=value.paySumMoney}}</td>
				<td class="">{{=value.payDividedMoney}}</td>
				<td class="">{{=value.times}}</td>
				<td class="">{{=value.createTime}}</td>
			</tr>
			{{~}}
		{{?}}

        {{? it.dataList.length > 0}}
        <tr>

            <td colspan="3">总计</td>

            <td colspan="1">{{=addSum(it.dataList,0)}}</td>
            <td colspan="1">{{=addSum(it.dataList,1)}}</td>
            <td colspan="1">{{=addSum(it.dataList,2)}}</td>
            <td colspan="1"></td>
        </tr>
        {{?}}
		</tbody>
	</table>

</script>
<script type="text/javascript">
    function addSum(arry,type) {
        var sum = 0
        for (var i = 0; i < arry.length; i++) {
            if (type==0) {
                sum = sum + arry[i].paySumMoney;
            }else if (type==1){
                sum = sum + arry[i].payDividedMoney;
            } else {
                sum = sum + arry[i].times;
            }
        }
        return sum
    }
</script>