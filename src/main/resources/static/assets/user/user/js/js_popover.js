$(function () {
    var customPopover = $('#customPopover');

    $('#popoverButton').click(function () {
        if (customPopover.is(':visible')) {
            customPopover.hide();
        } else {
            customPopover.show();
        }
    });
});



clickCheckbox = () => {
    var checkbox = document.querySelectorAll(".checkbox-popover");
    for (var i = 0; i < checkbox.length; i++) {
        if (checkbox[i].checked) {
            checkbox[i].checked = false;
        }
    }
    document.getElementById('form').submit();
}

buttonPopover = () => {
    var checkbox = document.querySelectorAll(".checkbox-sidebar");
    for (var i = 0; i < checkbox.length; i++) {
        if (checkbox[i].checked) {
            checkbox[i].checked = false;
        }
    }
    document.getElementById('form').submit();
}