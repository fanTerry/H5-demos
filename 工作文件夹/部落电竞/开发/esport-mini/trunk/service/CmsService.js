var api = require('../libs/http.js')

function loadCmsByPageApi(that){
    console.log("当前pageNo:"+that.data.pageNo);
    var pageNo = that.data.pageNo+1;
    console.log("下一页pageNo:"+pageNo);
    var pageSize = that.data.pageSize;
    var url = "/newlist";

     api._post(url,{
         pageNo:pageNo,
         pageSize:pageSize,
         contentType:that.data.selectedTag,  //默认推荐
     }).then(res => {
         if(res.code=="200"){
            var newdatas;
            newdatas =res.data;

             if (newdatas.length>0) {
                newdatas =  that.data.newsList.concat(newdatas);
                 console.log("设置页码",pageNo);
                that.setData({
                    newsList:newdatas,
                    pageNo :  pageNo,
                    noDataFlag:false,
                });
            }else if (pageNo==1) {
                // api._showToast("暂无数据",1);
             }


             if (that.data.newsList.length==0){
                 that.setData({
                     noDataFlag:true,
                 });
             }

             if (newdatas.length<pageSize) {
                 that.setData({
                     requestMore:true,
                 })
             }
        }
    }).catch(e => { })


}



module.exports = {
    loadCmsByPageApi: loadCmsByPageApi,

}
