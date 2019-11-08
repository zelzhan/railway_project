function login() {
    let email = $("#email").val();
    let password = $("#password").val();

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + btoa(email + ":" + password)
        }
    });

    $.get("/railway_station_service_war_exploded/services/items/secured/login", {}, function () {
        $.cookie('encrypted', btoa(email + ":" + password), { path : '/'});
        window.location.replace("/railway_station_service_war_exploded/");
        alert("login is successful.")
    }).fail( function (data) {
        console.log(data);
        alert("login is not successful.")
    });
}

$(document).ready(function () {
    $("#submit-reg").on('click', function () {
        login();
    });
});