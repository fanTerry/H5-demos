var api = require('../libs/http.js')
function getCommentPage(_self) {
    var pageNo = _self.data.pageNo;
    var pageSize = _self.data.pageSize;
    if (pageNo > _self.data.totalPages) {
        console.log("已经最后一页了,pageNo="+pageNo+",pageSize="+pageSize);
        return;
    }
    api._postAuth("/cmsComment/list", {
        pageNo: pageNo,
        pageSize: pageSize,
        contentId: _self.data.id,  //当前资讯id
        noShowLoading:true
    }).then(res => {
        console.log(res, '评论列表');
        if (res.code == "200") {
            var resData;
            resData = res.data.commentList;
            if (resData.length > 0) {
                resData = _self.data.commentList.concat(resData);
                _self.setData({
                    commentList: resData,
                    pageNo: pageNo + 1,
                    totalPages: res.data.totalPages
                });
            }
        }
    }).catch(e => { })
}

module.exports = {
    getCommentPage: getCommentPage
}
