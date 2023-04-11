$().ready(function() {
    Swal.fire({
       title: '위치정보를 제공하사겠습니까?',
       text: '제공해주신 위치정보는 날씨데이터를 불러오는 데 사용됩니다.',
       icon: 'info',
       width: "90%",
       hight: "50%",
       showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
       confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
       cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
       confirmButtonText: '승인', // confirm 버튼 텍스트 지정
       cancelButtonText: '취소', // cancel 버튼 텍스트 지정
       allowOutsideClick: false,
       reverseButtons: false, // 버튼 순서 거꾸로

    }).then(result => {
       // 만약 Promise리턴을 받으면,
       if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
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
       }else{
         location.href = "https://plus.kakao.com/talk/bot/@pointman_dev/위치정보 동의 실패";
       }
    });
})
