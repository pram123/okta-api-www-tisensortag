# okta-api-www-tisensortag

## Introduction
   This is the front-end to the POC that I built for demonstrating Okta/IoT/Mobile platform. Note, this is the POC - and this application needs the ti-sensor application in conjunction.
   We will capture basic sensor data (such as barometer,tem, gyro and mag data) from the TISensorTag.
   
## Author
    Prashanth Ram (pram123 at gmail)   

## Build instructions

Follow the steps [here](https://github.com/okta/okta-spring-boot) to configure the Springboot application.

Make the following changes to compile and run the application
  #### HTML file
   * In login.html
        - `  var orgUrl       = 'https://prdemo.oktapreview.com'; // TODO - Change this for usage with your org`
        - ` var authServer   = '/oauth2/ausca4x6gjdoZn9u60h7';              // TODO - Change this to your authorization server`
        - ` var redirect = 'http://localhost:8080/dashboard.html';          // TODO - Change this to where the access token should be sent`

  #### Java/Springboot application
    * In application.properties
    
    * Additional info:
        - Swagger documentation can be located here: http://<url>/swagger-ui.html#/user-controller    
## 