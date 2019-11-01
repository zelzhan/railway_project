let routeDate;

function updateRoute(items) {
    let str = "<table class=\"table table-bordered\"><thead class=\'thead-dark\'><tr><th scope=\"col\">#</th>"
    str += "<th scope=\"col\">Departure</th><th scope=\"col\">Destination</th><th scope=\"col\">Time</th><th scope=\"col\">-----</th></tr></thead>"
    str += "<tbody>";
    routeDate = items;
    for (let i=0; i<items.length; i++) {
        console.log(items[i]);
        str +="<tr><th scope=\"row\">"+items[i].train_id+"</th><td>"+ items[i].dep +"</td><td>"+items[i].des + "</td><td>" + items[i].dateh + "</td>" +
            "<td><button type=\"submit\" onclick ='showMap(" + i +");' class=\"btn btn-primary\">Show Map</button></td></tr>";
    }
    str += "</tbody></table>";
    $("#table-table").html("");
    $("#table-table").append(str);
}

function showMap(index) {
    let items = routeDate[index];
    let url = "/railway_station_service_war_exploded/services/items/" + items['dep']
        + "/" + items['des'] + "/" + items['datey'] + "/"+items['dateh'] + "/" + items['route_id'];

    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            window.location.replace("C:\\Users\\abyl\\Desktop\\Fall 2019\\rails\\web\\map.html");
        },
    });
}

function sendFormRoute() {
    var depToSend = $("#dep-input").val().toString();
    var desToSend = $("#des-input").val().toString();

    if (depToSend.match("\\w+") && desToSend.match("\\w+")) {
        $.post("services/items/" + "{" + depToSend + "}/{" + desToSend + "}/{" + $("#day-input").val().toString() + "-"
            + $("#month-input").val().toString() + "-" + $("#year-input").val().toString() + "}", function () {
            getRouteItems();
        })
    } else {
        window.alert("Please, check your input");
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
}


function test() {

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + $.cookie('encripted')
        }
    });

    $.get("/railway_station_service_war_exploded/services/secured/message", {}, function () {
        alert("Authentication is successful.");
    }).fail( function () {
        alert("Authentication failed.")
    });

}


function logout() {
    $.removeCookie('encripted', { path: '/'});
    window.location.replace("/railway_station_service_war_exploded");
}


function buyTicket(){
    let train_id = 563;
    let start_station_id = 8;
    let end_station_id = 7;
    let dest_time = "2019-04-04 23:34:22";
    let dept_time = "2019-04-05 23:34:22";

    $.post("/railway_station_service_war_exploded/services/items/buyTicket", {
       train_id: train_id,
       start_station_id: start_station_id,
       end_station_id: end_station_id,
       destTime: dest_time,
       deptTime: dept_time
    }, function () {
        console.log("hey");
        alert("We bought ticket");
    }).fail( function (){
        console.log("not hey");

    });
}

$(document).ready(function () {
    buyTicket();

    if (typeof $.cookie('encripted') != "undefined") {
        $("#login").hide();
        $("#signup").hide();
        $("#userprofile").show();
        $("#signout").show();
    } else {
        $("#login").show();
        $("#signup").show();
        $("#userprofile").hide();
        $("#signout").hide();
    }


    $("#routeForm").submit(function (e) {

        e.preventDefault(); // avoid to execute the actual submit of the form.

        var form = $(this);
        var data = [];

        $("form#routeForm :input").each(function () {
            var input = $(this); // This is the jquery object of the input, do what you will
            data.push(input.val());
        });

        // console.log(data);
        let url = "/railway_station_service_war_exploded/services/items/" + data[0] + "/" + data[1] + "/" + data[4] + "-" + data[3] + "-" + data[2];

        $.ajax({
            type: "GET",
            url: url,
            success: function (data) {
                if (data === "") {
                    alert("Place doesn't exist");
                }
                updateRoute(JSON.parse(data));

            },
        });
    });
});
