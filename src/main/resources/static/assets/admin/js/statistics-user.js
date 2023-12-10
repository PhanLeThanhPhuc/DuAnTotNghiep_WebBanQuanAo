app.controller("user-statistic-ctrl", function($scope, $filter, $http,$location) {

    $scope.listUser = [];
    $scope.listTotal = [];
    $scope.listData = [];

    $scope.initialize = async function() {
        await $http.get("/rest/phone-total").then(resp => {
            if(resp.status === 200){
                console.log("listData", resp.data);
                $scope.listData = resp.data.sort((a, b) => b.total - a.total);
                for (let i = 0; i < $scope.pager.listData.length; i++) {
                    $scope.listUser.push($scope.pager.listData[i].phone);
                    $scope.listTotal.push($scope.pager.listData[i].total);
                }
            }
        }).catch(error => {
            $location.path("/unauthorized");
        })
        $scope.chart();
        defaulStatistic();
    }

    $scope.initialize();

    $scope.chart = () =>{
        const ctx = document.getElementById('myChart');


        $scope.chartUser = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: $scope.listUser,
                datasets: [{
                    label: 'VNĐ',
                    data: $scope.listTotal,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(255, 159, 64, 0.2)',
                        'rgba(255, 205, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 0, 0, 0.2)',
                        'rgba(0, 255, 0, 0.2)',
                        'rgba(0, 0, 255, 0.2)',
                        'rgba(128, 128, 128, 0.2)'
                    ],
                    borderColor: [
                        'rgb(255, 99, 132)',
                        'rgb(255, 159, 64)',
                        'rgb(255, 205, 86)',
                        'rgb(75, 192, 192)',
                        'rgb(54, 162, 235)',
                        'rgb(153, 102, 255)',
                        'rgb(255, 0, 0)',
                        'rgb(0, 255, 0)',
                        'rgb(0, 0, 255)',
                        'rgb(128, 128, 128)'
                    ],
                    borderWidth: 1
                }]
            }
        });

    }

    defaulStatistic = () =>{
        var valuecbb = document.getElementById('cbb-statistic').value;
        console.log("ssss",valuecbb)
        if(valuecbb === 'default' ){
            document.getElementById("div-button").style.display = "none";
            document.getElementById("end-date").disabled = true;
            document.getElementById("start-date").disabled = true;
        }else if(valuecbb === 'month'){
            console.log("cbb", valuecbb);
            $scope.filterMonth();
            document.getElementById("div-button").style.display = "none";
            document.getElementById("end-date").disabled = true;
            document.getElementById("start-date").disabled = true;
        } else if (valuecbb === 'today'){
            $scope.filterToday();
            document.getElementById("div-button").style.display = "none";
            document.getElementById("end-date").disabled = true;
            document.getElementById("start-date").disabled = true;
        }else {
            document.getElementById("div-button").style.display = "";
            document.getElementById("end-date").disabled = false;
            document.getElementById("start-date").disabled = false;
        }
    }

    $scope.buttonFilterWithDate = async () => {
        var startDateFormat = $filter('date')($scope.startDate, 'yyyy-MM-dd');
        var endDateFormat = $filter('date')($scope.endDate, 'yyyy-MM-dd');

        console.log("startDateFormat", startDateFormat);
        console.log("endDateFormat", endDateFormat);

        await $http.get(`/rest/top-totals-by-date-range?start-date=${startDateFormat}&end-date=${endDateFormat}`).then(resp => {
            if (resp.status === 200) {
                $scope.listUser = [];
                $scope.listTotal = [];
                $scope.listData = [];
                $scope.listData = resp.data.sort((a, b) => b.total - a.total);
                for (let i = 0; i < $scope.pager.listData.length; i++) {
                    $scope.listUser.push($scope.pager.listData[i].phone);
                    $scope.listTotal.push($scope.pager.listData[i].total);
                }
                clearChart();
                $scope.chart();
            }
        });
    }

    $scope.filterToday = async () => {
        var today =  new Date();
        document.getElementById('start-date').value = today.getFullYear().toString().padStart(4, '0') + '-' + (today.getMonth()+1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');
        document.getElementById('end-date').value = today.getFullYear().toString().padStart(4, '0') + '-' + (today.getMonth()+1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');
        await $http.get(`/rest/phone-total-for-today`).then(resp => {
            if (resp.status === 200) {
                $scope.listUser = [];
                $scope.listTotal = [];
                $scope.listData = [];
                $scope.listData = resp.data.sort((a, b) => b.total - a.total);
                for (let i = 0; i < $scope.pager.listData.length; i++) {
                    $scope.listUser.push($scope.pager.listData[i].phone);
                    $scope.listTotal.push($scope.pager.listData[i].total);
                }
                clearChart();
                $scope.chart();
            }
        });
    }

    $scope.filterMonth = async () => {

        var today = new Date();
        var firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
        var lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

        var options = { timeZone: 'Asia/Ho_Chi_Minh' };

        console.log("Ngày đầu tháng:", firstDay.toLocaleDateString('en-US', options).split('/').reverse().join('-'));
        console.log("Ngày cuối tháng:", lastDay.toLocaleDateString('en-US', options).split('/').reverse().join('-'));

        var startDateFormat = firstDay.toLocaleDateString('en-US', options).split('/').reverse().join('-');
        var endDateFormat = lastDay.toLocaleDateString('en-US', options).split('/').reverse().join('-');
        document.getElementById('start-date').value = firstDay.getFullYear().toString().padStart(4, '0') + '-' + (firstDay.getMonth()+1).toString().padStart(2, '0') + '-' + firstDay.getDate().toString().padStart(2, '0');
        document.getElementById('end-date').value = lastDay.getFullYear().toString().padStart(4, '0') + '-' + (lastDay.getMonth()+1).toString().padStart(2, '0') + '-' + lastDay.getDate().toString().padStart(2, '0');
        await $http.get(`/rest/top-totals-by-date-range?start-date=${startDateFormat}&end-date=${endDateFormat}`).then(resp => {
            if (resp.status === 200) {
                $scope.listUser = [];
                $scope.listTotal = [];
                $scope.listData = [];
                $scope.listData = resp.data.sort((a, b) => b.total - a.total);
                for (let i = 0; i < $scope.pager.listData.length; i++) {
                    $scope.listUser.push($scope.pager.listData[i].phone);
                    $scope.listTotal.push($scope.pager.listData[i].total);
                }
                clearChart();
                $scope.chart();
            }
        });
    }

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
        size: 10,
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
        },
        last(){
            this.page = this.count - 1;
            // changeChartData();
            fillDataChart();
        },
        next(){
            this.page++;
            fillDataChart();

        },
        prev(){
            this.page--;
            fillDataChart();
        }
    }

    fillDataChart = () =>{
        $scope.listUser = [];
        $scope.listTotal = [];
        var map = processData($scope.pager.listData);
        for (let [key, value] of map) {
            $scope.listUser.push(key);
            $scope.listTotal.push(value);
        }
        // console.log($scope.chartUser);
        clearChart();
        $scope.chart();
    }

    clearChart = () =>{
        if($scope.chartUser){
            $scope.chartUser.destroy();
        }
    }

    $scope.firstChart = () =>{
        fillDataChart();
    }

});