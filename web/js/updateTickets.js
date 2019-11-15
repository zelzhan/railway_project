let cookie = $.cookie('encrypted');
let routeData;

function showTickets() {
    let data = []
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
                console.log(out);
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
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
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

    $.ajax({
        type: 'POST',
        url: "/railway_station_service_war_exploded/services/items/buyTicket",
        data: JSON.stringify( {
            authToken: cookie,
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
    $("#search-route").on('click', function() {
        showTickets();
    });
});