createPassword = () =>{
    if(!valdateCreatePassword()){
        return;
    }
}

valdateCreatePassword = () =>{
    var newPassword = document.getElementById('password').value
    var enterPassword = document.getElementById('enter-password').value

    var isValid = true

    if(newPassword == ''){
        document.getElementById('error-password').innerText='Không được để trống mật khẩu'
        isValid = false
    }else{
        document.getElementById('error-password').innerText=''
    }

    if(enterPassword == ''){
        document.getElementById('error-enter-password').innerText='Không được để trống nhập lại mật khẩu'
        isValid = false
    }else if(newPassword !== enterPassword){
        document.getElementById('error-enter-password').innerText='Mật khẩu không trùng khớp'
        isValid = false
    }else{
        document.getElementById('error-enter-password').innerText=''
    }

    return isValid

}

clearForm = () =>{
    document.getElementById('error-password').innerText=''
    document.getElementById('error-enter-password').innerText=''

}