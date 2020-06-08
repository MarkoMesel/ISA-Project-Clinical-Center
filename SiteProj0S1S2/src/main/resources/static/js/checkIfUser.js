if(localStorage.getItem('token')==null){
	window.location.href = "../whatAreYou";
}
else {
	var role = '';
	var verified = '';
	$.ajax({
		type : 'GET',
		url : "/getClinicAdminProfile",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			role = successData.role;
			if(role != "USER") {
				localStorage.removeItem('token');
				window.location.href = "../whatAreYou";
			}
			verified = successData.verified;
			if(verified == false) {
				localStorage.removeItem('token');
				window.location.href = "../notVerified";
			}
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