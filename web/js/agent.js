function updateTicket(id){
    localStorage.setItem( 'id', id);
    updateTicketPage();
    window.location.replace("http://localhost:8080/railway_station_service_war_exploded/updateTicket.html");
}

function cancelTicket(id) {
    $.ajaxSetup({
        headers: {
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
function getAllPaychecks() {

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });
    let email = atob(getCookie()).split(":")[0];
    let url = "/railway_station_service_war_exploded/services/agent/secured/paychecklist/" + email;
    $.ajax({
        type: "GET",
        url: url,
        success: function () {
            window.location.replace("paycheck.html");
        },
    });
}

function getTickets() {
    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });

    $.ajax({
        type: 'POST',
        url: "/railway_station_service_war_exploded/services/agent/secured/agentProfile",
        success: function(out) {
            let ticketData = JSON.parse(out);
            ticketData.forEach(function(eachTicket){
                let owner = eachTicket['key'];
                let ticket = eachTicket['value'];
                let dept = ticket['dept_time'].split(" ");
                let dest = ticket['dest_time'].split(" ");
                let appendText = "<tr>\n" +
                    "<th scope=\"col\">" + owner['first_name'] + " " + owner['last_name'] + "</th>\n" +
                    "<th scope=\"col\">" + ticket['dept_station'] + "</th>\n" +
                    "<th scope=\"col\">" + ticket['dest_station'] + "</th>\n" +
                    "<th scope=\"col\">" + dept[0] + "</th>\n" +
                    "<th scope=\"col\">" + dest[0] + "</th>\n" +
                    "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                    "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n";
                if (ticket['status'] !== "Cancelled") {
                    appendText = appendText + "<th scope=\"col\">" + "<button type=\"button\" onclick='updateTicket(" + ticket['id'] + ")' class=\"btn btn-primary\">Update Ticket</button>" + "</th>";
                    appendText = appendText + "<th scope=\"col\">" + "<button type=\"button\" onclick='cancelTicket(" + ticket['id'] + ")' class=\"btn btn-primary\">Cancel Ticket</button>" + "</th></tr>";
                }else{
                    appendText = appendText + "<th scope=\"col\">-</th> <th scope=\"col\">" + "Ticket is cancelled." + "</th></tr>";

                }
                $("#agent-tickets").append(appendText);

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
    $("#paycheck").on('click', function () {
        getAllPaychecks();
    });
});