$().ready(function() {


             navigator.geolocation.getCurrentPosition(function(pos) {
                 var latitude = pos.coords.latitude;
                 var longitude = pos.coords.longitude;
                 console.log(latitude)
                 console.log(longitude)
                 const url = new URL(window.location.href);
                 const urlParams = url.searchParams;
                 const u = urlParams.get('u');
                 let data =  JSON.stringify(
                     {
                         "x":latitude,
                         "y":longitude,
                         "kakaoUserkey":u
                     }
                 )
             jQuery.ajax({
                     type : "POST",
                     url : "./location-agree",
                     data : data,
                     cache: false,
                     dataType : "json",
                     beforeSend: function (xhr) {
                                xhr.setRequestHeader("Content-type","application/json");
                            },
                     success : function(data) {
                     },
                     error : function(e) {
                        alert(e)
                     },
                     complete : function(data) {
                        location.href = data.responseJSON.redirectURL;
                     }

                 });

             });
})
