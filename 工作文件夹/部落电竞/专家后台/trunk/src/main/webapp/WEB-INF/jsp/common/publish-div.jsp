<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 发布推荐成功 -->
 <div id="dialog-success" class="pub_success" style="display:none">
     <p class="sprite success_close" data-mark="x"></p>
     <p class="sprite success_logo"></p>
     <p class="success_word">恭喜你发布解读成功</p>
     <div class="success_btn">
         <a href="javascript:void(0)" class="success_look">解读查看</a>
         <a href="/publish/${gameCode}" class="go_pub">继续发布</a>        
     </div>
 </div>
<!-- 无logo -->
<div id="dialog-alert" class="pub_success" style="display:none">
    <p class="sprite success_close" data-mark="x"></p>
    <p class="success_word success_word2">至少选择一场比赛</p>
    <a href="javascript:void(0)" class="return_btn" data-mark="x">确定</a>
</div>

<div id="dialog-reload" class="pub_success" style="display:none">
    <p class="sprite success_close" data-mark="x"></p>
    <p class="success_word success_word2">xx</p>
    <a href="javascript:void(0)" class="return_btn" id="jq_reload">确定</a>
</div>

<div id="dialog-tip" class="pub_success"  style="display:none"> <!-- style="width:300px;" -->
    <!-- <p class="sprite success_close" data-mark="x"></p> -->
    <p class="fs20">您确认发布该解读吗？</p>
    <p class="mt10 fz16 red" color="red">解读发布后不可修改，请确保内容正确无误</p>
     <div class="success_btn">
        <a href="javascript:void(0)" class="return_btn mr20" data-mark="x" >取消</a>
        <a href="javascript:void(0)" class="return_btn" id="btnSubmitNew" >确认</a>        
    </div>
</div>

<div id="dialog-error" class="pub_success" style="display:none">
     <p class="sprite success_close" data-mark="x"></p>
     <p class="sprite fail_logo"></p>
     <p class="fail_word">抱歉,发布未成功</p>
     <p class="return_word">请返回重新发布</p>
     <a href="javascript:void(0)" class="return_btn" data-mark="x">返回</a>
 </div>