// component/popup/ppexpert/pp-expert.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
      payResult:Number,
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

      closePayDialog:function () {
          // 所有要带到主页面的数据，都装在eventDetail里面
          var eventDetail = {
              showPayDialog:false,
          }
          // 触发事件的选项 bubbles是否冒泡，composed是否可穿越组件边界，capturePhase 是否有捕获阶段
          var eventOption = {
              composed: true,
          }
          this.triggerEvent('closedialog', eventDetail, eventOption)
      }

  }
})
