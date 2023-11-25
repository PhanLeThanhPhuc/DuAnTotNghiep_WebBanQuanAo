app.controller("statistic-ctrl", function($scope, $filter, $http) {

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
                    console.log("Map data: ", $scope.map);
                    const tenDaysAgoArray = $scope.arrayTenDayAgo();
                    processTenDaysAgoArray(tenDaysAgoArray,$scope.map);
                }
            });
            $scope.total.reverse();
            $scope.date.reverse();
            $scope.chart();
            defaulStatistic();
        }
        $scope.initialize();

         processData = (arr) => {
            $scope.listTotalDate =arr;
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

    }

    $scope.chart = () => {
        const ctx = document.getElementById('myChart');
        new Chart(ctx, {
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

    $scope.filterTotalDate = () =>{

        var startDateFormat = $scope.startDate;
        var endDateFormat = $scope.endDate;

        $scope.listFilterNow = $scope.listTotalDate.filter(o => {
            const formattedOrderDate = $scope.formatDate(o.order_date);
            return formattedOrderDate >= $scope.formatDate(startDateFormat) && formattedOrderDate <= $scope.formatDate(endDateFormat);
        });

        var map = processData($scope.listFilterNow);
        const tenDaysAgoArray = getDates(startDateFormat,endDateFormat);
        processTenDaysAgoArray(tenDaysAgoArray,map);
        $scope.chart();

    }

    $scope.totalToDay = () =>{
        $scope.date =[];
        $scope.total =[];
        const today = new Date();
        const dateFormat = $scope.formatDate(today);
        var listTotalToDay = $scope.listTotalDate.filter( o =>{
            console.log("a", $scope.formatDate(o.order_date));
            console.log("to day", dateFormat);
            const formattedOrderDate = $scope.formatDate(o.order_date);
            return formattedOrderDate === dateFormat;
        });
        var map = processData(listTotalToDay);

        for (const [key, value] of map) {
            // console.log(key, value);
            $scope.date.push(key);
            $scope.total.push(value);
        }
        $scope.totalPrice=0;
        $scope.totalPrice = $scope.calculateTotalPrice($scope.total);
        $scope.chart();
        console.log("listdate to day", map);
        console.log("listdate to day", $scope.totalPrice);
    }

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
        const startDate = new Date(Date.parse(days[index]));
        console.log(startDate.toISOString().substring(0,10));
        document.getElementById('start-date').value = startDate.toISOString().substring(0,10);
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
            $scope.totalToDay();
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
                console.log("datedawdea", tenDaysAgoArray[i])
                $scope.total.push(0);
            }
        }
        $scope.totalPrice = $scope.calculateTotalPrice($scope.total);
        $scope.chart();
        document.getElementById('start-date').value =formatDate(tenDaysAgoArray[0]);
        var index = tenDaysAgoArray.length-1;
        console.log(index)
        document.getElementById('end-date').value =formatDate(tenDaysAgoArray[index]);
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
            const currentTime = `${formattedHour}:00:00`;


            hours.push(currentTime);
        }

        console.log("Mảng chứa giờ ", hours);
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

});