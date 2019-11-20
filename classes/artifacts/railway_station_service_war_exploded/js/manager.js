function notify(items) {
    var login = items['login'];
    var message = $("#message").val();

    $.post("/railway_station_service_war_exploded/services/items/send_notify", {
        login: login,
        message: message
    }, function () {
        alert("Notification message is created");
        window.location.replace("/railway_station_service_war_exploded/manager.html");
    }).fail( function () {
        alert("Notification message is not created")
    });
}

let employeeData;
function createListOfEmployees(items) {
    let str = "";
    employeeData = items;
    for (let i=0; i<items.length; i++) {
        str +="<tr id=\"" + i + "\"><th scope=\"row\">"+items[i].first_name+"</th><td>"+ items[i].last_name +"</td><td>"+items[i].salary + "</td>";
        str +="<td>" + items[i].email + "</td><td>" + items[i].schedule + "</td>";
        str +="<td><button type=\"submit\" onclick ='payroll(" + i +");' class=\"btn btn-primary\">Paycheck</button></td></tr>";
    }
    $("#manager-agents").html("");
    $("#manager-agents").append(str);
}

function payroll(index) {
    let items = employeeData[index];
    let url = "/railway_station_service_war_exploded/services/manager/secured/" + items['login'] + "/" + items['salary'];

    $.ajax({
        type: "GET",
        url: url,
        success: function () {
        },
    });
}

function getUserData() {

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });

    if(typeof $.cookie('encrypted') === "undefined"){
        console.log("Cookie doesn't exists");
    } else{
        $.post("/railway_station_service_war_exploded/services/manager/secured/managerProfile", {
            authToken: getCookie()
        }, function (out) {
            let data = JSON.parse(out);
            let first_name = data['first_name'];
            let last_name = data['last_name'];
            let phone = data['phone'];
            let email = data['email'];

        })
    }
}

function ListOfEmployees() {
    let url = "/railway_station_service_war_exploded/services/manager/secured/listOfEmployees";

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });

    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            createListOfEmployees(JSON.parse(data));
        },
    });

}

function readTextFile() {
    $.ajax({
        type: 'GET',
        url: "/railway_station_service_war_exploded/services/items/getLogs",
        success: function(out) {
            out = out.replace("\"","");
            out = out.replace("\"","");
            console.log(out);
            let arr = out.split("\\n");
            let str = "";
            for(let i=0; i<arr.length; i++){
                str+= "<p class='logs_text'>" + arr[i] +"</p>"
            }
            $("#logs").append(str);
        },
        fail: function(err) {
            console.log(err);
        },
        contentType: "application/json"
    })
}

$(document).ready(function () {
    // getUserData();
    readTextFile();
    ListOfEmployees();
    $("#notifyAll").on('click', function () {
        notify();
    });
    $("#makePayment").on('click', function () {
        ListOfEmployees();
    });
});