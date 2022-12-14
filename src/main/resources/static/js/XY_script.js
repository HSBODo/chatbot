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
                console.log(pos);
                var latitude = pos.coords.latitude;
                var longitude = pos.coords.longitude;
                let data =  JSON.stringify({"latitude":latitude,"longitude":longitude})

            jQuery.ajax({
                    type : "POST",
                    url : "./weatherInfo",
                    data : data,
                    cache: false,
                    dataType : "json",
                    beforeSend: function (xhr) {
                               xhr.setRequestHeader("Content-type","application/json");
                           },
                    success : function(data) {
                        console.log("성공")
                        console.log(data)
                    },
                    error : function(e) {
                               console.log("실패")
                    }, timeout:100000
                });

            });
       }
    });
})

//	, function (isConfirm) {
//		if (!isConfirm) return;
//
//		jQuery.ajax({
//			type : "POST",
//			url : "",
//			data : "",
//			cache: false,
//			dataType : "json",
//			success : function(data) {
//				swal("성공", "작업을 정상적으로 완료하였습니다.", "success");
//			},
//			error : function(e) {
//				swal("실패", "작업수행에 실패하였습니다.", "error");
//			}, timeout:100000
//		});
//	});
//});
//if(confirm("위치정보 제공에 동의하시겠습니까?")){
//    navigator.geolocation.getCurrentPosition(function(pos) {
//        console.log(pos);
//        var latitude = pos.coords.latitude;
//        var longitude = pos.coords.longitude;
//        alert("현재 위치는 : " + latitude + ", "+ longitude);
//    });
//}
