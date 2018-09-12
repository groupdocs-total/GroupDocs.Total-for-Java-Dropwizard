/**
 * groupdocs.annotation Plugin
 * Copyright (c) 2018 Aspose Pty Ltd
 * Licensed under MIT.
 * @author Aspose Pty Ltd
 * @version 1.0.0
 */
 $(document).ready(function(){
	 //////////////////////////////////////////////////
    // enter text event
    //////////////////////////////////////////////////  
	$("#gd-panzoom").bind("keyup", ".gd-replace-text-area-text",  function(event){
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
	
	var startX = 0;
	var startY = 0;
    var element = null;
	var annotationInnerHtml = null;	
	var lineInnerHtml = null;
	var currentPrefix = "";
	var idNumber = null;
	
	/**
	 * Draw text annotation
	 * @param {Object} canvas - document page to add annotation
	 * @param {Array} annotationsList - List of all annotations
	 * @param {Object} annotation - Current annotation
	 * @param {int} annotationsCounter - Current annotation number
	 * @param {Onject} ev - Current event
	 */
	$.fn.drawTextAnnotation = function(canvas, annotationsList, annotation, annotationsCounter, prefix, ev) {		
		currentPrefix = prefix;
		idNumber = annotationsCounter;		
		mouse = getMousePosition(ev)
		if(element == null && ($(ev.target).prop("tagName") == "IMG" || $(ev.target).prop("tagName") == "svg")){			
			annotation.id = idNumber;	
			canvas.style.cursor = "crosshair";
			startX = mouse.x;
			startY = mouse.y;
			element = document.createElement('div');
			element.className = 'gd-annotation';      
			element.innerHTML = getHtmlResizeHandles();			
			
			var canvasTopOffset = $(canvas).offset().top * $(canvas).css("zoom");
			var x = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
			var y = mouse.y - (canvasTopOffset + (parseInt($(canvas).css("margin-top")) * 2));
			switch (currentPrefix){
				case "text":					
					var startCoordinates = setTextAnnotationCoordinates(x, y - 2);
					element.style.left = startCoordinates.x + "px";
					element.style.top = startCoordinates.y + "px";
					element.style.height = startCoordinates.height + "px";
					annotationInnerHtml = getAnnotationHtml();
					break
				case "area":
					element.style.left = x + "px";
					element.style.top = y + "px";
					annotationInnerHtml = getAnnotationHtml();
					break;
				case "textStrikeout":
					var startCoordinates = setTextAnnotationCoordinates(x, y);
					element.style.left = startCoordinates.x + "px";
					element.style.top = startCoordinates.y + "px";
					element.style.height = startCoordinates.height + "px";
					annotationInnerHtml = getTextLineAnnotationHtml();
					lineInnerHtml = getLineHtml();
					break
				case "textReplacement":
					var startCoordinates = setTextAnnotationCoordinates(x, y);
					element.style.left = startCoordinates.x + "px";
					element.style.top = startCoordinates.y + "px";
					element.style.height = startCoordinates.height + "px";
					annotationInnerHtml = getAnnotationHtml();
					break;		
				case "textRedaction":
					var startCoordinates = setTextAnnotationCoordinates(x, y);
					element.style.left = startCoordinates.x + "px";
					element.style.top = startCoordinates.y + "px";
					element.style.height = startCoordinates.height + "px";
					annotationInnerHtml = getAnnotationHtml();
					break;	
				case "resourcesRedaction":
					element.style.left = x + "px";
					element.style.top = y + "px";
					annotationInnerHtml = getAnnotationHtml();
					break;	
				case "textUnderline":
					var startCoordinates = setTextAnnotationCoordinates(x, y);
					element.style.left = startCoordinates.x + "px";
					element.style.top = startCoordinates.y + "px";
					element.style.height = startCoordinates.height + "px";
					annotationInnerHtml = getTextLineAnnotationHtml();
					lineInnerHtml = getLineHtml();
					break					
			}
			
			annotation.left = parseFloat(element.style.left.replace("px", ""));
			annotation.top = parseFloat(element.style.top.replace("px", ""));	
			
			element.appendChild(annotationInnerHtml);	
			if(lineInnerHtml != null) {
				element.appendChild(lineInnerHtml);					
			}
			canvas.prepend(element);					
			annotationsList.push(annotation);				
			makeResizable(annotation);				
			addComment(annotation);
		} else {			
			canvas.onmousemove = null;			 		
			canvas.style.cursor = "default";
			if($(ev.target).prop("tagName") == "IMG"){					
				annotationsList[annotationsList.length - 1].width = parseFloat(element.style.width.replace("px", ""));
				annotationsList[annotationsList.length - 1].height = parseFloat(element.style.height.replace("px", ""));					
			}	
			element = null;				
			annotationInnerHtml = null;	
			lineInnerHtml = null;			
		}		
				 
		canvas.onmouseup = function(e) {
			if(element != null && e.target.tagName != "TEXTAREA"){
				if(currentPrefix == "textReplacement"){
					element.appendChild(getTextReplaceAnnotationHtml(idNumber));			
				}
				canvas.onmousemove = null;
				canvas.onmouseup = null;				
				canvas.style.cursor = "default";
				if($(ev.target).prop("tagName") == "IMG"){					
					annotationsList[annotationsList.length - 1].width = parseFloat(element.style.width.replace("px", ""));
					annotationsList[annotationsList.length - 1].height = parseFloat(element.style.height.replace("px", ""));					
				}			
				annotationInnerHtml = null;	
				lineInnerHtml = null;
				element = null;
			}
		}
		
		canvas.onmousemove = function (e) {      
			mouse = getMousePosition(e);
			if (element !== null) {
				if(currentPrefix == "text"){					
					element.style.width = Math.abs(mouse.x - startX) + "px";				
				} else {
					element.style.width = Math.abs(mouse.x - startX) + "px";	
					element.style.height = Math.abs(mouse.y - startY) + "px";						
				}
				annotation.width = parseFloat(element.style.width.replace("px", ""));
				annotation.height = parseFloat(element.style.height.replace("px", ""));
				annotationInnerHtml.style.width = parseFloat(element.style.width) + "px";
				annotationInnerHtml.style.height = parseFloat(element.style.height) + "px";				
			}
		}    
		
		function getAnnotationHtml(){
			var annotationHtml = document.createElement('div');
			annotationHtml.className = 'gd-' + currentPrefix + '-annotation annotation';
			annotationHtml.id = 'gd-' + currentPrefix + '-annotation-' + idNumber; 
			return annotationHtml;
		}		
						
		function getTextReplaceAnnotationHtml(){		
			var annotationHtml = document.createElement('div');			
			annotationHtml.className = "gd-replace-text-area annotation";
			annotationHtml.id = 'gd-' + currentPrefix + '-annotation-' + idNumber; 
			annotationHtml.innerHTML = '<div class="gd-replace-tab">Replace</div>'+
										'<textarea class="gd-replace-text-area-text mousetrap" data-id="' + idNumber + '">replace text</textarea>';
										
			return annotationHtml;
		}	
			
		function getTextLineAnnotationHtml(){
			var annotationHtml = document.createElement('div');
			annotationHtml.className = 'gd-' + currentPrefix + '-annotation gd-text-annotation annotation';
			annotationHtml.id = 'gd-' + currentPrefix + '-annotation-' + idNumber; 			
			return annotationHtml;
		}
		
		function getLineHtml(){
			var annotationHtml = document.createElement('div');
			annotationHtml.className = 'gd-' + currentPrefix + '-line';			
			return annotationHtml;			
		}
	}
})(jQuery);