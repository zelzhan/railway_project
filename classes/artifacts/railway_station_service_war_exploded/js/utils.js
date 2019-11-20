function redirect(pathName){
    window.location.replace("http://localhost:8080/railway_station_service_war_exploded/"+pathName+".html");
}

function createTicketPage() {
    redirect("createTicket");
}

function agent() {
    redirect("agent");
}

function passenger() {
    redirect("profile");
}

function manager() {
    redirect("manager");
}

function profile() {
    if(getRole()==="agent"){
        agent();
    } else if(getRole()==="manager"){
        manager();
    } else if(getRole()==="passenger"){
        passenger();
    } else{
        alert("Wrong role");
    }
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
    $.ajaxSetup({
        headers:{
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

function getRole() {
    return $.cookie('role');
}

let routeData;

function showTickets(buttonType) {
    $.ajax({
        type: "GET",
        url: getTicketsUrl(),
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
                        "<th scope=\"col\"><button type=\"submit\" onclick ='showMap(" + i + ");' class=\"btn btn-primary\">Show Map</button></th>";
                    if(buttonType==="createTicket"){
                        appendText+="<th scope=\"col\"><button type=\"button\" onclick='createTicket(" + i + ")' class=\"btn btn-primary\">Create Ticket</button></th></tr>";
                    }else if("updateTicket"){
                        appendText+="<th scope=\"col\"><button type=\"button\" onclick='updateTicket(" + i + ")' class=\"btn btn-primary\">Update Ticket</button></th></tr>";
                    }
                });
                $("#agent-tickets").html("");
                $("#agent-tickets").append(appendText);

            }
        }
    });
}

function createTicket(index){
    let train_id = routeData[index].train_id;
    let start_station = routeData[index].dep;
    let end_station = routeData[index].des;
    let dest_time = routeData[index].start_date;
    let dept_time = routeData[index].end_date;
    let route_id = routeData[index].route_id;
    getEmailFromField();
    if(exist === "false"){
        alert("Email doesn't exists!");
        return;
    }

    let email = $("#pass-email").val();
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
            showTickets("createTicket");
        },
        fail: function(err) { alert(err) },
        contentType: "application/json"
    })
}

function updateTicket(index){
    let train_id = routeData[index].train_id;
    let start_station = routeData[index].dep;
    let end_station = routeData[index].des;
    let dest_time = routeData[index].start_date;
    let dept_time = routeData[index].end_date;
    let route_id = routeData[index].route_id;
    let email = getCurrentEmail();

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
            cancelTicket(localStorage['id']);
            showTickets();
            agent();
        },
        fail: function(err) { alert(err) },
        contentType: "application/json"
    })
}

function getCurrentEmail() {
    return atob(getCookie()).split(":")[0];
}

let exist;

function getEmailFromField(){
    let email = $("#pass-email").val();
    console.log(email);
    emailExists(email).done(function (out){
        out = out.split("\"").join("");
        exist = out;
    });
}

function emailExists(email) {
    return $.ajax({
        type: 'GET',
        url: "/railway_station_service_war_exploded/services/items/existsEmail/"+email,
        async: false,
        success: function(out) {
            console.log(out);
            },
        contentType: "application/json"
    });
}