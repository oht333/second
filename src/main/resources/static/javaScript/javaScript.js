/**
 * 
 */

 
 /* list*/
$(function(){
	$("#boardtitle").click(function(){
		location.href = '/board/' + $(this).children(".boardNo").text();
	})
})