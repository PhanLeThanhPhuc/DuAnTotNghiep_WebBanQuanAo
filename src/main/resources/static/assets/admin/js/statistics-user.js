app.controller("user-statistic-ctrl", function($scope, $filter, $http) {

    $scope.listUser = [];
    $scope.listTotal = [];
    $scope.listData = [];

    $scope.initialize = async function() {
        await $http.get("/rest/orderPhoneAndDate").then(resp => {
            if(resp.status === 200){
                // console.log("listData", resp.data);
                $scope.listData = resp.data.sort((a, b) => b.total - a.total);
                for (let i = 0; i < $scope.pager.listData.length; i++) {
                    $scope.listUser.push($scope.pager.listData[i].phone);
                    $scope.listTotal.push($scope.pager.listData[i].total);
                }
            }
            // console.log("listData", $scope.pager.listData);
        });
        $scope.chart();
    }

    $scope.initialize();

    $scope.chart = () =>{
        const ctx = document.getElementById('myChart');


        $scope.chartUser = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: $scope.listUser,
                datasets: [{
                    label: 'My First Dataset',
                    data: $scope.listTotal,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(255, 159, 64, 0.2)',
                        'rgba(255, 205, 86, 0.2)'
                    ],
                    borderColor: [
                        'rgb(255, 99, 132)',
                        'rgb(255, 159, 64)',
                        'rgb(255, 205, 86)'
                    ],
                    borderWidth: 1
                }]
            }
        });

    }

    $scope.buttonFilterWithDate = async () => {
        $scope.listUser = [];
        $scope.listTotal = [];
        await $http.get("/rest/order-total-user").then(resp => {
            if (resp.status === 200) {
                $scope.listDataTotalWithUserOrder = resp.data.sort((a, b) => b.total - a.total);
            }
        });


        var startDateFormat = $scope.startDate;
        var endDateFormat = $scope.endDate;

        console.log("startDateFormat", startDateFormat);
        console.log("endDateFormat", endDateFormat);
        // console.log();

        $scope.filteredOrders = $scope.listDataTotalWithUserOrder.filter(o => {
            const formattedOrderDate = $scope.formatDate(o.date);
            return formattedOrderDate >= $scope.formatDate(startDateFormat) && formattedOrderDate <= $scope.formatDate(endDateFormat);
        });

        console.log("dd",$scope.filteredOrders);

        var map = processData($scope.filteredOrders);

        console.log("map", map)

        let count = 0;
        const limit = 10;

        for (let [key, value] of map) {
            $scope.listUser.push(key);
            $scope.listTotal.push(value);
            count++;
            if (count >= limit) {
                break;
            }
        }

        $scope.chart();
    }

    // filterDateInMonth = (arr) =>{
    //     var today = new Date();
    //     var currentMonth = today.getMonth() + 1;
    //     var currentYear = today.getFullYear();
    //     var filteredData = arr.filter(function(item) {
    //         var itemDate = new Date(item.date);
    //         return itemDate.getMonth() + 1 === currentMonth && itemDate.getFullYear() === currentYear;
    //     });
    //     return filteredData.sort((a, b) => b.total - a.total);
    // }

    $scope.formatDate = function(date) {
        return $filter('date')(date, 'dd/MM/yyyy');
    };

    /// xu li du lieu trung lap
    processData = (arr) => {
        // $scope.listTotalDate =arr;
        var map = new Map();
        for (let i = 0; i <arr.length; i++) {
            const phone = arr[i].phone;
            if (map.has(phone)) {
                let value = map.get(phone);
                value += arr[i].total;
                map.set(phone, value);
            } else {
                map.set(phone, arr[i].total);
            }
        }

        return map;
    }


    $scope.pager = {
        page: 0,
        size: 5,
        get listData(){
            if(this.page < 0){
                this.last();
            }
            if(this.page >= this.count){
                this.first();
            }
            var start = this.page*this.size;
            return $scope.listData.slice(start, start + this.size)
        },
        get count(){
            return Math.ceil(1.0 * $scope.listData.length / this.size);
        },
        first(){
            this.page = 0;
            // changeChartData();
            // clearChart();
            // $scope.listUser = [];
            // $scope.listTotal = [];
            // var map = processData($scope.pager.listData);
            // for (let [key, value] of map) {
            //     $scope.listUser.push(key);
            //     $scope.listTotal.push(value);
            // }
            // $scope.chart();
        },
        last(){
            this.page = this.count - 1;
            // changeChartData();
            clearChart();
        },
        next(){
            this.page++;
            clearChart();

        },
        prev(){
            this.page--;
            clearChart();
            // changeChartData();
        }
    }

    clearChart = () =>{
        $scope.listUser = [];
        $scope.listTotal = [];
        var map = processData($scope.pager.listData);
        for (let [key, value] of map) {
            $scope.listUser.push(key);
            $scope.listTotal.push(value);
        }
        // console.log($scope.chartUser);
        if($scope.chartUser){
            $scope.chartUser.destroy();
        }
        $scope.chart();
    }

    $scope.firstChart = () =>{
        clearChart();
    }


});