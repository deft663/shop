//服务层
app.service('loginService',function($http){
    //读取列表数据绑定到表单中
    this.showName=function(){

        return $http.get('http://localhost:9106/login/name.do',{'withCredentials':true});
    }
});
