//controller
app.controller('brandController', function($scope, $controller,brandService) {
	$controller('baseController',{$scope:$scope});//继承	，通过传递scope来实现伪继承
	
		//分页
		$scope.findPage = function(page, rows) {
			brandService.findPage(page, rows)
					.success(function(response) {
						$scope.list = response.rows; //显示当前页数据
						$scope.paginationConf.totalItems = response.total;//更新总记录数
					});
		}
		//新增
		$scope.save = function() {
			var object = null;
			if ($scope.entity.id != null) {//如果有ID
				object=brandService.update($scope.entity);
			}else{
				object=brandService.add($scope.entity);
			}
			object.success(function(response) {
						if (response.success) {
							//重新查询 
							$scope.reloadList();//重新加载
						} else {
							alert(response.message);
						}
					});
		}
		//查询实体 
		$scope.findOne = function(id) {
			brandService.findOne(id).success(
					function(response) {
						$scope.entity = response;
					});
		}

		$scope.selectIds = [];//选中的ID集合 
		//更新复选
		$scope.updateSelection = function($event, id) {
			if ($event.target.checked) {//如果是被选中,则增加到数组
				$scope.selectIds.push(id);
			} else {
				var idx = $scope.selectIds.indexOf(id);
				$scope.selectIds.splice(idx, 1);//删除 
			}
		}
		//批量删除 
		$scope.dele = function() {
			//获取选中的复选框			
			brandService.dele($scope.selectIds).success(
					function(response) {
						if (response.success) {
							$scope.reloadList();//刷新列表
						} else {
							alert(response.message);
						}
					});
		}

		$scope.searchEntity = {};//定义搜索对象 			
		//条件查询 
		$scope.search = function(page, rows) {
			brandService.search(page, rows,$scope.searchEntity).success(function(response) {
				$scope.paginationConf.totalItems = response.total;//总记录数 
				$scope.list = response.rows;//给列表变量赋值 
			});
		}
	});