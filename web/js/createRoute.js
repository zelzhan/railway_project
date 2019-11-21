let routes;
let selected = false;
let prveRouteId;
let selectedRoute;


function select(i) {
    if (selected) {

        let str = "";
        str+="<th scope=\"row\">" + routes[prveRouteId] + "</th>";
        str +="<td><button type=\"button\" onclick ='select(" + prveRouteId +");' class=\"btn btn-primary\" id='0" + prveRouteId + "'>Select</button></td>";

        $("#" + prveRouteId).html("");
        $("#" + prveRouteId).append(str);
    }

    selected = true;
    prveRouteId = i;

    selectedRoute = routes[i];

    let str = "";
    str +="<th scope=\"row\">" + routes[i] + "</th>";
    str +="<td>Selected</td>";

    // let str = "";
    // str+= "<th scope=\"row\">" + routes[prveRouteId] + "</th>";
    // str +="<td><button type=\"button\" onclick ='select(" + prveRouteId +");' class=\"btn btn-primary\" id='0" + prveRouteId + "'>Select</button></td>";


    $("#" + i).html("");
    $("#" + i).append(str);

}


function getRoutes () {
    let dep = $("#dep").val();
    let des = $("#des").val();

    let url = "/railway_station_service_war_exploded/services/manager/showRoutes/" + dep + "/" + des;
    $.ajax({
        type: "GET",
        url: url,
        success: function (res) {
            routes = JSON.parse(res);
            let str = "";
            for (let i=0; i<routes.length; i++) {
                str +="<tr id=\"" + i + "\"><th scope=\"row\">" + routes[i] + "</th>";
                str +="<td><button type=\"button\" onclick ='select(" + i +");' class=\"btn btn-primary\" id='0" + i + "'>Select</button></td></tr>";
            }
            $("#create-routes").html("");
            $("#create-routes").append(str);
            console.log(res);
        },
    });
}


function createRoute(){
    let year = $("#year-input").val();
    let month = $("#month-input").val();
    let day = $("#day-input").val();
    let hour = $("#hour-input").val();
    let min = $("#min-input").val();
    let sec = $("#sec-input").val();

    let date = year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
    console.log(date);
    console.log(selectedRoute);
    let url = "/railway_station_service_war_exploded/services/manager/createRoute/";

    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify( {
            stations: selectedRoute,
            departureTime : date
        }),
        success: function() {
            alert('Route successfully created!');
            window.location.replace("manager.html");
        },
        fail: function(err) { alert(err) },
        contentType: "application/json"
    })

}



$(document).ready(function () {
    $("#search-route").on('click', function () {
        getRoutes();
    });

    $("#create-route").on('click', function () {
        createRoute();

    })
});