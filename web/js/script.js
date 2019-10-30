
function updateRoute(items) {
	let str = "<table class=\"table table-bordered\"><thead class=\'thead-dark\'><tr><th scope=\"col\">#</th>"
	str += "<th scope=\"col\">Departure</th><th scope=\"col\">Destination</th><th scope=\"col\">Time</th></tr></thead>"
	str += "<tbody>";
	for (let i=0; i<items.length; i++) {
		str +="<tr><th scope=\"row\">"+items[i].train_id+"</th><td>"+ items[i].dep +"</td><td>"+items[i].des + "</td><td>" + items[i].date + "</td></tr>";
	}
	str += "</tbody></table>";
	$("#table-table").html("");
	$("#table-table").append(str);

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


$(document).ready(function () {

    $("#submit-reg").on('click', function () {
        register();
    });

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
                alert(data);
                updateRoute(JSON.parse(data));

            },
        });
    });
});