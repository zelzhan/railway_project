let cookie = $.cookie('encrypted');
let routeData;

function showTickets() {
    let data = [];
    $("form#routeForm :input").each(function () {
        var input = $(this); // This is the jquery object of the input, do what you will
        data.push(input.val());
    });

    let url = "/railway_station_service_war_exploded/services/items/" + data[0] + "/" + data[1] + "/" + data[4] + "-" + data[3] + "-" + data[2];

    $.ajax({
        type: "GET",
        url: url,
        success: function (out) {
            let appendText = "";
            if (out === "") {
                alert("Place doesn't exist");
            } else {
                routeData = JSON.parse(out);
                routeData.forEach(function (ticket, i) {
                    let dept = ticket['start_date'].split(" ");
                    let dest = ticket['end_date'].split(" ");
                    appendText += "<tr>\n" +
                        "<th scope=\"col\">" + ticket['train_id'] + "</th>\n" +
                        "<th scope=\"col\">" + ticket['dep'] + "</th>\n" +
                        "<th scope=\"col\">" + ticket['des'] + "</th>\n" +
                        "<th scope=\"col\">" + dept[0] + "</th>\n" +
                        "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                        "<th scope=\"col\">" + dest[0] + "</th>\n" +
                        "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
                        "<th scope=\"col\">" + ticket['capacity'] + "</th>\n" +
                        "<th scope=\"col\"><button type=\"submit\" onclick ='showMap(" + i + ");' class=\"btn btn-primary\">Show Map</button></th>" +
                        "<th scope=\"col\"><button type=\"button\" onclick='buyTicket(" + i + ")' class=\"btn btn-primary\">Update Ticket</button></th></tr>";
                });
                $("#agent-tickets").html("");
                $("#agent-tickets").append(appendText);

            }
        }
    });
}

function getOldTicket(){
    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + cookie
        }
    });

    let id = getUrlParameter('id');
    $.ajax({
        type: 'POST',
        url: "/railway_station_service_war_exploded/services/agent/secured/getTicket/"+ id,
        success: function(out) {
            let ticket = JSON.parse(out);
            let dept = ticket['dept_time'].split(" ");
            let dest = ticket['dest_time'].split(" ");
            let appendText = "<tr>\n" +
                "<th scope=\"col\">" + ticket['train_id'] + "</th>\n" +
                "<th scope=\"col\">" + ticket['dept_station'] + "</th>\n" +
                "<th scope=\"col\">" + ticket['dest_station'] + "</th>\n" +
                "<th scope=\"col\">" + dept[0] + "</th>\n" +
                "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                "<th scope=\"col\">" + dest[0] + "</th>\n" +
                "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
                "<th scope=\"col\">" + ticket['email'] + "</th></tr>";
            $("#old-ticket").html("");
            $("#old-ticket").append(appendText);
        },
        fail: function(err) { console.log(err) },
        contentType: "application/json"
    });

}

function getProfile() {
    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + cookie
        }
    });

    $.ajax({
        type: 'POST',
        url: "/railway_station_service_war_exploded/services/agent/secured/userAgent",
        success: function(out) {
            let data = JSON.parse(out);
            $("#full_name").append(data["first_name"]+" "+data["last_name"]);
            $("#email").append(data["email"]);
            $("#phone").append(data["phone"]);
            $("#workHours").append(data["workingHours"]);
            $("#salary").append(data["salary"]);
        },
        fail: function(err) { console.log(err) },
        contentType: "application/json"
    });
}

function cancelTicket(id) {
    $.ajaxSetup({
        headers: {
            'Authorization': "Basic " + cookie
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

function getUrlParameter(sParam) {
    return localStorage[sParam];
}

function buyTicket(index){
    let id = getUrlParameter('id');
    let train_id = routeData[index].train_id;
    let start_station = routeData[index].dep;
    let end_station = routeData[index].des;
    let dest_time = routeData[index].start_date;
    let dept_time = routeData[index].end_date;
    let route_id = routeData[index].route_id;
    let cookie = $.cookie('encrypted');
    let email = atob(cookie).split(":")[0];
    $.ajax({
        type: 'POST',
        url: "/railway_station_service_war_exploded/services/items/buyTicket",
        data: JSON.stringify( {
            email: email,
            train_id: train_id,
            start_station: start_station,
            end_station: end_station,
            destTime: dest_time,
            deptTime: dept_time,
            route_id: route_id
        }),
        success: function() {
            alert('Successful purchase!');
            cancelTicket(id);
            showTickets();
            window.location.replace("http://localhost:8080/railway_station_service_war_exploded/agent.html")
        },
        fail: function(err) { alert(err) },
        contentType: "application/json"
    })
}

$(document).ready(function () {
    getProfile();
    getOldTicket();
    $("#search-route").on('click', function() {
        showTickets();
    });
});