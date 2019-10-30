function updateRoute(items) {
    $("#table-table").html("");
    $("#table-table").append("<div class=\"train-table\"><div class=\"container\"><table class=\"table table-dark\"><thead>");
    $("#table-table").append("<tr><th scope=\"col\">#</th><th scope=\"col\">Departure   </th><th scope=\"col\">Destination</th>");
    // $("#table-table").append("<th scope=\"col\">Departure Time</th><th scope=\"col\">Destination time</th><th scope=\"col\">Date</th></tr></thead><tbody>");
    console.log(items);

    for (let i = 0; i < items.length; i++) {
        $("#table-table").append("<tr><th scope=\"row\">" + items[i].train_id + "</th><td>" + items[i].dep + "</td><td>" + items[i].des + "</td><td>" + items[i].date + "</td></tr>");
    }
    $("#table-table").append("</tbody></table></div></div>");

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

function register() {
    var email = $("#email").val();
    var password = $("#inputPassword").val();
    var phone = $("#phone").val();
    var firstName = $("#firstName").val();
    var lastName = $("#lastName").val();

    $.post("/railway_station_service_war_exploded/services/items/send", {
        email: email,
        password: password,
        phone: phone,
		firstName: firstName,
		lastName: lastName
    }, function () {
    	alert("Registration is successful.")
	}).fail( function () {
		alert("Registration is not successful.")
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