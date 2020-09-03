if(localStorage.getItem('token')==null){
	window.location.href = "../whatAreYou";
}
else {
	var role = '';
	var verified = '';
	var firstLogin = '';
	$.ajax({
		type : 'GET',
		url : "/getDoctorProfile",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			role = successData.role;
			if(role != "DOCTOR") {
				localStorage.removeItem('token');
				window.location.href = "../whatAreYou";
			}
			verified = successData.verified;
			if(verified == false) {
				localStorage.removeItem('token');
				window.location.href = "../notVerified";
			}
			firstLogin = successData.firstLogin;
			if(firstLogin == true) {
				window.location.href = "../changeDoctorPassword";
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
