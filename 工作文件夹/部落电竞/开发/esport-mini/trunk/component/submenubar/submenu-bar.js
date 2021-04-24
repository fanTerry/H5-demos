// component/submenubar/submenu-bar.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    titleArray: Array,
    selectedTag: Number,
  },

  /**
   * 组件的初始数据
   */
  data: {
  },

  attached() { },
  ready() {
      // console.log("dddddd",this.properties.selectedTag);
  },

  /**
   * 组件的方法列表
   */
  methods: {

      changeTag:function (e) {
          console.log("切换栏目");
          var curIndex = e.currentTarget.dataset.index;
          var arry = this.properties.titleArray;
          console.log(arry[curIndex].contentType)
          this.setData({selectedTag: arry[curIndex].contentType});

          // 所有要带到主页面的数据，都装在eventDetail里面
          var eventDetail = {
              tag:arry[curIndex].contentType,
              name:arry[curIndex].name,
          }
          // 触发事件的选项 bubbles是否冒泡，composed是否可穿越组件边界，capturePhase 是否有捕获阶段
          var eventOption = {
              composed: true,
          }
          this.triggerEvent('myevent', eventDetail, eventOption)
      }
  }
})
