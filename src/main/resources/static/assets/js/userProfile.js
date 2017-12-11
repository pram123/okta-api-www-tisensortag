function init(){
    //everything we need should be in the jwt!
    // lets just decode the jwt and put info where it belongs!

    var idToken = localStorage.getItem("idToken");
    console.log("idToken:", idToken);

    var decoded = jwt_decode(idToken);
    console.log("Name = ", decoded);
    $('#userFirstAndLastName').html(decoded.honorificPrefix + " " + decoded.name + " " +decoded.family_name );
    $('#userFirstName').val(decoded.name);
    $('#lastName').val(decoded.family_name);
    $('#userName').val(decoded.preferred_username);
    $('#lastName').val(decoded.family_name);
    $('#email').val(decoded.preferred_username);
    $('#myInfo').html( decoded.title + "<br>"+ decoded.mobilePhone + "<br/>"+ decoded.streetAddress);
    $('#city_state').val(( decoded.city + "/"  + decoded.state));
    $('#zipCode').val(decoded.zipCode);
    $('#aboutMe').val(decoded.aboutMe);
    $('#company').val(decoded.organization);
    $('#country').val(decoded.countryCode);
    $("#userProfileImg").attr("src", decoded.profile)

    console.log(decoded);

}