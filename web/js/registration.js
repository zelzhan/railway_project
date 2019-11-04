
function register() {
    var email = $("#email").val();
    var password = $("#inputPassword").val();
    var phone = $("#phone").val();
    var firstName = $("#firstName").val();
    var lastName = $("#lastName").val();

    $.post("/railway_station_service_war_exploded/services/items/registration", {
        email: email,
        password: password,
        phone: phone,
        firstName: firstName,
        lastName: lastName
    }, function () {
        alert("Registration is successful.")
        window.location.replace("/railway_station_service_war_exploded/");
    }).fail( function () {
        alert("Registration is not successful.")
    });
}


$(document).ready(function () {

    if (typeof $.cookie('encripted') != "undefined") {
        $("#login").hide();
        $("#signup").hide();
        $("#userprofile").show();
        $("#signout").show();
    } else {
        $("#login").show();
        $("#signup").show();
        $("#userprofile").hide();
        $("#signout").hide();
    }

    $("#submit-reg").on('click', function () {
        register();
    });
});