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
                let dept = past['dept_time'].split(" ");
                let dest = past['dest_time'].split(" ");
                $("#past").append("<tr>\n" +
                    "<th scope=\"col\">" + past['dept_station'] + "</th>\n" +
                    "<th scope=\"col\">" + past['dest_station'] + "</th>\n" +
                    "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dept[0] + "</th>\n" +
                    "<th scope=\"col\">" + dest[0] + "</th>\n" +
                    "<th scope=\"col\">" + "status" +"</th>" +
                    "<th scope=\"col\"><button type=\"button\" class=\"btn btn-primary\">Cancel Ticket</button></th>" +
                    "</tr>")
            });

            futures.forEach(function (future) {
                let dept = future['dept_time'].split(" ");
                let dest = future['dest_time'].split(" ");
                $("#future").append("<tr>\n" +
                    "<th scope=\"col\">" + future['dept_station'] + "</th>\n" +
                    "<th scope=\"col\">" + future['dest_station'] + "</th>\n" +
                    "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dept[0] + "</th>\n" +
                    "<th scope=\"col\">" + dest[0] + "</th>\n" +
                    "<th scope=\"col\">" + "status" +"</th>" +
                    "<th scope=\"col\"><button type=\"button\" class=\"btn btn-primary\">Cancel Ticket</button></th>" +
                    "</tr>")
            });
        }).fail( function () {

        });
    }
}



$(document).ready(function () {
    getUserData();
});