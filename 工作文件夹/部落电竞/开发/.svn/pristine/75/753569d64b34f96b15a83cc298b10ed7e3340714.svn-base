// component/header/navlist/nav_list.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
		dataList: Array,
		myClass:String,
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
    changeTab(tabId, e) {
      console.log("切换到tabid=", tabId);
      if (tabId == this.selectedTab) {
        return;
      }
      this.selectedTab = tabId;
      this.$emit("changeTab", tabId);
      this.scrollTarget(e.currentTarget.previousElementSibling);
    },

    scrollTarget(target) {
      if (!target) {
        return;
      }
      var thisLeft = target.offsetLeft - 10;
      $(".list").animate({
        scrollLeft: thisLeft
      }, 500);
    }
  }
})