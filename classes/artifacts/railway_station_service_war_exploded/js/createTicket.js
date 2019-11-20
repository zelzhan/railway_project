$(document).ready(function () {
    getProfile();
    $("#search-route").on('click', function() {
        showTickets("createTicket");
    });
});