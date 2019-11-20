function getOldTicket(){
    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });

    let id = localStorage['id'];
    $.ajax({
        type: 'POST',
        url: "/railway_station_service_war_exploded/services/agent/secured/getTicket/"+ id,
        success: function(out) {
            console.log(out);
            // let ticket = JSON.parse(out);
            // let dept = ticket['dept_time'].split(" ");
            // let dest = ticket['dest_time'].split(" ");
            // let appendText = "<tr>\n" +
            //     "<th scope=\"col\">" + ticket['train_id'] + "</th>\n" +
            //     "<th scope=\"col\">" + ticket['dept_station'] + "</th>\n" +
            //     "<th scope=\"col\">" + ticket['dest_station'] + "</th>\n" +
            //     "<th scope=\"col\">" + dept[0] + "</th>\n" +
            //     "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
            //     "<th scope=\"col\">" + dest[0] + "</th>\n" +
            //     "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
            //     "<th scope=\"col\">" + ticket['email'] + "</th></tr>";
            // $("#old-ticket").html("");
            // $("#old-ticket").append(appendText);
        },
        fail: function(err) { console.log(err) },
        contentType: "application/json"
    });

}

$(document).ready(function () {
    getProfile();
    getOldTicket();
    $("#search-route").on('click', function() {
        showTickets("updateTicket");
    });
});