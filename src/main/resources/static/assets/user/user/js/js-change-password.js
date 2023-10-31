$('#update-password-button').on('click', function() {

    var oldPassword = $('#old-password').val();
    var newPassword = $('#new-password').val();
    var confirmPassword = $('#enter-the-password').val();

    if (newPassword !== confirmPassword) {
        message('Mật khẩu mới và xác nhận mật khẩu không trùng khớp.');
    } else {

        var data = {
            oldPassword: oldPassword,
            newPassword: newPassword,
            confirmPassword: confirmPassword
        };

        $.ajax({
            type: 'POST',
            url: '/user/change-password',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(response) {
                message('Cập nhật mật khẩu thành công.');
                document.getElementById('close').click();
            },
            error: function(error) {
                message('Mật khẩu cũ sai, vui lòng nhập lại!');
                console.error('Lỗi khi cập nhật mật khẩu.');
            }
        });
    }
});

check = () => {
    console.log("jkđskk")
    $('#exampleModalCenter1').modal('hide');
}


message = (mes) =>{
    $.toast({
        text: mes,
        heading: 'Note',
        icon: 'success',
        showHideTransition: 'fade',
        allowToastClose: true,
        hideAfter: 3000,
        stack: 5,
        position: 'top-right', // bottom-left or bottom-right or bottom-center or top-left or top-right or top-center or mid-center or an object representing the left, right, top, bottom values
        textAlign: 'left',  // Text alignment i.e. left, right or center
        loader: true,  // Whether to show loader or not. True by default
        loaderBg: '#9EC600',  // Background color of the toast loader
        beforeShow: function () {}, // will be triggered before the toast is shown
        afterShown: function () {}, // will be triggered after the toat has been shown
        beforeHide: function () {}, // will be triggered before the toast gets hidden
        afterHidden: function () {}  // will be triggered after the toast has been hidden
    });
}

$(document).ready(function() {
    $("#upload-file-input").on("change", function() {
        if (this.files && this.files[0]) {
            uploadFile();
            $("#profileImage").attr("src", "/assets/user/user/image/4colors.gif");
        }
    });
});

function uploadFile() {
    $.ajax({
        url: "/user/upload",
        type: "POST",
        data: new FormData($("#upload-file-form")[0]),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            $("#profileImage").attr("src", data);
            message('Cập nhật hình ảnh thành công.');
        },
        error: function () {
            // Handle upload error
            // ...
        }
    });
}