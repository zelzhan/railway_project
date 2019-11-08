function login() {
    var email = $("#email").val();
    var password = $("#password").val();

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + btoa(email + ":" + password)
        }
    });

    $.get("/railway_station_service_war_exploded/services/items/secured/login", {}, function () {
        $.cookie('encripted', btoa(email + ":" + password), { path : '/'});

        $.get("/railway_station_service_war_exploded/services/items/getRole", {}, function (res) {
            res = res.replaceAll("\"", "");
            $.cookie('role', res );
        });

        window.location.replace("/railway_station_service_war_exploded/");
        alert("login is successful.")
    }).fail( function () {
        alert("login is not successful.")
    });
    console.log("logged in!");
}




$(document).ready(function () {
    $("#submit-reg").on('click', function () {
        console.log("submitted");
        login();
    });


});