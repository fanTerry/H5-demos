<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	在head 部分引入 样式文件 <link rel="stylesheet" href="/resources/plugins/kindeditor/4.1.11/themes/default/default.css"/>
	母文件 含有<textarea name="content"/>
 -->
<script src="/resources/plugins/kindeditor/4.1.11/kindeditor-all.js" charset="utf-8"></script>
<script type="text/javascript">
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="content"]', {
		width:'100%',
		height:400,
		items: [
		        'source', '|', 'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
		        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 
		        'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
		        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
		        'table', 'hr', 'pagebreak',
		        'anchor', 'link', 'unlink'
		],
		resizeType : 1,
		allowImageRemote: false,	//禁止网络图片
		allowPreviewEmoticons : false,
		allowImageUpload : true,
		formatUploadUrl : false,
		uploadJson : "/image/upload",
		afterBlur : function(){
            this.sync();
		}
	});
});
</script>