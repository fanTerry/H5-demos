<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>wangEditor demo</title>
</head>
<body>
    <div id="editor1">
    </div>
    <script type="text/javascript" src="/resources/plugins/wangeditor/wangEditor.min.js"></script>
    <script type="text/javascript">
    	
        var E = window.wangEditor
        var editor1 = new E('#editor1')
     	// 将图片大小限制为 10M
        editor1.customConfig.uploadImgMaxSize = 10 * 1024 * 1024
     	// 限制一次最多上传 5 张图片
        editor1.customConfig.uploadImgMaxLength = 5
        editor1.customConfig.uploadImgServer = '/fileupload/uploadImage'
        // editor1.customConfig.showLinkImg = false   // 隐藏“网络图片”tab
        editor1.create()
       	
        editor1.customConfig.uploadImgHooks = {
		    before: function (xhr, editor, files) {
		    	console.log("进入了before");
		        // 图片上传之前触发
		        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件
		        
		        // 如果返回的结果是 {prevent: true, msg: 'xxxx'} 则表示用户放弃上传
		        // return {
		        //     prevent: true,
		        //     msg: '放弃上传'
		        // }
		    },
		    success: function (xhr, editor, result) {
		    	console.log("进入了success");
		        // 图片上传并返回结果，图片插入成功之后触发
		        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
		    },
		    fail: function (xhr, editor, result) {
		    	console.log("进入了fail");
		        // 图片上传并返回结果，但图片插入错误时触发
		        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
		    },
		    error: function (xhr, editor) {
		    	console.log("进入了error");
		        // 图片上传出错时触发
		        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
		    },
		    timeout: function (xhr, editor) {
		        // 图片上传超时时触发
		        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
		    },
		
		    // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
		    // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
		    customInsert: function (insertImg, result, editor) {
		        // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
		        // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果
		
		        // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
		        console.log("进入了customInsert");
		        var url = result.url
		        insertImg(url)
		
		        // result 必须是一个 JSON 格式字符串！！！否则报错
		    }
    	}

        
        
    </script>
</body>
</html>