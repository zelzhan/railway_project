function register() {
    let email = $("#email").val();
    let password = $("#inputPassword").val();
    let phone = $("#phone").val();
    let firstName = $("#firstName").val();
    let lastName = $("#lastName").val();

    $.post("/railway_station_service_war_exploded/services/items/registration", {
        email: email,
        password: password,
        phone: phone,
        firstName: firstName,
        lastName: lastName
    }, function () {
        alert("Registration is successful.");
        home();
    }).fail( function () {
        alert("Registration is not successful.")
    });
}


$(document).ready(function () {
    cookieCheck();
    $("#submit-reg").on('click', function () {
        register();
    });
});