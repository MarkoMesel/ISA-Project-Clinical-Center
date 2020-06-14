

if(localStorage.getItem('token')==null){
	window.location.href = "../whatAreYou";
}
else {
	$.ajax({
		type : 'GET',
		url : "/getDoctorProfile",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			dId = successData.id;
			$("#doctorId").val(dId);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			} else {
				window.location.href = "../notAuthorized";
			}
		}
	});
}
