/**
 * 
 */
 
 /* list*/
$(function(){
	$("#boardtitle").click(function(){
		location.href = '/board/' + $(this).children(".boardNo").text();
	})
	
/* enroll*/
	$('#memId').blur(function(){
		let enrollMemId = $('#memId').val();
		let letterExp = /^[a-z\d]{5,20}$/;
		
		if(!letterExp.test($enrollMemId)){
			$('#id-feedback').val("5-20자의 영대문자 및 소문자와 숫자만 가능합니다.");
			$('#memId').val("");
			return false;
		}
		else if(letterExp.test($enrollMemId)){
			$('#id-feedback').val("");
			$.ajax({
				url : {memId : $enrollMemId},
				type : "post",
				contentType : "application/x-www-form-urlencoded",
				success : function(data){
					if(data.data === 0){
						
					}
				}
			})
			return true;
		}
	})
})