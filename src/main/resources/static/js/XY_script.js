if(confirm("위치정보 제공에 동의하시겠습니까?")){
    navigator.geolocation.getCurrentPosition(function(pos) {
        console.log(pos);
        var latitude = pos.coords.latitude;
        var longitude = pos.coords.longitude;
        alert("현재 위치는 : " + latitude + ", "+ longitude);
    });
}
