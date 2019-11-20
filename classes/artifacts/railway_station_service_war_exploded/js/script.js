let routeDate;

function updateRoute(items) {
    let str = "<table class=\"table table-bordered\"><thead class=\'thead-dark\'><tr><th scope=\"col\">#</th>";
    str += "<th scope=\"col\">Departure</th><th scope=\"col\">Destination</th><th scope=\"col\">Time of Departure</th><th scope=\"col\">Date of Departure</th><th scope=\"col\">Time of Arrival</th>";
    str += "<th scope=\"col\">Date of Arrival</th><th scope=\"col\">Capacity</th><th scope=\"col\">Route</th><th scope=\"col\"></th></tr></thead><tbody>";
    routeDate = items;
    for (let i=0; i<items.length; i++) {
        str +="<tr id=\"" + i + "\"><th scope=\"row\">"+items[i].train_id+"</th><td>"+ items[i].dep +"</td><td>"+items[i].des + "</td>";
        str +="<td>" + items[i].start_date.split(" ")[0] + "</td><td>" + items[i].start_date.split(" ")[1] + "</td>";
        str +="<td>" + items[i].end_date.split(" ")[0] + "</td><td>" + items[i].end_date.split(" ")[1] + "</td>";
        str +="<th scope=\"row\">"+items[i].capacity+"</th>";
        str +="<td><button type=\"submit\" onclick ='showMap(" + i +");' class=\"btn btn-primary\">Show Map</button></td> <td><button type=\"submit\" onclick ='buyTicket(" + i +");' class=\"btn btn-primary\">Buy ticket</button></td></tr>";
    }
    str += "</tbody></table>";
    $("#table-table").html("");
    $("#table-table").append(str);
}

function showTickets() {
    $.ajax({
        type: "GET",
        url: getTicketsUrl(),
        success: function (data) {
            if (data === "") {
                alert("Place doesn't exist");
            }
            updateRoute(JSON.parse(data));
        },
    });
}

function showMap(index) {
    let items = routeDate[index];
    let url = "/railway_station_service_war_exploded/services/items/" + items['dep']
        + "/" + items['des'] + "/" + items['datey'] + "/"+items['dateh'] + "/" + items['route_id'];

    $.ajax({
        type: "GET",
        url: url,
        success: function () {
            window.location.replace("map.html");
        },
    });
}

function sendFormRoute() {
    let depToSend = $("#dep-input").val().toString();
    let desToSend = $("#des-input").val().toString();

    if (depToSend.match("\\w+") && desToSend.match("\\w+")) {
        $.post("services/items/" + "{" + depToSend + "}/{" + desToSend + "}/{" + $("#day-input").val().toString() + "-"
            + $("#month-input").val().toString() + "-" + $("#year-input").val().toString() + "}", function () {
            getRouteItems();
        })
    } else {
        alert("Please, check your input");
    }
}

function getRouteItems() {
    $.ajax({
        url: 'services/items/',
        dataType: 'json',
        success: function (r) {
            updateRoute(r);
        }
    });

    $.get("/railway_station_service_war_exploded/services/secured/message", {}, function () {
        alert("Authentication is successful.");
    }).fail( function () {
        alert("Authentication failed.")
    });
}

function buyTicket(index){
    let train_id = routeDate[index].train_id;
    let start_station = routeDate[index].dep;
    let end_station = routeDate[index].des;
    let dest_time = routeDate[index].start_date;
    let dept_time = routeDate[index].end_date;
    let route_id = routeDate[index].route_id;
    let email = atob(getCookie()).split(":")[0];
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
            showTickets();
        },
        fail: function(err) { alert(err) },
        contentType: "application/json"
    })
}

$(document).ready(function () {
    $.get("/railway_station_service_war_exploded/services/items/initialize", {}, function () {
        console.log("Successfully initialized!");
    });
    cookieCheck();
    $("#search-route").on('click', function() {
        showTickets();
    });
});
