/**
 * groupdocs.annotation Plugin
 * Copyright (c) 2018 Aspose Pty Ltd
 * Licensed under MIT.
 * @author Aspose Pty Ltd
 * @version 1.0.0
 */

 $(document).ready(function(){
	
	//////////////////////////////////////////////////
    // delete text field click event
    //////////////////////////////////////////////////  
	$('#gd-panzoom').on('click', '.gd-text-field-delete', function(e){
		var id = parseInt($(this.parentElement.parentElement).find(".annotation").attr("id").replace ( /[^\d.]/g, '' ));
		var annotationToRemove = $.grep(annotationsList, function(obj){return obj.id === id;})[0];	
		annotationsList.splice($.inArray(annotationToRemove, annotationsList),1);
		$(".gd-annotation").each(function(index, element){
			if(!$(element).hasClass("svg")){
				if(parseInt($(element).find(".annotation").attr("id").replace( /[^\d.]/g, '' )) == id){
					$(element).remove();
				} else {
					return true;
				}
			}
		});	
	});

	//////////////////////////////////////////////////
    // change font family or font size event
    //////////////////////////////////////////////////  
	$("#gd-panzoom").on("input", ".gd-typewriter-font, .gd-typewriter-font-size", function(e){
		var textArea = $(this.parentElement.parentElement).find("textarea")[0];
		var id = parseInt($(this.parentElement.parentElement).find(".annotation").attr("id").replace ( /[^\d.]/g, '' ));
		var font = "Arial";
		var fontSize = 10;
		var currentAnnotation = $.grep(annotationsList, function(e){ return e.id == id; });
		if($(this).attr("class") == "gd-typewriter-font"){
			currentAnnotation[0].font = this.value;
			$(textArea).css("font-family", currentAnnotation[0].font);
			
		} else {
			currentAnnotation[0].fontSize = parseInt(this.value);
			$(textArea).css("font-size", currentAnnotation[0].fontSize);
		}
	});	

	//////////////////////////////////////////////////
    // resize text area event
    //////////////////////////////////////////////////  
	$("#gd-panzoom").bind("mouseup", ".gd-typewriter-text",  function(event){
		var id = $(event.target).data("id");
		$.each(annotationsList, function(index, elem){
			if(elem.id == id){
				elem.width = $(event.target).width();
				elem.height = $(event.target).height();			
			} else {
				return true;
			}
		});			
	});

	//////////////////////////////////////////////////
    // enter text event
    //////////////////////////////////////////////////  
	$("#gd-panzoom").bind("keyup", ".gd-typewriter-text",  function(event){
		var id = $(event.target).data("id");
		$.each(annotationsList, function(index, elem){
			if(elem.id == id){
				elem.text = $(event.target).val();					
			} else {
				return true;
			}
		});			
	});
 });
 
(function( $ ) {

	/**
	* Create private variables.
	**/
	var mouse = {
        x: 0,
        y: 0       
    };
	var zoomCorrection = {
		x: 0,
		y: 0
	};
	var startX = 0;
	var startY = 0;
    var element = null;
	var annotationInnerHtml = null;
	var currentPrefix = "";
	var idNumber = null;
	
	/**
	 * Draw field annotation	
	 */
	$.fn.drawFieldAnnotation = function() {
		
	}
	
	/**
	* Extend plugin
	**/
	$.extend(true, $.fn.drawFieldAnnotation, {
		/**
		 * Draw text field annotation
		 * @param {Object} canvas - document page to add annotation
		 * @param {Array} annotationsList - List of all annotations
		 * @param {Object} annotation - Current annotation
		 * @param {int} annotationsCounter - Current annotation number
		 * @param {Onject} ev - Current event
		 */
		drawTextField: function(canvas, annotationsList, annotation, annotationsCounter, prefix, ev) {	
			event.stopPropagation();
			$('#gd-annotations-comments-toggle').prop('checked', false);
			mouse = getMousePosition(ev);
			currentPrefix = prefix;
			idNumber = annotationsCounter;
			var canvasTopOffset = $(canvas).offset().top * $(canvas).css("zoom");
			var x = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
			var y = mouse.y - canvasTopOffset - (parseInt($(canvas).css("margin-top")) * 2);
			zoomCorrection.x = ($(canvas).offset().left * $(canvas).css("zoom")) - $(canvas).offset().left;
			zoomCorrection.y = ($(canvas).offset().top * $(canvas).css("zoom")) - $(canvas).offset().top;
			annotation.id = annotationsCounter;				
			startX = mouse.x;
			startY = mouse.y;
			element = document.createElement('div');
			element.className = 'gd-annotation';  			
			element.innerHTML = getTextFieldAnnotationHtml(annotationsCounter);	
			var canvasTopOffset = $(canvas).offset().top * $(canvas).css("zoom");
			element.style.left = x + "px";
			element.style.top = y + "px";
				
			canvas.prepend(element);	
			$(".gd-typewriter-text").click(function (e) {
				e.stopPropagation()
				$(this).focus();
			})			
			element.style.top = parseInt(element.style.top) - parseInt(($(element).find(".gd-text-area-toolbar").height() + $(element).find(".gd-text-area-toolbar").css("margin-bottom"))) + "px";
			annotation.width = $(element).find("textarea").width();
			annotation.height = $(element).find("textarea").height();	
			annotation.left = parseInt(element.style.left.replace("px", ""));
			var dashboardHeight = parseInt($(element).find(".gd-text-area-toolbar").css("margin-bottom")) + parseInt($(element).find(".gd-text-area-toolbar").css("height"));
			annotation.top = parseInt(element.style.top.replace("px", "")) + dashboardHeight;			
			annotationsList.push(annotation);	
			makeResizable(annotation);			
		} 
	});
	
	function getTextFieldAnnotationHtml(id){
		var annotationToolBarHtml = '<div class="gd-text-area-toolbar">'+									
										'<input type="text" class="gd-typewriter-font" value="Arial">'+
										'<input type="number" value="10" class="gd-typewriter-font-size">'+
										'<i class="fa fa-trash-o gd-text-field-delete"></i>'+
									'</div>';
		var annotationTextFieldHtml = '<textarea class="gd-typewriter-text mousetrap annotation" id="gd-' + currentPrefix + '-annotation-' + idNumber + '" data-id="' + idNumber + '">Enter annotation text here</textarea>';
		return annotationToolBarHtml + annotationTextFieldHtml;
	}
})(jQuery);