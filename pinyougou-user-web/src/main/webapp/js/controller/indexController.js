//首页控制器
app.controller('indexController',function($http,$scope,loginService){
    $scope.showName=function(){
        loginService.showName().success(
            function(response){
                $scope.loginName=response.loginName;
            }
        );
    }
    //查询购物车列表
    $scope.findCartList = function () {
        $http.get("http://localhost:9107/order/findPage.do?page=1&rows=1",{'withCredentials':true}).success(
            function (response) {
                $scope.cartList = response;
                $scope.totalValue = cartService.sum($scope.cartList);//求合计数
            }
        );
    }
});
