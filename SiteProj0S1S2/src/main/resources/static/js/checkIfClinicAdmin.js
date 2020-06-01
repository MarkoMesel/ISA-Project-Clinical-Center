if(localStorage.getItem('token')==null){
	window.location.href = "../whatAreYou";
}
else {
	var role = '';
	$.ajax({
		type : 'GET',
		url : "/getDoctorProfile",
		dataType : "json",
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			role = successData.role;
			if(role == "DOCTOR") {
				window.location.href = "../doctorHome";
			} else {
				window.location.href = "../whatAreYou";
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			} else {
				$.ajax({
					type : 'GET',
					url : "/getProfile",
					dataType : "json",
					headers:{
						'token':localStorage.getItem('token')
					},
					success : function(successData) {
						role = successData.role;
						if(role == "USER") {
							window.location.href = "../home";
						} else {
							window.location.href = "../whatAreYou";
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						if(textStatus=="401"){			
							window.location.href = "../whatAreYou";
						}
					}
				});
			}
		}
	});
}