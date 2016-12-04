angular.module('myApp', ['ngRoute'])

.config(['$routeProvider',function($routeProvider){
	$routeProvider
	.when('/shouye',{
		templateUrl:'template/shouye.html',
		controller:'shouyeCtrl'
	})
	.when('/dingdan',{
		templateUrl:'template/dingdan.html',
		controller:'dingdanCtrl'
	})

	
	
	
	
	
	.otherwise('/shouye')
}])

.controller('indexCtrl',['$scope',function($scope){
	$scope.change = function(){
		window.location.href = '#/dingdan';
	}
}])

.controller('shouyeCtrl', ['$scope','Dinner','$rootScope', function($scope,Dinner,$rootScope){
	$scope.totalPrice = 0;
	$rootScope.totalNum = 0;
	
	Dinner.getList();
	
	function init(){
		if(localStorage.dinner){
			var arr = [];
			arr = JSON.parse(localStorage.dinner);
			for(var i=0;i<arr.length;i++){
				for(var j=0;j<$scope.dinnerList.length;j++){
					if($scope.dinnerList[j].id == arr[i].id){
						$scope.dinnerList[j].num = arr[i].num;
					}
				}
			}
		}
		if(localStorage.totalNum){
			$rootScope.totalNum = JSON.parse(localStorage.totalNum)
		}
	}
	
	$scope.$on('dinnerList', function(event,data){
		//获取整个的数据 data
		$scope.allData = data;
		console.log(data)
		//默认显示主菜的数据
		//dinnerList 右侧列表的变量
		$scope.dinnerList = data[0].items;  //菜品数据
		$scope.dinnerTitle = data[0].name;  //右侧的标题
		
		angular.forEach($scope.dinnerList,function(val,key){
			val.num = 0;
		})
		init();
	});
	
	//菜单点击事件
	$scope.changeNav = function(index){
		//index 当前菜单的索引

		//首先把所有菜单的数据里面的ifChose改为false   
		angular.forEach($scope.allData,function(val,key){ //第一个参数是对象，第二个参数是索引
//			console.log(val)
			val.ifChose = false;
		});
		//把当前点击的菜单数据ifChose改成true
		$scope.allData[index].ifChose = true;

		//把右侧的菜品数据，切换成当前点击的导航下的菜品数据
		$scope.dinnerList = $scope.allData[index].items;
		$scope.dinnerTitle = $scope.allData[index].name;
		
		angular.forEach($scope.dinnerList,function(val,key){
			val.num = 0;
		})
		init();
	}
	
	$scope.add = function(index){
		if(localStorage.totalNum){
			$rootScope.totalNum = JSON.parse(localStorage.totalNum);
		}
		console.log(index)
		$scope.dinnerList[index].num++;
		
		$scope.totalPrice += $scope.dinnerList[index].price;
		$rootScope.totalNum +=1;
		
		var arr=[];
		if(localStorage.dinner){ //判断一下localStorage里面有没有数据，如果没有数据而直接解析的话会报错
			arr = JSON.parse(localStorage.dinner);
		};
//		console.log(arr);
		var flag =true;
		for(var i=0;i<arr.length;i++){
			if(arr[i].id == $scope.dinnerList[index].id){
				++arr[i].num;
				flag = false;
			}
		}
		if(flag){
			arr.push($scope.dinnerList[index]);
		};
		console.log(arr);
		localStorage.dinner = JSON.stringify(arr);
		localStorage.totalNum = JSON.stringify($rootScope.totalNum);
		console.log($rootScope.totalNum)
	}
	
	$scope.reduce = function(index){
		if(localStorage.totalNum){
			$rootScope.totalNum = JSON.parse(localStorage.totalNum);
		}
		console.log(index);
		$scope.dinnerList[index].num--;
		
		$scope.totalPrice -= $scope.dinnerList[index].price;
		$rootScope.totalNum -= 1;
		
		arr = JSON.parse(localStorage.dinner);
		for(var i =0;i<arr.length;i++){
			if(arr[i].id == $scope.dinnerList[index].id){
				--arr[i].num;
				if(arr[i].num == 0){
					arr.splice(i,1);
				}
			}
		}
		localStorage.dinner = JSON.stringify(arr);
		localStorage.totalNum =JSON.stringify($rootScope.totalNum);
	}
}])


.controller('dingdanCtrl',function($scope,$rootScope){
	$scope.back =function(){
		window.location.href = '#/shouye';
		$('.veg2').html('已点菜单').css({'background':'#6b615e','color':'#999999'});
	}
	if(localStorage.dinner){
		$scope.productList = JSON.parse(localStorage.dinner);
	}
	$scope.add = function(index){
		if(localStorage.totalNum){
			$rootScope.totalNum = JSON.parse(localStorage.totalNum);
		}
//		$scope.totalPrice += $scope.dinnerList[index].price;
		$rootScope.totalNum +=1;
		$scope.productList[index].num++;

		localStorage.dinner = JSON.stringify($scope.productList);
		localStorage.totalNum = JSON.stringify($rootScope.totalNum);
		console.log($rootScope.totalNum)
	}
	
	$scope.reduce = function(index){
		if(localStorage.totalNum){
			$rootScope.totalNum = JSON.parse(localStorage.totalNum);
		}
		console.log(index);
		$scope.productList[index].num--;
		
//		$scope.totalPrice -= $scope.dinnerList[index].price;
		$rootScope.totalNum -= 1;
	
		if($scope.productList[index].num == 0){
			$scope.productList.splice(index,1);
		}
				
		localStorage.dinner = JSON.stringify($scope.productList);
		localStorage.totalNum =JSON.stringify($rootScope.totalNum);
	}
	
})

.service('Dinner', ['$http','$rootScope', function($http,$rootScope){

	return {
		"getList" : function(){
			$http.get('groups.json', {})
			.then(function(response){
				console.log(response.data);
				$rootScope.$broadcast('dinnerList', response.data)
			},function(error){
				console.log(error)
			})
		}
	}
}])
