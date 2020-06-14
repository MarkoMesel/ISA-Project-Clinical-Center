var url = document.URL;
var mcId = url.substring(url.lastIndexOf('/') + 1);

$(document).on('submit', '*[name="sendMedicalCheckupRequestForm"]',function(e){
	e.preventDefault();
	const form = document.forms['sendMedicalCheckupRequestForm'];
	var notes = document.getElementById("notes").value;
	$.ajax({
		type : 'PUT',
		url : "/saveEndNotes",
		contentType : 'application/json',	
		headers:{
			'token':localStorage.getItem('token'),
			'mcId':mcId,
			'notes':notes
		},
		success : function(successData) {
			const data = formToJSON(form.elements);
			
			$.ajax({
				type : 'POST',
				url : "/saveNotesAndSendMedicalCheckupRequest",
				contentType : 'application/json',
				data : data,	
				headers:{
					'token':localStorage.getItem('token')
				},
				success : function(successData) {
					//window.location.href = "../clinicManager";	
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					if(textStatus=="401"){			
						window.location.href = "../clinicBasicInfo";
					}
					else{
						//window.location.href = "../registerMedicalCheckup";				
					}
				}
			});		
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../doctorHome";
			}
			else{
				window.location.href = "../beginMedicalCheckup/"+mcId;			
			}
		}
	});	
});

$("#saveNotesBtn").click(function() {
	const form = document.forms['sendMedicalCheckupRequestForm'];
	//const data = formToJSON(form.elements);
	var notes = document.getElementById("notes").value;
	$.ajax({
		type : 'PUT',
		url : "/saveEndNotes",
		contentType : 'application/json',	
		headers:{
			'token':localStorage.getItem('token'),
			'mcId':mcId,
			'notes':notes
		},
		success : function(successData) {
			//window.location.href = "../doctorHome";	
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(textStatus=="401"){			
				window.location.href = "../doctorHome";
			}
			else{
				window.location.href = "../beginMedicalCheckup/"+mcId;			
			}
		}
	});	
});

function formToJSON(form) {
	return JSON.stringify({
		'id' : mcId,
		'date': form['date'].value,
		'time': form['time'].value,
		'notes' : form['notes'].value,
	});
}

function getParsedDataFromTable(tableIdString) {
	var selectedData = $(tableIdString).tabulator("getSelectedData");
	var dataTest = JSON.stringify(selectedData[0]);
	var parsedData = JSON.parse(dataTest);
	return parsedData;
}