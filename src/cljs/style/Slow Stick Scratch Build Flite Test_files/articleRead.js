$().ready(function(){
	if ( _id ){
		setTimeout(function(){
			$.post("/me/readArticle",{article_id:articleId});
		},3000);
	}

	setTimeout(function(){
        prepCommentForms();
        initCommentActions();
	}, 500);

	/*
	$(".ratingBlock").click(function(){
		var rating = $(this).data('rating');
		if ( _id ){
			$.post("/articles/rate",{article_id:articleId,rating:rating});
		}
	})
	*/

	//start initial comment box
	$("#newComment").html($("#commentFormTemplate").html())
		.find('.response_to, .cancelComment').remove();
	$(".commentForm:first").attr('id','newCommentForm');


})

function initCommentActions(){
	$(".commentReply").unbind('click').on('click',function(){

        //hide other comments boxes
		$("#comments").find('.commentFormContainer').each(function(){
			// SKIP THE TEMPLATE!
			if( $(this).parents("#commentFormTemplate").length == 0){
                $(this).find('.cancelComment').click();
            }
		})

		responseTo = $(this).parents('.commentContainer').attr('rel');

		container = $(this).parents('.commentContainer:first');
		replies = container.children('.replies');
		container.children('.commentActions:first').hide();
		replies.append($("#commentFormTemplate").html());
		container.find('.response_to').val(responseTo);
		container.find('.commentContent').focus();
		container.find('.commentFormContainer').css('border-left','10px solid #F5F5F5').css('padding-left','20px')
		prepCommentForms();
	})

	$(".removeCommentForm").unbind('submit').on('submit',function(){
		if ( !confirm("Are you sure you want to remove this comment?") ){
			return false;
		}
	})

}

function submitComment(){
	$(this).find('.alert-error').fadeOut(100); //get rid of previous alert

    if ( jQuery.trim($(".commentForm").find('.commentContent').val()) == '' ){
        console.log("empty comment");
        $(this).find('.alert-error').html("<strong>Not So Fast</strong><br />You think we're going to let you submit an empty comment?  Try again.").fadeIn(100);
        $(this).find('.commentContent').focus();
        return false;
    }

	if ( jQuery.trim($(".commentForm").find('.commentContent').val()).indexOf("href") !== -1 ){
        console.log("links in comment");
        $(this).find('.alert-error').html("<strong>Not So Fast</strong><br />HTML links not allowed.  Try again.").fadeIn(100);
        $(this).find('.commentContent').focus();
        return false;
    }

    grecaptcha.execute();
    return false;
}

function submitCaptcha(captchaResponse){

    var commentForm = $(".commentForm").first();
	commentForm.hide().after("<div class='submitting'>Submitting...</div>")

	var form = commentForm;
	if (commentForm.attr('id') == 'newCommentForm'){
		var type = 'newComment';
		var dest = $(".comment-list");
	} else {
		var type = 'reply';
		var parent = commentForm.parents('.commentContainer:first');
		var dest = parent.children('.replies');
		var formContainer = form.parent('.commentFormContainer');
	}
    $("#captcha-response").remove();
	commentForm.append('<input type=hidden id="captcha-response" name="recaptcha" value="'+captchaResponse+'">');
	var data = commentForm.serialize();
	$.post("/articles/submitComment", data, function(response){
		console.log(response);
		$(".submitting").remove();
		$("#noComments").remove();
		if( type == 'reply' ){
			formContainer.hide();
			parent.find('.commentActions').fadeIn(100);
		} else {
			$("#newCommentForm .commentContent").val('');
			$("#newCommentForm").fadeIn(100);
		}
		grecaptcha.reset();
		dest.append("<div class='newComment'>"+response+"</div>");
		$(".newComment").slideDown(100).removeClass('newComment');
		initCommentActions();
	})

	return false;

}

function prepCommentForms(){
	$(".commentForm").unbind('submit').on('submit', submitComment);

	$(".cancelComment").unbind('click').on('click',function(){
		$(this).parents('.commentFormContainer').fadeOut(100,function(){
			$(this).siblings('.commentActions').fadeIn(100);
			$(this).remove()
		})
	})
}
