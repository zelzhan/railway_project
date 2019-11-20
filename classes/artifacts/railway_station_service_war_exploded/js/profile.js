function getUserData() {
let cookie = $.cookie('encrypted');

function logout() {
    $.removeCookie('encrypted', { path: '/'});
    window.location.replace("/railway_station_service_war_exploded");
}

function removeTicket(id) {
    $.ajaxSetup({
        headers: {
            'Authorization': "Basic " + getCookie()
        }
    });

    $.ajax({
        type: "POST",
        url: encodeURI("/railway_station_service_war_exploded/services/items/cancelTicket?ticket_id=" + id),
        success: function () {
            alert("Ticket successfully cancelled!");
            location.reload();
        }
    });
}
function getAllPaychecks() {

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });
    let email = atob(cookie).split(":")[0];
    let url = "/railway_station_service_war_exploded/services/items/paychecklist/" + email;
    $.ajax({
        type: "GET",
        url: url,
        success: function () {
            window.location.replace("paycheck.html");
        },
    });
}


    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });

    if(typeof $.cookie('encrypted') === "undefined"){
        console.log("Cookie doesn't exists");
    } else{
        $.post("/railway_station_service_war_exploded/services/items/secured/userProfile", {
            authToken: getCookie()
        }, function (out) {
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
                    "<th scope=\"col\">" + past['status'] +"</th></tr>")
            });

            futures.forEach(function (future) {
                let dept = future['dept_time'].split(" ");
                let dest = future['dest_time'].split(" ");

                let append = "";
                if (future['status'] === "Cancelled") {
                    append = "<th scope=\"col\">" + "Done!" +"</th>"
                } else {
                    append = "<th scope=\"col\"><button type=\"button\" onclick='cancelTicket(" + future.id + ")' class=\"btn btn-primary\" id=\"" + future.id + "\">Cancel Ticket</button></th>";
                }

                $("#future").append("<tr>\n" +
                    "<th scope=\"col\">" + future['dept_station'] + "</th>\n" +
                    "<th scope=\"col\">" + future['dest_station'] + "</th>\n" +
                    "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dept[0] + "</th>\n" +
                    "<th scope=\"col\">" + dest[0] + "</th>\n" +
                    "<th scope=\"col\">" + future['status'] +"</th>" + append + "</tr>")
                }
            );
        })
    }
}

$(document).ready(function () {
    getUserData();
    $("#paycheck").on('click', function () {
        getAllPaychecks();
    });
});