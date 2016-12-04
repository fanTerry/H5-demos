angular.module('myApp',['ionic'])

.config(['$stateProvider','$urlRouterProvider','$ionicConfigProvider',function($stateProvider,$urlRouterProvider,$ionicConfigProvider){
		$ionicConfigProvider.platform.android.tabs.position('bottom');
		//让页面在安卓机里面始终显示在底部 
		
		
		$stateProvider
		.state('tab',{
			url:'/tab',
			templateUrl:'template/muban.html',
			abstract:true
		})
		
		.state('tab.home',{
			url:'/home',
			views:{
				"article-view":{
					templateUrl:'template/home.html',
					controller:'homeCtrl'
				}	
			}			
		})	
		
		.state('tab.news',{
			url:'/news',
			views:{
				'news-view':{
					templateUrl:'template/news.html',
					controller:'newsCtrl'
				}
			}
		})
		
		.state('tab.tiezi',{
			url:"/tiezi/:id",
			views:{
				'news-view':{
					templateUrl:'template/tiezi.html',
					controller:'tieziCtrl'
				}
			}
		})
		
		.state('tab.detail',{
			url:"/detail/:id",
			views:{
				'news-view':{
					templateUrl:'template/detail.html',
					controller:'detailCtrl'
				}
			}
		})
		
		$urlRouterProvider.otherwise('tab/home');
}])

.controller('homeCtrl',['$scope','$ionicSlideBoxDelegate',function($scope,$ionicSlideBoxDelegate){
	$scope.pageClick = function(index){
		$ionicSlideBoxDelegate.slide(index);
	}
}])

.controller('newsCtrl',['$scope','$http',function($scope,$http){
	$http.get('http://www.phonegap100.com/appapi.php?a=getThreadCate').success(function(res){
		console.log(res.result)
		$scope.newsList = res.result;
	})
}])

.controller('tieziCtrl',['$scope','$http','$stateParams',function($scope,$http,$stateParams){
	var fid = $stateParams.id;
	console.log(fid)
	$scope.getData = function(fid,page){
		$http.get('http://www.phonegap100.com/appapi.php?a=getThreadList&fid='+fid+'&page='+page+'').success(function(res){
			console.log(res.result);
			$scope.tieziList = res.result;
		})
	}
	$scope.getData(fid,1);
}])

.controller('detailCtrl',['$scope','$http','$stateParams','$ionicPopup',function($scope,$http,$stateParams,$ionicPopup){
	var tid = $stateParams.id;
	console.log(tid);
	$scope.getData = function(tid){
		$http.get('http://www.phonegap100.com/appapi.php?a=getThreadContent&tid='+tid+'').success(function(res){
			console.log(res);
			$scope.detailList = res.result;
		})
	}
	$scope.getData(tid);
	
	$scope.user = {};
	$scope.user.choice = 'normal';
	
	$scope.setFont = function(){
		var pop = $ionicPopup.show({
			title: '设置文字大小',
			templateUrl: 'template/font.html',
			scope: $scope,
			buttons: [{ 
			    text: '取消',
			    type: 'button-default',
			    onTap: function(e) {
			      //点击取消触发的事件
			      pop.close();
			    }
		  	},
		  	{
			    text: '确定',
			    type: 'button-positive',
			    onTap: function(e) {
		      		//return $scope.data.response;
		      		console.log($scope.user.choice);
		      		switch($scope.user.choice){
				      	case 'small':
				      		$scope.isSmall = true;
				      		$scope.isBig = false;
				      		$scope.isNormal = false;
				      		break;
		
				      	case 'normal':
				      		$scope.isSmall = false;
				      		$scope.isBig = false;
				      		$scope.isNormal = true;
				     		break;
		
				     	case 'big':
				     		$scope.isSmall = false;
				      		$scope.isBig = true;
				      		$scope.isNormal = false;
				     		break;
	     			}
				}
			}]
		})
	}
}])