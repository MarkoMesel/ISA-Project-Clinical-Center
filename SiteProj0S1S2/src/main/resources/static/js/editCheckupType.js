var url = document.URL;
var ctId = url.substring(url.lastIndexOf('/') + 1);

var name = "";
var price = "";

$.ajax({
	type : 'GET',
	url : "/getCtInfo/"+ctId,
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		name = successData.name;
		price = successData.price;
		$("#name").val(name);
		$("#price").val(price);
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){	
			window.location.href = "../whatAreYou";
		}
		window.location.href = "../checkupTypeManager";
	}
});

$(document).on('submit', '*[name="editCtForm"]',function(e){
	e.preventDefault();
	const form = document.forms['editCtForm'];
	const data = formToJSON(form.elements);
	
	$.ajax({
		type : 'PUT',
		url : "/editCtInfo",
		contentType : 'application/json',
		data : data,	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			window.location.href = "../checkupTypeManager";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../editCt/"+ctId;
			}
			else{
				window.location.href = "../editCt/"+ctId;				
			}
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({
		'id' : ctId,
		'name': form['name'].value,
		'price': form['price'].value,		
	});
}


/*
var clinicName = '';
var clinicDescription = '';
var clinicLocation = '';
$.ajax({
	type : 'GET',
	url : "/getClinicInfo",
	dataType : "json",
	headers:{
		'token':localStorage.getItem('token')
	},
	success : function(successData) {
		clinicName = successData.name;
		clinicDescription = successData.description;
		clinicLocation = successData.address;
		$("#name").val(clinicName);
		$("#description").val(clinicDescription);
		$("#address").val(clinicLocation);
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		if(textStatus=="401"){	
			window.location.href = "../whatAreYou";
		}
	}
});

$(document).on('submit', '*[name="editClinicForm"]',function(e){
	e.preventDefault();
	const form = document.forms['editClinicForm'];
	const data = formToJSON(form.elements);
	
	$.ajax({
		type : 'PUT',
		url : "/editClinicLocation",
		contentType : 'application/json',
		data : data,	
		headers:{
			'token':localStorage.getItem('token')
		},
		success : function(successData) {
			window.location.href = "../clinicManager";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../clinicLocation";
			}
			else{
				window.location.href = "../clinicLocation";				
			}
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({				
		'name': form['name'].value,
		'description': form['description'].value,
		'address': form['address'].value,
		
	});
}
*/