let routeDate;

function logout() {
    $.removeCookie('encrypted', { path: '/'});
    window.location.replace("/railway_station_service_war_exploded");
}

function updateTicket(){
    let train_id = routeDate[index].train_id;
    let start_station = routeDate[index].dep;
    let end_station = routeDate[index].des;
    let dest_time = routeDate[index].start_date;
    let dept_time = routeDate[index].end_date;
    let route_id = routeDate[index].route_id;
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
            showTickets();
        },
        fail: function(err) { alert(err) },
        contentType: "application/json"
    })
}

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
        success: function (data) {
            if (data === "") {
                alert("Place doesn't exist");
            }
            updateRoute(JSON.parse(data));
        },
    });

}
