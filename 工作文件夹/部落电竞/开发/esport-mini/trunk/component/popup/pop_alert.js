// component/popup/pop_alert.js
Component({
	/**
	 * 组件的属性列表
	 */
	properties: {
		showPop: Boolean,
		content: String,
	  type: String,
	  btnTxt1: String,
    btnTxt2: String,
	},

	/**
	 * 组件的初始数据
	 */
	data: {
	},

	/**
	 * 组件的方法列表
	 */
	methods: {
    cancel: function (e) {
      this.triggerEvent('mycancel')
    },
    confirm: function (e) {
      this.triggerEvent('myconfirm')
    },
	}
})
