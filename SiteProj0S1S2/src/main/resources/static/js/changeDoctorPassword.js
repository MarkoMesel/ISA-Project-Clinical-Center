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
		url : "/changeDoctorPassword",
		contentType : 'application/json',
		data : data,	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			//window.location.replace("../editProfile?success");
			window.location.href = "../doctorHome";
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../doctorLogin";
			}
			else{
				window.location.replace("../changeDoctorPassword?passwordFormatError");				
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