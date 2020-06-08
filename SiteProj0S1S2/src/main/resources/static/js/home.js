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
			if(role == "CLINICADMIN") {
				window.location.href = "../clinicAdminHome";
			} else {
				localStorage.removeItem('token');
				window.location.href = "../whatAreYou";
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../whatAreYou";
			} else {
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
							localStorage.removeItem('token');
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
									verified = successData.verified;
									if(verified == false) {
										localStorage.removeItem('token');
										window.location.href = "../notVerified";
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
		}
	});
}

$("#logout").click(function() {
	localStorage.removeItem('token');
	window.location.href = "../whatAreYou";
});