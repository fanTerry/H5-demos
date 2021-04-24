// modules/store/list/goodsList.js
const app = getApp();
var api = require('../../../libs/http.js')
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		myClass: 'goodsList_Page',
		contentType: 0,
		goodsQueryType: Number,
		shopGoodList: [],
		categoryList: [],
		pageNo: 0,
		pageSize: 10,
		haseNext: true,
		pageType: 2,
		selectedTag: 0,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		var contentType = options.contentType;
		var name = options.name
		console.log(name);
		var goodsQueryType = options.goodsQueryType
		if (goodsQueryType) {
			this.setData({
				goodsQueryType: goodsQueryType,
				pageType: 3,
			})
		} else {
			this.setData({
				selectedTag: contentType,
				contentType: contentType,
			})

		}
		if(goodsQueryType==3){
			this.setData({
				pageSize:50,
			})
		}
		console.log("contentType", contentType);
		console.log("name", name);
		console.log("goodsQueryType", goodsQueryType);

		wx.setNavigationBarTitle({
			title: name
		})
		if(contentType){
			this.loadIndexData();
		}

		this.loadShopGoods()



	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function () {

	},

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () {

	},

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () {

	},

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: function () {

	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {
		this.loadShopGoods()
	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},

	loadShopGoods: function () {
		if (!this.data.haseNext) {
			return;
		}
		var that = this;
		var param = {}
		param.pageNo = that.data.pageNo + 1;
		param.pageSize = that.data.pageSize;
		param.contentType = that.data.contentType
		if (this.data.pageType == 3) {
			param.goodsQueryType = that.data.goodsQueryType
		}

		var url = "/shop/getGooods";
		api._post(url, param).then(res => {
			if (res.code == "200") {
				if (res.data.shopGoodList.length > 0) {
					if (res.data.shopGoodList.length < 10) {
						//没有下一页了
						that.setData({
							haseNext: false,
						});
					}
					var shopGoodList = that.data.shopGoodList.concat(res.data.shopGoodList)
					that.setData({
						shopGoodList: shopGoodList,
						pageNo: param.pageNo,
						pageSize: param.pageSize,
					});
				} else {
					that.setData({
						haseNext: false,
					});
				}

			}
		}).catch(e => { })
	},

	/**
     * 初始化商品栏目
     */
	loadIndexData: function () {
		var that = this
		var url = "/shop/indexData";

		api._post(url, {
			pageNo: 1,
			pageSize: 10,
			clientType: 5 //微信小程序
		}).then(res => {
			if (res.code == "200") {
				var temarr = res.data.categoryList;
				var arr = [];
				arr.push({
					contentType: 0,
					name: "全部"
				});
				temarr.forEach(element => {
					arr.push({
						contentType: element.id,
						name: element.name
					})
				});
				that.setData({
					categoryList: arr
				});
			} else {
				console.log(res.message);
			}
		}).catch(e => { })

	},

	/**
     * 切换栏目重新渲染数据
     */
	onMyEvent: function (e) {
		if (this.data.selectedTag == e.detail.tag) {
			return;
		}

		var contentType = e.detail.tag
		var name = ""
		this.data.categoryList.forEach(element => {
			if (element.contentType === contentType) {
				name = element.name;
			}
		});
		wx.setNavigationBarTitle({
			title: name
		})

		this.setData({
			selectedTag: contentType,
			contentType: contentType,
			pageNo: 0,
			shopGoodList: [],
			haseNext: true,

		});
		this.loadShopGoods();

	},
})