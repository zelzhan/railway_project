// let routeData;
//
// function showTickets() {
//     $.ajax({
//         type: "GET",
//         url: getTicketsUrl(),
//         success: function (out) {
//             let appendText = "";
//             if (out === "") {
//                 alert("Place doesn't exist");
//             } else {
//                 routeData = JSON.parse(out);
//                 routeData.forEach(function (ticket, i) {
//                     let dept = ticket['start_date'].split(" ");
//                     let dest = ticket['end_date'].split(" ");
//                     appendText += "<tr>\n" +
//                         "<th scope=\"col\">" + ticket['train_id'] + "</th>\n" +
//                         "<th scope=\"col\">" + ticket['dep'] + "</th>\n" +
//                         "<th scope=\"col\">" + ticket['des'] + "</th>\n" +
//                         "<th scope=\"col\">" + dept[0] + "</th>\n" +
//                         "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
//                         "<th scope=\"col\">" + dest[0] + "</th>\n" +
//                         "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
//                         "<th scope=\"col\">" + ticket['capacity'] + "</th>\n" +
//                         "<th scope=\"col\"><button type=\"submit\" onclick ='showMap(" + i + ");' class=\"btn btn-primary\">Show Map</button></th>" +
//                         "<th scope=\"col\"><button type=\"button\" onclick='buyTicket(" + i + ")' class=\"btn btn-primary\">Update Ticket</button></th></tr>";
//                 });
//                 $("#agent-tickets").html("");
//                 $("#agent-tickets").append(appendText);
//
//             }
//         }
//     });
// }

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
            let ticket = JSON.parse(out);
            let dept = ticket['dept_time'].split(" ");
            let dest = ticket['dest_time'].split(" ");
            let appendText = "<tr>\n" +
                "<th scope=\"col\">" + ticket['train_id'] + "</th>\n" +
                "<th scope=\"col\">" + ticket['dept_station'] + "</th>\n" +
                "<th scope=\"col\">" + ticket['dest_station'] + "</th>\n" +
                "<th scope=\"col\">" + dept[0] + "</th>\n" +
                "<th scope=\"col\">" + dept[1].slice(0, -2) + "</th>\n" +
                "<th scope=\"col\">" + dest[0] + "</th>\n" +
                "<th scope=\"col\">" + dest[1].slice(0, -2) + "</th>\n" +
                "<th scope=\"col\">" + ticket['email'] + "</th></tr>";
            $("#old-ticket").html("");
            $("#old-ticket").append(appendText);
        },
        fail: function(err) { console.log(err) },
        contentType: "application/json"
    });

}

// function buyTicket(index){
//     let id = getUrlParameter('id');
//     let train_id = routeData[index].train_id;
//     let start_station = routeData[index].dep;
//     let end_station = routeData[index].des;
//     let dest_time = routeData[index].start_date;
//     let dept_time = routeData[index].end_date;
//     let route_id = routeData[index].route_id;
//     let email = atob(getCookie()).split(":")[0];
//     $.ajax({
//         type: 'POST',
//         url: "/railway_station_service_war_exploded/services/items/buyTicket",
//         data: JSON.stringify( {
//             email: email,
//             train_id: train_id,
//             start_station: start_station,
//             end_station: end_station,
//             destTime: dest_time,
//             deptTime: dept_time,
//             route_id: route_id
//         }),
//         success: function() {
//             alert('Successful purchase!');
//             cancelTicket(id);
//             showTickets();
//             window.location.replace("http://localhost:8080/railway_station_service_war_exploded/agent.html")
//         },
//         fail: function(err) { alert(err) },
//         contentType: "application/json"
//     })
// }

$(document).ready(function () {
    getProfile();
    getOldTicket();
    $("#search-route").on('click', function() {
        showTickets("updateTicket");
    });
});