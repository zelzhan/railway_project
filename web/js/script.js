
function updateRoute(items) {
	$("#table-table").html("");
	$("#table-table").append("<div class=\"train-table\"><div class=\"container\"><table class=\"table table-dark\"><thead>");
	$("#table-table").append("<tr><th scope=\"col\">#</th><th scope=\"col\">Departure</th><th scope=\"col\">Destination</th>");
	$("#table-table").append("<th scope=\"col\">Departure Time</th><th scope=\"col\">Destination time</th><th scope=\"col\">Date</th></tr></thead><tbody>");
    
    items.routes.forEach(function (e, i) {
    	$("#table-table").append("<tr><th scope=\"row\">"+e.train_id+"</th><td>"+ e.departure +"</td><td>"+e.destination+"</td><td>");
    	$("#table-table").append(e.dep_time+"</td><td>"+e.des_time+"</td><td>"+e.date+"</td></tr>");
		});
	
	$("#table-table").append("</tbody></table></div></div>");

}

function sendFormRoute() {
	var depToSend = $("#dep-input").val().toString();
	var desToSend = $("#des-input").val().toString();
	
	if(depToSend.match("\\w+") && desToSend.match("\\w+")) {
		$.post("services/items/"+ "{"+depToSend+"}/{"+desToSend+"}/{"+$("#day-input").val().toString()+":"
	+$("#month-input").val().toString()+":"+$("#year-input").val().toString()+"}", function() {
			getRouteItems();
		})
	} else {
		window.alert("Please, check your input");
	}
}

 function getRouteItems() {
            $.ajax({
                url : 'services/items/',
                dataType : 'json',
                success : function(r) {
                    updateRoute(r);
                }
            });
        }


$(document).ready(function() {
			$("#search-route").on('click', function() {
				console.log('asd1');
				sendFormRoute();
			})
		});