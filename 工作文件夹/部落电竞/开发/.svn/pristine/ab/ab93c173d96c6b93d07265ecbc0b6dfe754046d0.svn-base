// component/popup/ppmy/pp-my.js
var api = require('../../../libs/http.js')
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    items: Array,
    payPrice:Number
  },

  /**
   * 组件的初始数据
   */
  data: {
    showDialog: false, 
    choosedValue: 1,
  },
  ready(){
    console.log(this.properties.payPrice,'payPrice');
  },
  /**
   * 组件的方法列表
   */
  methods: {
    show() {
      this.setData({
        showDialog: true
      })
    },
    hide() {
      this.setData({
        showDialog: false
      })
    },
    radioChange(e) {
      this.setData({
        choosedValue: e.detail.value
      })
      console.log(this.data.choosedValue, '选择的支付方式')
    },
    confirmPay(e) {
      var choosedValue = e.currentTarget.dataset.selectpay;
      var item = e.currentTarget.dataset.item;
      this.setData({
        choosedValue: choosedValue
      })
      if(choosedValue==2){
        if (item.balance * 1 == 0 || item.balance * 1< item.amount*1){
          api._showToast('友宝余额不足', 2);
          return;
        }
      }
      //触发确认支付回调
      this.triggerEvent("confirm", { choosedValue: choosedValue })
    }
  }
})
