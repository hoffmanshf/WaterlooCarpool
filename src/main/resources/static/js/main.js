(function ($) {
    $.toggleShowPassword = function (options) {
        var settings = $.extend({
            field: "#password",
            control: "#toggle_show_password",
        }, options);

        var control = $(settings.control);
        var field = $(settings.field)

        control.bind('click', function () {
            if (control.is(':checked')) {
                field.attr('type', 'text');
            } else {
                field.attr('type', 'password');
            }
        })
    };



}(jQuery));

$(document).ready(function() {
    var confirm = function() {
        bootbox.confirm({
            title: "Booking Confirmation",
            message: "Do you really want to schedule this booking?",
            buttons: {
                cancel: {
                    label: '<i class="fa fa-times"></i> Cancel'
                },
                confirm: {
                    label: '<i class="fa fa-check"></i> Confirm'
                }
            },
            callback: function (result) {
                if (result == true) {
                    $('#bookingForm').submit();
                } else {
                    console.log("Scheduling cancelled.");
                }
            }
        });
    };

    $.toggleShowPassword({
        field: '#password',
        control: "#showPassword"
    });


    $('#submitBooking').click(function () {
        confirm();
    });

});

$(function() {

    var checkbox = $("#switch");
    var hidden = $("#hidden-fields");

    hidden.hide();
    checkbox.change(function() {
        if (checkbox.is(':checked')) {
            hidden.show();
        } else {
            hidden.hide();
        }
    });
});

$(".form_datetime[data-view='hour']").datetimepicker({
    format: "yyyy-mm-dd hh:ii",
    autoclose: true,
    todayBtn: true,
    minuteStep: 10
});


