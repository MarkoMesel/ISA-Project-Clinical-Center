if (localStorage.getItem('token') == null) {
	window.location.href = "../login";
}

$.ajax({
	type : 'GET',
	url : "/getDoctorProfile",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		const form = document.forms['editForm'];
		form.elements['firstName'].value = successData.firstName;
		form.elements['lastName'].value = successData.lastName;
		form.elements['street'].value = successData.street;
		form.elements['city'].value = successData.city;
		form.elements['country'].value = successData.country;
		form.elements['phone'].value = successData.phone;
		form.elements['jmbg'].value = successData.jmbg;
		form.elements['email'].value = successData.email;
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){			
			window.location.href = "../login";
		}
	}
});	

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
		url : "/editDoctorProfile",
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
				window.location.replace("../editProfile?passwordFormatError");				
			}
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({				
		'firstName': form['firstName'].value,
		'lastName': form['lastName'].value,
		'street': form['street'].value,
		'city': form['city'].value,
		'country': form['country'].value,
		'phone': form['phone'].value,
		'jmbg': form['jmbg'].value,
		'email': form['email'].value,
		
	});
}