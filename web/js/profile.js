function getUserData() {
    let cookie = $.cookie('encripted');

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + cookie
        }
    });

    console.log(cookie);
    if(typeof $.cookie('encripted') === "undefined"){
        console.log("Cookie doesn't exists");
        // window.location.replace("/railway_station_service_war_exploded/");
    } else{
        $.post("/railway_station_service_war_exploded/services/items/secured/userProfile", {
            authToken: cookie
        }, function (out) {
            console.log(out);
            let data = JSON.parse(out);
            let full_name = data['first_name'] + " " + data['last_name'];
            let email = data['email'];
            let pasts = data['past'];
            let futures = data['future'];
            $("#full_name").append(full_name);
            $("#email").append(email);
            $("#past").html("");
            $("#future").html("");

            pasts.forEach(function (past) {
                $("#past").append("<li class=\"list-group-item list_of_tickets\">\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past[0] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past[1] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past[2] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past[3] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past[4] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past[5] + "</a>\n" +
                    "</li>")
            });

            futures.forEach(function (future) {
                $("#future").append("<li class=\"list-group-item list_of_tickets\">\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future[0] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future[1] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future[2] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future[3] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future[4] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future[5] + "</a>\n" +
                    "</li>")
            });

            alert("login is successful.")
        }).fail( function () {
            alert("login is not successful.")
        });
    }
}



$(document).ready(function () {
    getUserData();
});