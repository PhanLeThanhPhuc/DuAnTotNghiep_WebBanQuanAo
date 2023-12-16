$(document).ready(function () {
createPassword = () => {
    if (!validateCreatePassword()) {
        return;
    }

    var userObject = {
        password: document.getElementById('password').value
    }

    $.ajax({
        type: 'POST',
        url: '/rest/users/create-password',
        data: JSON.stringify(userObject),
        contentType: 'application/json',
        success: function(response) {

            localStorage.setItem('successMessage', response.message);

            window.location.href = '/user/info';
        },
        error: function(error) {
            // Handle errors if needed
        }
    });
}

showMessage = () => {
    var successMessage = localStorage.getItem('successMessage');

    if (successMessage) {
        message(successMessage);

        localStorage.removeItem('successMessage');
    }
}


showMessage();

message = (mes) => {
    $.toast({
        text: mes,
        heading: 'Note',
        icon: 'success',

    });
}

validateCreatePassword = () => {
    var newPassword = document.getElementById('password').value;
    var enterPassword = document.getElementById('enter-password').value;

    var isValid = true;

    if (newPassword == '') {
        document.getElementById('error-password').innerText = 'Không được để trống mật khẩu';
        isValid = false;
    } else {
        document.getElementById('error-password').innerText = '';
    }

    if (enterPassword == '') {
        document.getElementById('error-enter-password').innerText = 'Không được để trống nhập lại mật khẩu';
        isValid = false;
    } else if (newPassword !== enterPassword) {
        document.getElementById('error-enter-password').innerText = 'Mật khẩu không trùng khớp';
        isValid = false;
    } else {
        document.getElementById('error-enter-password').innerText = '';
    }

    return isValid;
}

clearForm = () => {
    document.getElementById('error-password').innerText = '';
    document.getElementById('error-enter-password').innerText = '';
}

updateUserInfo = () => {
    var name = document.getElementById('name-info').value;
    var email = document.getElementById('email-info').value;
    var gender = document.querySelector('input[name="radio-gender"]:checked').value;


    var userObject = {
        fullName: name,
        email: email,
        gender: gender
    };

    $.ajax({
        type: 'POST',
        url: '/rest/users/update-user-info',
        data: JSON.stringify(userObject),
        contentType: 'application/json',
        success: function(response) {
            localStorage.setItem('successUpdateUser', response.message);

            window.location.href = '/user/info';
        },
        error: function(error) {
            // Handle errors if needed
            console.error(error);
        }
    });
}

    showMessageUserInfo = () => {
        var successMessage = localStorage.getItem('successUpdateUser');

        if (successMessage) {
            message(successMessage);

            localStorage.removeItem('successUpdateUser');
        }
    }


    showMessageUserInfo();

});