var api = require('../../libs/http.js')
const app = getApp()
Component({

  properties: {

  },

  /**组件的初始数据*/
  data: {
    contentValue: '',
    focus: false,
    placeholder: '我来发表一点意见吧',
    showLineInput: false //显示单行文本框
  },

  /**组件的方法列表*/
  methods: {

    /**自带发送按钮 */
    _sendChat: function (e) {
      var content = e.detail.value;
      this.setData({
        focus: false
      })
      console.log(content, '_sendChat');
      if (!content) {
        api._showToast('请输入要发送的内容', 2);
        return;
      }
      this.triggerEvent('myevent', { content: content });
    },

    /**绑定输入内容 */
    _contentInput: function (e) {
      this.setData({
        contentValue: e.detail.value
      })
    },

    /**绑定外部发送按钮 */
    _sendButtonChat: function () {
      var _self = this;
      var curContentValue = _self.data.contentValue;
      //api._showToast(curContentValue + '点了发送按钮', 2)
      console.log(curContentValue, '_sendButtonChat');
      if (!curContentValue) {
        api._showToast('请输入要发送的内容', 2);
        return;
      }
      if (curContentValue.length > 50) {
        api._showToast('聊天内容不能超过50个字', 2);
        return;
      }
      //处理逻辑和_sendChat一样
      _self.triggerEvent('myevent', { content: curContentValue });
     
      setTimeout(function () {
        _self.setData({ contentValue: '' });
      }, 300);
    },

    /**刷新当前组件数据 */
    reflushData(content) {
      console.log('清空数据');
      this.setData({
        contentValue: '',
        focus: false,
        showLineInput: true
      })
    },

    bindDTextAreaFocus: function (e) {
      this.setData({
        focus: true,
        showLineInput: false
      });
      this.triggerEvent('scorllto');
    },

    bindDTextAreaBlur: function (e) {
      console.log(e);
      var curContentValue = e.detail.value;
      console.log(curContentValue, 'blur');
      if (curContentValue == '') {
        this.setData({
          showLineInput: true
        });
      }
      if (curContentValue.length == 50) {
        api._showToast('聊天内容不能超过50个字', 2);
        return;
      }
      this.setData({
        focus: false,
        contentValue: curContentValue
      });
    }
  }
})
