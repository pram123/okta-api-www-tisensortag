var metricsURL="https://sensorapi.ngrok.io/rest/admin/user/map";


function init(){
    console.log("Init admin");

    $.get( metricsURL , function() {
    }).success(function(data) {
        console.log("Data from url:", data);
        updateAllMetrics(data)
    });
}

function updateAllMetrics(data){
    // lets update the numbers
    //totalRegisteredDevices
    uniqUID = _.uniqBy(data, function (e) {
        return e.userID;
    });
    uniqDID = _.uniqBy(data, function (e) {
        return e.deviceID;
    });

   // console.log("unqi data = ", uniqID);
    $('#totalRegisteredDevices').html(uniqDID.length);
    $('#totalRegisteredUsers').html(uniqUID.length);
    populateTable(data);
}

function populateTable(data){


    $('#userTableMap').DataTable( {
        "aaData": data,
        "aoColumns": [
            { "mDataProp": "userID" },
            { "mDataProp": "deviceID" },
        ],
    } );

    // Activate an inline edit on click of a table cell
    $('#userTableMap').on( 'click', 'tbody td:not(:first-child)', function (e) {
        editor.inline( this );
    } );


}