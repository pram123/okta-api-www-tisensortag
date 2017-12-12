//var APIURL="https://sensorapi.ngrok.io/rest/";
var APIURL="http://localhost:8080/rest/";
var getURL = APIURL + "user/"+ localStorage.getItem("userID")+"/dump/";


//this is temperature info - not tmp object
var tempObject= new Object();
var tempData=[];
var accData=[];
var gyroData = new Object();
var gyroChart, accChart;
var gyroCtx, accCtx;
var chartConfig= {
    type: 'line',
    data: {
        datasets: [{
            label: "X",
            borderColor: "#000000",
            fill: true,
            data: []
        }, {
            label: "Y",
            borderColor: "#271e8c",
            fill: true,
            data: []
        }, {
            label: "Z",
            borderColor: "#1e8c48",
            fill: true,
            data: []
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    min: -270,
                    max: 270,
                    stepSize: 90
                }
            }]
        }
    }
}

function init(){

    initMetrics();
    // lets initialize the charts
     gyroCtx = document.getElementById("deviceGyroData").getContext("2d");
     gyroChart = new Chart(gyroCtx, chartConfig);



    //this.gyroChart = initCanvas('deviceGyroData');
    //initCanvas('deviceGyroData');
 //   updateCanvas()
    //updateGyroCanvas();
    //updateAccData()
}

function initMetrics(){
    idToken = JSON.parse(localStorage.getItem("decodedIDToken"))
    console.log("DeviceID = ", idToken);
    $('#deviceID').html(idToken.deviceID);
    localStorage.setItem("userID", idToken.uid);
    $('#deviceSensors').html(3);
    // lets udpate the # devices
    $('#userRegisteredSensors').html(1);
}

function updateTempData(data){
    tempData =[];
    _.forEach(data, function(aMeasurement) {
        //lets clear what've first
        console.log(aMeasurement);
        tempObject.tempData = aMeasurement.tempInfo;
        tempObject.tempTime = aMeasurement.dataCaptureTime;
        tempData.push(aMeasurement.tempInfo);
    });
}

function updateAllMetrics(){
    console.log("Updating data");

    latestInfoURL = APIURL + "user/"+ localStorage.getItem("userID") + "/"+ $('#deviceID').text()+"/latest/sensorData";

    $.get( latestInfoURL , function() {
    }).success(function(data) {
        console.log("latestData = ", data);
        if (!($.isEmptyObject(data))) {
            deviceNumEvents = parseInt($('#deviceNumEvents').text()) + 1;
            $('#deviceNumEvents').text(deviceNumEvents);
            $('#currentTemp').text(data.envData.tempInfo);
            $('#currentBar').text(data.envData.barInfo);


            addData(gyroChart,-1,data.gyroData)
           // addData(accChart,-1,data.accData)


        } else {
            gyroData.x=0;gyroData.y=0;gyroData.z=0;
            addData(gyroChart,-1,gyroData)
            //accData.x=0;accData.y=0;accData.z=0;
            //addData(accChart,-1,accData)

        }
    });

    // lets get the current gyro Date
    /*gyroURL = APIURL + "user/"+ localStorage.getItem("userID") + "/"+ $('#deviceID').text()+"/latest/gyro/" + currTime;
    console.log("GyroDataURL = ",gyroURL);
    $.get( gyroURL , function() {
    }).success(function(gTmp) {
        console.log("gyroData = ", gTmp);
        var numElements = gTmp.length;  // always display the last element
        //console.log("max lenth =" , gTmp.length);
        //console.log("gyroTmp = ", gTmp[0].gyroData);

        gyroData.time = gTmp[0].dataCaptureTime;
        gyroData.x = gTmp[0].gyroData.x;
        gyroData.y = gTmp[0].gyroData.y;
        gyroData.z = gTmp[0].gyroData.z;
        console.log("GyroData", gyroData);
       //this.gyroArray.push(gyroData);
        //console.log("gyroArray = ", tempData);
        //tmpGyroArray = gyroData;
    });
    */
}

function addData(chart, label, data) {
    console.log("Updating chart for chart ,", chart);
   // initCanvas('deviceGyroData');

    if (chart.data.datasets[0].data.length > 5){
        chart.data.labels.shift();
        chart.data.datasets[0].data.shift();
        chart.data.datasets[1].data.shift();
        chart.data.datasets[2].data.shift();
    }
    chart.data.labels.push(" ");
    chart.data.datasets[0].data.push(data.x);
    chart.data.datasets[1].data.push(data.y);
    chart.data.datasets[2].data.push(data.z);

    chart.update();
    console.log("data " , chart.data.datasets[0]);
}


function getRandomIntInclusive(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min; //The maximum is inclusive and the minimum is inclusive
}

