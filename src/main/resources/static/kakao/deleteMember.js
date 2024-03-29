$().ready(function() {
                 const url = new URL(window.location.href);
                 const urlParams = url.searchParams;
                 const u = urlParams.get('u');

                 jQuery.ajax({
                         type : "DELETE",
                         url : `./kakao-member/${u}`,
                         data : null,
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


})
