$.ajax({
    url: "/user/province",
    method: "GET",
    success: function (resp) {
        var jsonData = JSON.parse(resp);
        var listProvince = jsonData.data;

        console.log(listProvince);
        var comboxboxProvince = document.getElementById("comboxboxProvince");
        for (var i = 0; i < listProvince.length; i++) {
            var option = document.createElement("option");
            option.text = listProvince[i].ProvinceName;
            option.value = listProvince[i].ProvinceID;
            comboxboxProvince.add(option);
        }
    },
    error: function (xhr, status, error) {
        console.error("Error fetching data:", error);
    }
});

district = (province_id) => {
    // Clear comboboxDistrict and comboboxWard
    document.getElementById("comboboxDistrict").innerHTML = "";
    document.getElementById("comboboxWard").innerHTML = "";

    $.ajax({
        url: `/user/district?province_id=${province_id}`,
        method: "GET",
        success: function (resp) {
            var jsonData = JSON.parse(resp);
            var listDistrict = jsonData.data;

            console.log(listDistrict);
            var comboxboxDistrict = document.getElementById("comboboxDistrict");
            for (var i = 0; i < listDistrict.length; i++) {
                var option = document.createElement("option");
                option.text = listDistrict[i].DistrictName;
                option.value = listDistrict[i].DistrictID;
                comboxboxDistrict.add(option);
            }
        },
        error: function (xhr, status, error) {
            console.error("Error fetching district data:", error);
        }
    });
}

ward = (district_id) => {
    // Clear comboboxWard
    document.getElementById("comboboxWard").innerHTML = "";

    $.ajax({
        url: `/user/ward?district_id=${district_id}`,
        method: "GET",
        success: function (resp) {
            var jsonData = JSON.parse(resp);
            var listWard = jsonData.data;

            console.log(listWard);
            var comboboxWard = document.getElementById("comboboxWard");
            for (var i = 0; i < listWard.length; i++) {
                var option = document.createElement("option");
                option.text = listWard[i].WardName;
                option.value = listWard[i].WardCode;
                comboboxWard.add(option);
            }
        },
        error: function (xhr, status, error) {
            console.error("Error fetching ward data:", error);
        }
    });
}

createAddress = () => {
    var cboAddress = document.querySelectorAll(".country_select");

    var addressObject = {
        provinceId: cboAddress[0].options[cboAddress[0].selectedIndex].value,
        districtId: cboAddress[1].options[cboAddress[1].selectedIndex].value,
        wardId: cboAddress[2].options[cboAddress[2].selectedIndex].value,
        provinceName: cboAddress[0].options[cboAddress[0].selectedIndex].text,
        districtName: cboAddress[1].options[cboAddress[1].selectedIndex].text,
        wardName: cboAddress[2].options[cboAddress[2].selectedIndex].text,
        addressDetail: document.getElementById("addressDetail").value
    }



    $.ajax({
        type: 'POST',
        url: '/rest/insert-address',
        data: JSON.stringify(addressObject),
        contentType: 'application/json',
        success: function(response) {
          location.href = '/user/address';
        },
        error: function(error) {

        }
    });
}

