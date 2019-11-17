let cookie = $.cookie('encrypted');

function logout() {
    $.removeCookie('encrypted', { path: '/'});
    window.location.replace("/railway_station_service_war_exploded");
}

function notify(items) {
    var login = items['login'];
    var message = $("#message").val();

    $.post("/railway_station_service_war_exploded/services/items/send_notify", {
        login: login,
        message: message
    }, function () {
        alert("Notification message is created")
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
        console.log(items[i]);
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
            'Authorization': "Basic " + cookie
        }
    });

    if(typeof $.cookie('encrypted') === "undefined"){
        console.log("Cookie doesn't exists");
    } else{
        $.post("/railway_station_service_war_exploded/services/manager/secured/managerProfile", {
            authToken: cookie
        }, function (out) {
            console.log("getUserData: " + out);
            let data = JSON.parse(out);
            let first_name = data['first_name']
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
            'Authorization': "Basic " + cookie
        }
    });

    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            console.log("list of Employees: " + data);
            createListOfEmployees(JSON.parse(data));
        },
    });

}


$(document).ready(function () {
    // getUserData();
    ListOfEmployees();
    $("#notifyAll").on('click', function () {
        notify();
    });
    $("#makePayment").on('click', function () {
        ListOfEmployees();
    });
});