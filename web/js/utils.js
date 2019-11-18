function redirect(pathName){
    window.location.replace("http://localhost:8080/railway_station_service_war_exploded/"+pathName+".html");
}

function createTicket() {
    redirect("createTicket");
}

function agent() {
    redirect("agent");
}

function profile() {
    redirect("profile");
}

function home() {
    window.location.replace("http://localhost:8080/railway_station_service_war_exploded");
}

function updateTicketPage() {
    redirect("updateTicket");
}

function logout() {
    $.removeCookie('encrypted', { path: '/'});
    home();
}

function getProfile() {
    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
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
            $("#work-hours").append(data["workingHours"]);
            $("#salary").append(data["salary"]);
        },
        fail: function(err) { console.log(err) },
        contentType: "application/json"
    });
}

function cookieCheck() {
    if (typeof $.cookie('encrypted') != "undefined") {
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
}

function cancelTicket(id) {
    $.ajax({
        type: "POST",
        url: encodeURI("/railway_station_service_war_exploded/services/items/cancelTicket?ticket_id=" + id),
        success: function () {
            alert("Ticket successfully cancelled!");
            location.reload();
        }
    });
}

function getTicketsUrl(){
    let data = [];
    $("form#routeForm :input").each(function () {
        var input = $(this); // This is the jquery object of the input, do what you will
        data.push(input.val());
    });

    return "/railway_station_service_war_exploded/services/items/" + data[0] + "/" + data[1] + "/" + data[4] + "-" + data[3] + "-" + data[2];
}

function getCookie(){
    return $.cookie('encrypted');
}