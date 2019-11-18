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

        $.ajaxSetup({
            headers:{
                'Authorization': "Basic " + btoa(email + ":" + password)
            }
        });

        $.get("/railway_station_service_war_exploded/services/items/getRole", {}, function (res) {
            res = res.split("\"").join("");
            console.log(res);
            $.cookie('role', res, { path : '/'} );
            home();
            alert("login is successful.")
        });

    }).fail( function (err) {
        console.log(err);
        alert("login is not successful.")
    });
}

$(document).ready(function () {
    $("#submit-reg").on('click', function () {
        login();
    });
});