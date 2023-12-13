app.controller("statistic-ctrl", function($scope, $filter, $http,$location) {

    $scope.listTotalDate = [];
    $scope.date =[];
    $scope.total =[];
    $scope.totalPrice = 0;

    $scope.initialize = async function() {
        await $http.get("/rest/orderTotal").then(resp => {
            $scope.map = new Map();
            if (resp.status === 200){
                $scope.listTotalDate = resp.data;
                $scope.map = processData($scope.listTotalDate);
                const tenDaysAgoArray = $scope.arrayTenDayAgo();
                processTenDaysAgoArray(tenDaysAgoArray,$scope.map);
            }
        }).catch(error => {
            $location.path("/unauthorized");
        })

        $scope.total.reverse();
        $scope.date.reverse();
        $scope.chart();
        defaulStatistic();
        $scope.getHoursOfDay();
    }
    $scope.initialize();

    processData = (arr) => {
        // $scope.listTotalDate =arr;
        var map = new Map();
        for (let i = 0; i <arr.length; i++) {
            const date = $scope.formatDate(arr[i].order_date);
            if (map.has(date)) {
                let value = map.get(date);
                value += arr[i].total;
                map.set(date, value);
            } else {
                map.set(date, arr[i].total);
            }
        }
        return map;
    }

    processTenDaysAgoArray =(tenDaysAgoArray, map) => {
        console.log("MAP: ",map);
        console.log("tenDaysAgoArray: ",tenDaysAgoArray);
        $scope.date =[];
        $scope.total =[];
        for (let i = 0; i < tenDaysAgoArray.length; i++) {
            const date = tenDaysAgoArray[i];
            if (map.has(date)) {
                let value = map.get(date);
                $scope.date.push(date);
                $scope.total.push(value);
            } else {
                $scope.date.push(date);
                $scope.total.push(0);
            }
        }
        $scope.totalPrice=0;
        $scope.totalPrice = $scope.calculateTotalPrice($scope.total);
        $scope.messageTotalPrice = `Tổng doanh thu trong 10 ngày trước:`;

    }

    $scope.chart = () => {
        const ctx = document.getElementById('myChart');
        if ($scope.myChart) {
            $scope.myChart.destroy();
        }

        $scope.myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: $scope.date,
                datasets: [{
                    label: 'VNĐ',
                    data: $scope.total,
                    fill: false,
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                }]
            },
            options: {
                animations: {
                    tension: {
                        duration: 1000,
                        easing: 'linear',
                        from: 1,
                        to: 0,
                        loop: true
                    }
                },
                locale:'de-DE',
                scales: {
                    y: {
                        beginAtZero: true,
                    }
                }
            }
        });
    }


    $scope.calculateTotalPrice = (arr) => {
        return arr.reduce((acc, val) => acc + val, 0);
    };

    $scope.filterTotalDate = () => {
        var listTotalDateCopy = []
        var startDateFormat = $scope.startDate;
        var endDateFormat = $scope.endDate;

        var listTotalDateCopy = $scope.listTotalDate;

        $scope.listFilterNow = listTotalDateCopy.filter(o => {
            const formattedOrderDate = $scope.formatDate(o.order_date);
            return formattedOrderDate >= $scope.formatDate(startDateFormat) && formattedOrderDate <= $scope.formatDate(endDateFormat);
        });

        var map = processData($scope.listFilterNow);
        const tenDaysAgoArray = getDates(startDateFormat, endDateFormat);
        processTenDaysAgoArray(tenDaysAgoArray, map);
        $scope.chart();
        // console.log("arr",$scope.listTotalDate)
    };

    $scope.formatDate = function(date) {
        return $filter('date')(date, 'dd/MM/yyyy');
    };


    $scope.arrayTenDayAgo = () => {
        const today = new Date();
        const days = [];
        for (let i = 0; i <= 10; i++) {

            const date = new Date();

            date.setDate(today.getDate() - i);


            const dd = date.getDate();
            const mm = date.getMonth() + 1;
            const yyyy = date.getFullYear();

            const formatted = yyyy + '-' + mm + '-' + dd;

            days.push($scope.formatDate(formatted));
        }
        console.log("daert",days)
        document.getElementById('end-date').value = today.toISOString().substring(0,10);
        console.log("date",today.toISOString().substring(0,10));
        var index = days.length -1;
        const startDate = days[index]
        console.log("date start: ", formatDate(startDate));
        document.getElementById('start-date').value = formatDate(startDate)
        return days;
        // console.log(days);
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
            $scope.totalByMonth();
            document.getElementById("div-button").style.display = "none";
            document.getElementById("end-date").disabled = true;
            document.getElementById("start-date").disabled = true;
        } else if (valuecbb === 'today'){
            $scope.TotalInToday();
            document.getElementById("div-button").style.display = "none";
            document.getElementById("end-date").disabled = true;
            document.getElementById("start-date").disabled = true;
        }else {
            document.getElementById("div-button").style.display = "";
            document.getElementById("end-date").disabled = false;
            document.getElementById("start-date").disabled = false;
        }
    }

    $scope.getDayByMonth = () => {
        const date = new Date();
        const month = date.getMonth();
        const year = date.getFullYear();
        const day = date.getDate();
        const days = [];

        // Duyệt từ 1 đến ngày hiện tại
        for (let d = 1; d <= day; d++) {

            const currentDate = new Date(year, month, d);

            days.push($scope.formatDate(currentDate));
        }

        console.log("Mảng chứa ngày của tháng", days);
        return days;
    }


    $scope.totalByMonth = () =>{
        $scope.date =[];
        $scope.total =[];
        const tenDaysAgoArray = $scope.getDayByMonth();
        for (let i = 0; i < tenDaysAgoArray.length; i++) {
            if ($scope.map.has(tenDaysAgoArray[i])) {
                let value = $scope.map.get(tenDaysAgoArray[i]);
                $scope.date.push(formatDayMonth(tenDaysAgoArray[i]));
                $scope.total.push(value);
            } else {
                $scope.date.push(formatDayMonth(tenDaysAgoArray[i]));
                // console.log("datedawdea", tenDaysAgoArray[i])
                $scope.total.push(0);
            }
        }
        document.getElementById('start-date').value =formatDate(tenDaysAgoArray[0]);
        var index = tenDaysAgoArray.length-1;
        document.getElementById('end-date').value =formatDate(tenDaysAgoArray[index]);
        $scope.totalPrice=0;
        $scope.totalPrice = $scope.calculateTotalPrice($scope.total);
        $scope.messageTotalPrice = `Tổng doanh thu tháng này`;
        $scope.chart();
        $scope.$apply();
    }



    function formatDate(dateString) {
        var parts = dateString.split('/');
        var formattedDate = [parts[2], parts[1], parts[0]].join('-');
        var d = new Date(formattedDate),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2)
            month = '0' + month;
        if (day.length < 2)
            day = '0' + day;

        return [year, month, day].join('-');
    }

    function formatDayMonth(dateString) {
        var parts = dateString.split('/');
        return [parts[0], parts[1]].join('/');
    }

    $scope.getHoursOfDay = () => {
        const hours = [];


        for (let hour = 0; hour < 24; hour++) {

            const formattedHour = hour.toString().padStart(2, '0');
            // const currentTime = `${formattedHour}:00:00`;


            hours.push(formattedHour);
        }

        // console.log("Mảng chứa giờ ", hours);
        return hours;
    }

    function getDates(startDate, endDateValue) {
        const endDate = new Date(endDateValue);
        const dates = [];
        const currentDate = new Date(startDate);
        while (currentDate <= endDate) {
            dates.push($scope.formatDate(currentDate));
            currentDate.setDate(currentDate.getDate() + 1);
        }
        return dates;
    }

    $scope.formatHours = (arr) => {
        const listTotal = [];
        for (let i = 0; i < arr.length; i++) {
            var item = arr[i];
            var hour = formatTime(item.order_date). split(':')[0]; // Lấy giờ từ order_date
            item.order_date = hour;
            listTotal.push(item);
        }
        return listTotal;
    }

     formatTime = (dateString) => {
        let date = new Date(dateString);
        let formattedTime = date.toLocaleTimeString();
        return formattedTime;
    }

    $scope.TotalInToday = () =>{
        console.log("Data list", $scope.listTotalDate);

        // Make a copy of the original array
        var listTotalDateCopy = angular.copy($scope.listTotalDate);

        var today = new Date();
        var todayFormatted = today.toLocaleDateString('en-GB', { timeZone: 'Asia/Ho_Chi_Minh' });

        // Use the copied array for filtering
        var filteredData = listTotalDateCopy.filter(item => {
            var itemDate = new Date(item.order_date);
            var itemDateFormatted = itemDate.toLocaleDateString('en-GB', { timeZone: 'Asia/Ho_Chi_Minh' });
            return itemDateFormatted === todayFormatted;
        });

        var listHour = $scope.getHoursOfDay();
        var listHourFormatDatabase = $scope.formatHours(filteredData);

        console.log("listHour", listHour);
        console.log("listHourFormatDatabase", listHourFormatDatabase);

        var map = new Map();
        for (let i = 0; i <listHourFormatDatabase.length; i++) {
            const date = listHourFormatDatabase[i].order_date
            console.log("date", date)
            if (map.has(date)) {
                let value = map.get(date);
                value += listHourFormatDatabase[i].total;
                map.set(date, value);
            } else {
                map.set(date, listHourFormatDatabase[i].total);
            }
        }
        $scope.date =[];
        $scope.total =[];
        for (let i = 0; i < listHour.length; i++) {
            if (map.has(listHour[i])) {
                let value = map.get(listHour[i]);
                $scope.date.push(listHour[i]+':00');
                console.log("value", value)
                $scope.total.push(value);
            } else {
                $scope.date.push(listHour[i]+':00');
                $scope.total.push(0);
            }
        }
        $scope.chart();
        console.log("total hours", map)
        $scope.totalPrice=0;
        $scope.totalPrice = $scope.calculateTotalPrice($scope.total);
        $scope.messageTotalPrice = `Tổng doanh thu ngày ngày hôm nay`;
        document.getElementById('start-date').value =today.toISOString().split('T')[0];
        document.getElementById('end-date').value =today.toISOString().split('T')[0];
        $scope.$apply();
    }

});