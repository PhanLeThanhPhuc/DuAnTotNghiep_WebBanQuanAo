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
            // Store the message in localStorage
            localStorage.setItem('successMessage', response.message);

            // Redirect to another page
            window.location.href = '/user/info';
        },
        error: function(error) {
            // Handle errors if needed
        }
    });
}

showMessage = () => {
    // Retrieve the message from localStorage
    var successMessage = localStorage.getItem('successMessage');

    // Check if there is a message to display
    if (successMessage) {
        message(successMessage);

        // Clear the stored message to avoid displaying it again
        localStorage.removeItem('successMessage');
    }
}

// Call the showMessage function on page load
showMessage();

message = (mes) => {
    $.toast({
        text: mes,
        heading: 'Note',
        icon: 'success',
        // ... rest of your code ...
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
});