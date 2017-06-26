jQuery(function($) {
    var msgAnimateTime = 150;
    var msgShowTime = 2000;
    var errorIconClass = "glyphicon glyphicon-remove form-control-feedback";
    var successIconClass = "glyphicon glyphicon-ok form-control-feedback";
    var defaultMessage = "Please give valid input for the incorrect fields.";
    
    $("form [required]").blur(function() {
    	var id = '#' + this.id;
    	if(!$(id).val()) {
    		$(id + '-feedback').addClass(errorIconClass);
    		showError(this.id.replace(/([A-Z])/g, ' $1') + " is required");
    	} else {
    		$(id + '-feedback').removeClass(errorIconClass);
    	}
    });
    
    $("form [pattern]").blur(function() {
    	var id = '#' + this.id;
    	if(!$(id)[0].checkValidity()) {
    		$(id + '-feedback').addClass(errorIconClass);
    		showError(this.id.replace(/([A-Z])/g, ' $1') + " did not match the requirement given.");
    	} else {
    		$(id + '-feedback').removeClass(errorIconClass);
    	}
    });

    
    function msgFade(messageId, messageText) {
        messageId.fadeOut(msgAnimateTime, function() {
            $(this).text(messageText).fadeIn(msgAnimateTime);
        });
    }
    
    function showMessage(divClass, iconClass, messageText) {
    	var divTag = $('#message');
    	var iconTag = $('#message-icon');
    	var textTag = $('#message-text');
        var oldMessage = defaultMessage;
        msgFade(textTag, messageText);
        divTag.addClass(divClass);
        iconTag.removeClass("glyphicon-chevron-right");
        iconTag.addClass(iconClass + " " + divClass);
        setTimeout(function() {
            msgFade(textTag, oldMessage);
            divTag.removeClass(divClass);
            iconTag.addClass("glyphicon-chevron-right");
            iconTag.removeClass(iconClass + " " + divClass);
  		}, msgShowTime);
    }
    
    function showError(message) {
    	showMessage("error", "glyphicon-remove", message);
    }
    
    function showSuccess(message) {
    	showMessage("success", "glyphicon-ok", message);
    }
});