// component/popup/ppmy/pp-my.js
var api = require('../../../libs/http.js')
var strUtil = require('../../../libs/strUtil');
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    price: Number,
    unit: String
  },

  /**
   * 组件的初始数据
   */
  data: {
    tips: "",
    choosedPayWay: {},
    payWay: [],
    readyFlag: false,
    preventRepeatFlag: true,
    notEnough: false,
    showPop: false,
    buttonTxt: "确认支付",
    buttonStr: "余额不足,去充值",
    showDialog: false
  },
  ready() {
    console.log(this.properties.payPrice, 'payPrice');
  },
  /**
   * 组件的方法列表
   */
  methods: {
	/**
	 * 弹窗获取可用支付方式
	 */
    getPayWay: function() {
      var _self = this;
      api._postAuth('/payment/getPayWayList',{
		needRecPay:false,
	  }).then(res => {
        _self.setData({
          payWay: res.data,
          readyFlag: true
        })
        console.log(this.data.payWay, "返回的可选支付方式")
        res.data.forEach(function(item) {
          if (parseFloat(item.balance) >= _self.properties.price) {
            _self.setData({
              choosedPayWay: item
            })
            _self.judgeIsEnough();
            return;
          } else {
            _self.setData({
              choosedPayWay: res.data[0]
            })
            _self.judgeIsEnough();
          }
        });
      }).catch(e => {
        console.log(e)
      })
	},
	/**
	 * 
	 * 选择支付方式
	 */
    choosePay(e) {
      console.log(e, 'choose');
      var item = e.currentTarget.dataset.item;
      this.setData({
        choosedPayWay: item
      })
      this.judgeIsEnough();
	},
	/**
	 * 判断支付余额情况
	 */
    judgeIsEnough() {
      if (this.data.choosedPayWay.showBalance==0){
        this.setData({
          notEnough: false,
          buttonTxt: "确认支付"
        });
        return;
      }
      if (parseFloat(this.data.choosedPayWay.balance) < parseFloat(this.properties.price)) {
        this.setData({
          notEnough: true,
          buttonTxt: this.data.buttonStr
        });
      } else {
        this.setData({
          notEnough: false,
          buttonTxt: "确认支付"
        });
      }
	},
	/**
	 * 
	 * 暂时弃用
	 */
    confirmPay(e) {
      var choosedValue = e.currentTarget.dataset.selectpay;
      var item = e.currentTarget.dataset.item;
      this.setData({
        choosedValue: choosedValue
      })
      if (choosedValue == 2) {
        if (item.balance * 1 == 0 || item.balance * 1 < item.amount * 1) {
          api._showToast('友宝余额不足', 2);
          return;
        }
      }
      //触发确认支付回调
      this.triggerEvent("confirm", {
        choosedValue: choosedValue
      })
	},
	/**
	 * 支付回调上城父类组件的实际支付方法
	 */
    payMoney() {
      if (this.data.choosedPayWay.payIndex == null) {
        api._showToast("请先选择支付方式", 2);
        return;
      }
      this.judgeIsEnough();
      if (this.data.notEnough) {
        this.toCharge();
        return;
      }
      this.triggerEvent("confirm", {
        choosedValue: this.data.choosedPayWay.payIndex
      })
    },
 
	/**
	 * 余额不足跳转充值或提示
	 */
    toCharge() {
      let chooseIndex = this.data.choosedPayWay.payIndex;
      if (chooseIndex == 2) {
        //window.location.href = this.globalConst.uboxCharge;
        api._showToast("暂不支持友宝余额充值", 2);
      } else if (chooseIndex == null || chooseIndex == 0) {
        wx.navigateTo({
          url: '/modules/mypage/myrecharge/my-recharge?returnUrlEncodeFlag=2'
        })
      }
	},
	show() {
		this.getPayWay();
		this.setData({
		  showDialog: true
		})
	  },
	  close() {
		this.setData({
		  showDialog: false
		})
	  },
  }
})