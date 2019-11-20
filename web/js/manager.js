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
let trainData;
function createListOfEmployees(items) {
    $("#employee").show();
    $("#trains").hide();
    employeeData = items;
    let str = "";
    for (let i=0; i<items.length; i++) {
        str +="<tr id=\"" + i + "\"><th scope=\"row\">"+items[i].first_name+"</th><td>"+ items[i].last_name +"</td><td>"+items[i].salary + "</td>";
        str +="<td>" + items[i].email + "</td><td>" + items[i].schedule + "</td>";
        str +="<td><button type=\"submit\" onclick ='payroll(" + i +");' class=\"btn btn-primary\">Paycheck</button></td></tr>";
    }
    $("#manager-agents").html("");
    $("#manager-agents").append(str);
}

function cancelRoute(index) {
    //To do
    //Take all required data from trainData and remove which is required
    alert("route is cancelled!");
}

function createListOfTrains(items) {
    $("#employee").hide();
    $("#trains").show();
    trainData = items;
    let str = "";
    for (let i=0; i<items.length; i++) {
        let date = items[i][2].split(" ");
        str +="<tr id=\"" + i + "\"><th scope=\"row\">"+items[i][0]+"</th><td>"+ items[i][1] +"</td><td>"+items[i][3] + "</td>";
        str +="<td>" + date[1].slice(0, -2) + "</td><td>" + date[0] + "</td>";
        str +="<td><button type=\"submit\" onclick ='cancelRoute(" + i +");' class=\"btn btn-primary\">Cancel ticket</button></td></tr>";
    }
    $("#manager-trains").html("");
    $("#manager-trains").append(str);
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
            console.log(out);
            let data = JSON.parse(out);
            console.log(data);
            let first_name = data['first_name'];
            let last_name = data['last_name'];
            let phone = data['phone'];
            let email = data['email'];

        })
    }
}

function getAllPaychecks() {

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });
    let email = atob(getCookie()).split(":")[0];
    let url = "/railway_station_service_war_exploded/services/manager/secured/paychecklist/" + email;
    $.ajax({
        type: "GET",
        url: url,
        success: function () {
            window.location.replace("paycheck.html");
        },
    });
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
            console.log(data);
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
                if(arr[i].indexOf("ALMT") !== -1){
                    str += "</br>";
                }
            }
            $("#logs").append(str);
        },
        fail: function(err) {
            console.log(err);
        },
        contentType: "application/json"
    })
}

function listAllTrains() {
    let url = "/railway_station_service_war_exploded/services/manager/secured/listOfTrains";

    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + getCookie()
        }
    });

    $.ajax({
        type: "GET",
        url: url,
        success: function (data) {
            console.log(data);
            createListOfTrains(JSON.parse(data));
        },
    });
}

function logging(){
    if( document.getElementById("startLogs").checked === true){
        readTextFile();
    } else{
        $("#logs").html("");
    }
}

$(document).ready(function () {
    // getUserData();

    getProfile();
    ListOfEmployees();
    $("#notifyAll").on('click', function () {
        notify();
    });
    $("#makePayment").on('click', function () {
        ListOfEmployees();
    });
    $("#paycheck").on('click', function () {
        getAllPaychecks();
    });
    $("#look-agent").on('click', function () {
        ListOfEmployees();
    })
    $("#look-routes").on('click', function () {
        listAllTrains();
    })
});