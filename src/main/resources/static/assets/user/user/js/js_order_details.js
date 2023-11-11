function submitForm() {
    var selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
    var idOrder = document.getElementById("inputIdOrder").value;
    document.getElementById('paymentForm').action = '/submitPayment/' + selectedPaymentMethod+'/' + idOrder;
    document.getElementById('paymentForm').submit();
}
