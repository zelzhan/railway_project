function getUserData() {
    let cookie = $.cookie('encripted');

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + cookie
        }
    });

    if(typeof $.cookie('encripted') === "undefined"){
        console.log("Cookie doesn't exists");
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
                $("#past").append("<tr>\n" +
                    "<th scope=\"col\">" + past['dept_station'] + "</th>\n" +
                    "<th scope=\"col\">" + past['dest_station'] + "</th>\n" +
                    //"<th scope=\"col\">" + past['dept_exact_time'] + "</th>\n" +
                    //"<th scope=\"col\">" + past['dest_exact_time'] + "</th>\n" +
                    "<th scope=\"col\">" + past['dept_time'] + "</th>\n" +
                    "<th scope=\"col\">" + past['dest_time'] + "</th>\n" +
                    "</tr>")
            });

            futures.forEach(function (future) {
                console.log(future);
                $("#future").append("<tr>\n" +
                    "<th scope=\"col\">" + future['dept_station'] + "</th>\n" +
                    "<th scope=\"col\">" + future['dest_station'] + "</th>\n" +
                    //"<th scope=\"col\">" + future['dept_exact_time'] + "</th>\n" +
                    //"<th scope=\"col\">" + future['dest_exact_time'] + "</th>\n" +
                    "<th scope=\"col\">" + future['dept_time'] + "</th>\n" +
                    "<th scope=\"col\">" + future['dest_time'] + "</th>\n" +
                    "</tr>")
            });
        }).fail( function () {

        });
    }
}



$(document).ready(function () {
    getUserData();
});