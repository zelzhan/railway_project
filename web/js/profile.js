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
            let phone = data['phone'];
            let email = data['email'];
            let pasts = data['past'];
            let futures = data['future'];
            $("#full_name").append(full_name);
            $("#email").append(email);
            $("#phone").append(phone);

            pasts.forEach(function (past) {
                console.log(past);
                $("#past").append("<li class=\"list-group-item list_of_tickets\">\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past['dept_station'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past['dest_station'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past['dept_exact_time'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past['dest_exact_time'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past['dept_time'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + past['dest_time'] + "</a>\n" +
                    "</li>")
            });

            futures.forEach(function (future) {
                console.log(future);
                $("#future").append("<li class=\"list-group-item list_of_tickets\">\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future['dept_station'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future['dest_station'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future['dept_exact_time'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future['dest_exact_time'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future['dept_time'] + "</a>\n" +
                    "<a class=\"col-md-3\" ng-href=\"#\">" + future['dest_time'] + "</a>\n" +
                    "</li>")
            });
        }).fail( function () {

        });
    }
}



$(document).ready(function () {
    getUserData();
});