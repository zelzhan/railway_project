let ticketData;
let cookie = $.cookie('encrypted');

function logout() {
    $.removeCookie('encrypted', { path: '/'});
    window.location.replace("/railway_station_service_war_exploded");
}

function updateTicket(id){
    localStorage.setItem( 'id', id);
    window.location.replace("http://localhost:8080/railway_station_service_war_exploded/updateTicket.html");
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

function getTickets() {
    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + cookie
        }
    });

    $.ajax({
        type: 'POST',
        url: "/railway_station_service_war_exploded/services/agent/secured/agentProfile",
        success: function(out) {
            ticketData = JSON.parse(out);
            ticketData.forEach(function(eachTicket, i){
                let owner = eachTicket['key'];
                let ticket = eachTicket['value'];
                let dept = ticket['dept_time'].split(" ");
                let dest = ticket['dest_time'].split(" ");
                let appendText = "<tr>\n" +
                    "<th scope=\"col\">" + ticket['dept_station'] + "</th>\n" +
                    "<th scope=\"col\">" + ticket['dest_station'] + "</th>\n" +
                    "<th scope=\"col\">" + dept[0] + "</th>\n" +
                    "<th scope=\"col\">" + dest[0] + "</th>\n" +
                    "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + "<button type=\"button\" onclick='updateTicket(" + ticket['id'] + ")' class=\"btn btn-primary\">Update Ticket</button>" + "</th>";
                if (ticket['status'] === "Cancelled") {
                    appendText = appendText + "<th scope=\"col\">" + "Done!" +"</th></tr>";
                } else {
                    appendText = appendText + "<th scope=\"col\">" + "<button type=\"button\" onclick='cancelTicket(" + ticket['id'] + ")' class=\"btn btn-primary\">Cancel Ticket</button>" + "</th></tr>";
                    $("#agent-tickets").append(appendText);
                }
                });
            },
        fail: function(err) {
            console.log(err);
            },
        contentType: "application/json"
    })
}

$(document).ready(function () {
    getProfile();
    getTickets();
});