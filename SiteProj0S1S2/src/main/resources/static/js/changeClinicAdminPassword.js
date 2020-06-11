if (localStorage.getItem('token') == null) {
	window.location.href = "../login";
}

$(document).on('submit', '*[name="editForm"]',function(e){
	e.preventDefault();
	const form = document.forms['editForm'];
	
	/*
	if(form.elements['password'].value!=form.elements['confirmPassword'].value){
		window.location.replace("../editProfile?passwordMatchError");
		return;

	}
	*/
	
	const data = formToJSON(form.elements);
	$.ajax({
		type : 'PUT',
		url : "/changeClinicAdminPassword",
		contentType : 'application/json',
		data : data,	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			//window.location.replace("../editProfile?success");
			window.location.href = "../clinicAdminHome";
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../clinicAdminLogin";
			}
			else{
				window.location.replace("../changeClinicAdminPassword?passwordFormatError");				
			}
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({		
		'oldPassword': form['oldPassword'].value,	
		'password': form['password'].value,	
	});
}