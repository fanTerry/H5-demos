const app = getApp()
var initMsg = {
    "execType": 12, "playload": { "usrId": app.getGlobalUserInfo().usrId + '' }
};
var heartCheck = { //心跳对象
    timeout: 100,
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function (content, _self) {
        this.timeoutObj = setTimeout(() => {
            var getmsg = {
                "sceneType": 3, //房间场景
                "execType": 13, //聊天
                "playload": { "sceneId": 'R_' + _self.data.matchId, "sendMsg": content, "nickName": app.getGlobalUserInfo().nickName, "usrId": app.getGlobalUserInfo().usrId + '' }
            }
            console.log("聊天室注意开始发送数据");
            toSendSocketMessage(getmsg);
            // this.serverTimeoutObj = setTimeout(() => {
            //   wx.closeSocket();
            // }, this.timeout);
        }, this.timeout);
    }
};

/**连接socket */
function linkSocket(_self) {
    wx.connectSocket({
        url: _self.data.socketUrl,
        success() {
            console.log(_self.data.socketUrl, '聊天室连接成功');
            initEventHandle(_self);
        }
    })
}

/**处理初始化 */
function initEventHandle(_self) {
    wx.onSocketMessage((res) => {
        var data = JSON.parse(res.data);
        if (data.execType == 12) {
            if (data.ret == 'fail') {//返回失败,需要重连
                toSendSocketMessage(initMsg);
            } else {//初次返回成功,则发送一条关联当前场景的信息
                var initScene = {
                    "sceneType": 3, "cmdType": 21, "execType": 10, //用户与与房间进行关联
                    "playload": {
                        "sceneId": 'R_' + _self.data.matchId, "usrId": app.getGlobalUserInfo().usrId + '', "matchId": _self.data.matchId + ''
                    }
                }
                toSendSocketMessage(initScene);
            }
        } else {
            console.log(data, '聊天室服务器返回的数据');
            _self.setData({
                chatList: _self.data.chatList.concat(data.playload)
            });
        }
    })
    wx.onSocketOpen(() => {
        console.log('聊天室WebSocket连接打开')
        //发送指定消息
        toSendSocketMessage(initMsg);
    })
    wx.onSocketError((res) => {
        console.log('聊天室WebSocket连接打开失败')
        reconnect(_self);
    })
    wx.onSocketClose((res) => {
        console.log('聊天室WebSocket 已关闭！')
    })
}

/**重新连接 */
function reconnect(_self) {
    if (_self.data.lockReconnect) return;
    _self.data.lockReconnect = true;
    clearTimeout(_self.data.timer)
    console.log(_self.data.limit,'重连次数');
    if (_self.data.limit < 12) {//不给服务器太大的压力,这里设置的是5秒重试一次,最多请求12次
        _self.data.timer = setTimeout(() => {
            linkSocket(_self);
            _self.data.lockReconnect = false;
        }, 5000);
        _self.setData({
            limit: _self.data.limit + 1
        })
    }else{
        console.log('超过次数');
    }
}

/**发送消息,msg:json格式,统一转string */
function toSendSocketMessage(msg) {
    wx.sendSocketMessage({
        data: JSON.stringify(msg),
        success() {
            console.log("聊天室发送成功,内容:" + JSON.stringify(msg));
        }
    });
}

module.exports = {
    heartCheck: heartCheck,
    linkSocket: linkSocket,
    initEventHandle: initEventHandle,
    reconnect: reconnect,
    toSendSocketMessage: toSendSocketMessage
}