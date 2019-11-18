function login() {
    $.ajaxSetup({
        headers:{
            'Authorization': "Basic " + $.cookie('encripted')
        }
    });
}
