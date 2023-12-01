register = ()=> {
    if(!validateForm()){
        return;
    }
    var userObject = {};

    userObject.fullname = document.getElementById('full_name').value;
    userObject.phone = document.getElementById('phone').value;
    userObject.email = document.getElementById('email').value;
    userObject.password = document.getElementById('password').value;
    userObject.confirmPassword = document.getElementById('confirm_password').value;

    console.log(userObject)
    $.ajax({
        type: 'POST',
        url: '/user/register',
        data: JSON.stringify(userObject),
        contentType: 'application/json',
        success: function(response) {
            console.log(response)
            if(response.status === false){
                console.log(response.message)
                document.getElementById('message_respose').innerText = response.message;
            }else{
                console.log(response.message)
                location.href = "/user/index";
            }
        },
        error: function(error) {

        }
    });
}

validateForm = () =>{

    var fullname = document.getElementById('full_name').value;
    var phone = document.getElementById('phone').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('confirm_password').value;

    console.log(confirmPassword)

    var isValid = true;

    if (fullname === '') {
        document.getElementById('full_name_error').innerText = 'Tên đăng nhập không được để trống';
        isValid = false;
    } else {
        document.getElementById('full_name_error').innerText = '';
    }

    if (password === '') {
        document.getElementById('password_error').innerText = 'Mật khẩu không được để trống';
        isValid = false;
    } else {
        document.getElementById('password_error').innerText = '';
    }

    if (confirmPassword === '') {
        document.getElementById('confirm_password_error').innerText = 'Mật khẩu xác nhận không được để trống';
        isValid = false;
    }else if (confirmPassword !== password) {
        document.getElementById('confirm_password_error').innerText = 'Mật khẩu và mật khẩu xác nhận không khớp';
        isValid = false;
    } else {
        document.getElementById('confirm_password_error').innerText = '';
    }

    if (email === '') {
        document.getElementById('email_error').innerText = 'Email không được để trống';
        isValid = false;
    } else {
        document.getElementById('email_error').innerText = '';
    }

    if (phone === '') {
        document.getElementById('phone_error').innerText = 'Số điện thoại không được để trống';
        isValid = false;
    } else {
        document.getElementById('phone_error').innerText = '';
    }
    return isValid;
}