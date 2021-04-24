import {
    post,
    get
} from '../request/http';

var websock = null;
var global_callback = null;

var heartCheck = { // 心跳对象
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
                'sceneType': 3, // 房间场景
                'execType': 13, // 聊天
                'playload': {
                    'sceneId': 'T_R_' + _self.matchId,
                    'sendMsg': content,
                    'nickName': _self.nickName,
                    'usrId': _self.userId,
                    'icon': _self.icon
                }
            };
            // console.log("聊天室注意开始发送数据");
            toSendSocketMessage(getmsg);
            // this.serverTimeoutObj = setTimeout(() => {
            //   wx.closeSocket();
            // }, this.timeout);
        }, this.timeout);
    }
};

// 初始化weosocket
function initWebSocket(_self) {
    // ws地址
    websock = new WebSocket(_self.socketUrl);
    websock.onmessage = function (e) {
        websocketonmessage(_self, e);
    };
    websock.onclose = function (e) {
        // console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean)
        console.log(e)
        console.log('websock closed');
        // websocketclose(e);
    };
    websock.onopen = function () {
        websocketOpen(_self);
    };

    // 连接发生错误的回调方法
    websock.onerror = function () {

        // console.log("WebSocket连接发生错误");
    };
}

// 实际调用的方法
function sendSock(agentData, callback) {
    global_callback = callback;
    if (websock.readyState === websock.OPEN) {
        // 若是ws开启状态
        toSendSocketMessage(agentData);
    } else if (websock.readyState === websock.CONNECTING) {
        // 若是 正在开启状态，则等待1s后重新调用
        setTimeout(function () {
            sendSock(agentData, callback);
        }, 1000);
    } else {
        // 若未开启 ，则等待1s后重新调用
        setTimeout(function () {
            sendSock(agentData, callback);
        }, 1000);
    }
}

// 数据接收
function websocketonmessage(_self, res) {
    // global_calllback(JSON.parse(e.data));
    var data = JSON.parse(res.data);
    // console.log(_self.userId, '数据接收,返回数据12-3');
    if (data.execType == 12) {
        if (data.ret == 'fail') { // 返回失败,需要重连,所有的场景都需要这个,初次发消息
            var initMsg = {
                'execType': 12,
                'playload': {
                    'usrId': _self.userId
                }
            };
            toSendSocketMessage(initMsg);
        } else { // 初次返回成功,则发送一条
            //console.log(_self.initScene, '初次返回成功,则发送一条');
            console.log(JSON.stringify(_self.initScene), '初次返回成功,则发送一条');
            toSendSocketMessage(_self.initScene);
            setTimeout(() => {
                _self.ready = true;
            }, 300);

        }
    } else {
        if (data.execType == 13) { // 聊天
            //console.log(data, '聊天室服务器返回的数据12-3');
            _self.chatList = _self.chatList.concat(data.playload);
        }
    }
}

// 数据发送
function toSendSocketMessage(agentData) {
    // console.log(agentData, '开始发送数据12-3');
    // console.log(websock);
    websock.send(JSON.stringify(agentData));
}

// 关闭
function websocketclose(e) {
    // console.log("connection closed (" + e + ")");
    websock.close();
}

function websocketOpen(_self) {
    // console.log(_self.userId, '_self.userId发送数据');
    if (!_self.userId || _self.userId == null || _self.userId == '' || _self.userId == 'undefined') {
        console.log('_self.userId为空,不发送数据');
        // alert(JSON.parse(window.localStorage.getItem('user')));
        // alert(window.localStorage.user);
        return;
    }
    var initMsg = {
        'execType': 12,
        'playload': {
            'usrId': _self.userId
        }
    };
    // 发送指定消息,所有的场景都需要这个,初次发消息
    toSendSocketMessage(initMsg);
    // console.log("连接成功");
}

function getSceneMsg(sceneType, cmdType, execType, sceneId, matchId, usrId, icon) {
    var initScene = {
        'sceneType': sceneType,
        'cmdType': cmdType,
        'execType': execType,
        'playload': {
            'sceneId': sceneId + matchId,
            'usrId': usrId,
            'matchId': matchId + '',
            'icon': icon
        }
    };
    return initScene;
}

// initWebSocket();

export {
    initWebSocket,
    getSceneMsg,
    sendSock,
    heartCheck,
    toSendSocketMessage,
    websocketclose
};
