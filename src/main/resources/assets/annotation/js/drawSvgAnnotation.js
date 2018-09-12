/**
 * groupdocs.annotation Plugin
 * Copyright (c) 2018 Aspose Pty Ltd
 * Licensed under MIT.
 * @author Aspose Pty Ltd
 * @version 1.0.0
 */

(function( $ ) {

	/**
	* Create private variables.
	**/
	var mouse = {
        x: 0,
        y: 0,       
    };
    var element = null;	
	var pointSvgSize = 25;
	var svgCircleRadius = 22;
	
	var zoomCorrection = {
		x: 0,
		y: 0
	};
	
	var canvas = null;
	var currentAnnotation = null;
	var canvasTopOffset = null;
	var currentPrefix = "";
	
	/**
	 * Draw svg annotation	
	 */
	$.fn.drawSvgAnnotation = function(documentPage, prefix) {		
		canvas = documentPage;
		currentAnnotation = annotation;
		zoomCorrection.x = ($(canvas).offset().left * $(canvas).css("zoom")) - $(canvas).offset().left;
		zoomCorrection.y = ($(canvas).offset().top * $(canvas).css("zoom")) - $(canvas).offset().top;	
		canvasTopOffset = $(canvas).offset().top * $(canvas).css("zoom");	
		currentPrefix = prefix;
	}
	
	/**
	* Extend plugin
	**/
	$.extend(true, $.fn.drawSvgAnnotation, {
		
		/**
		* Draw point annotation
		*/
		drawPoint: function(event){			
			mouse = getMousePosition(event);	
			var x = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
			var y = mouse.y - canvasTopOffset - (parseInt($(canvas).css("margin-top")) * 2);	
			currentAnnotation.id = annotationsCounter;	
			currentAnnotation.left = x;
			currentAnnotation.top = y;	
			currentAnnotation.width = pointSvgSize;
			currentAnnotation.height = pointSvgSize;
			annotationsList.push(currentAnnotation);
			addComment(currentAnnotation);	
			var circle = svgList[canvas.id].circle(svgCircleRadius);
			circle.attr({
				'fill': 'red',			
				'stroke': 'black',
				'stroke-width': 2,
				'cx': x,
				'cy': y,
				'id': 'gd-point-annotation-' + annotationsCounter,
				'class': 'gd-annotation annotation svg'
			})			
		},
		
		/**
		* Draw polyline annotation
		*/
		drawPolyline: function(event){
			mouse = getMousePosition(event);	
			var x = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
			var y = mouse.y - canvasTopOffset - (parseInt($(canvas).css("margin-top")) * 2);	
			currentAnnotation.id = annotationsCounter;		
			const option = {
				'stroke': 'red',
				'stroke-width': 2,
				'fill-opacity': 0,
				'id': 'gd-polyline-annotation-' + annotationsCounter,
				'class': 'gd-annotation annotation svg'						  
			}
			let line = null;		
			line = svgList[canvas.id].polyline().attr(option);			
			line.draw(event);					
			svgList[canvas.id].on('mousemove', event => {
			  if (line) {
				line.draw('point', event);
			  }
			})
			svgList[canvas.id].on('mouseup', event => {
				if (line && currentPrefix == "polyline") {											
					line.draw('stop', event);				
					currentAnnotation.left = x;
					currentAnnotation.top = y;	
					currentAnnotation.width = line.width();
					currentAnnotation.height = line.height();
					currentAnnotation.svgPath = "M";	
					var previousX = 0;
					var previousY = 0;
					$.each(line.node.points, function(index, point){										
						if(index == 0){
							currentAnnotation.svgPath = currentAnnotation.svgPath + point.x + "," + point.y + "l";							
							previousX = point.x;
							previousY = point.y;
						} else {				
							previousX =  point.x - previousX;
							previousY =  point.y - previousY;
							currentAnnotation.svgPath = currentAnnotation.svgPath + previousX + "," + previousY + "l";	
							previousX = point.x;
							previousY = point.y;							
						}
					});
					currentAnnotation.svgPath = currentAnnotation.svgPath.slice(0,-1);
					annotationsList.push(currentAnnotation);							
					addComment(currentAnnotation);	
					line = null;
				}
			});		
		},
		
		/**
		* Draw arrow annotation
		*/
		drawArrow: function(event){
			mouse = getMousePosition(event);	
			var x = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
			var y = mouse.y - canvasTopOffset - (parseInt($(canvas).css("margin-top")) * 2);	
			currentAnnotation.id = annotationsCounter;	
			const option = {
				'stroke': 'red',
				'stroke-width': 2,
				'fill-opacity': 0,
				'id': 'gd-arrow-annotation-' + annotationsCounter,
				'class': 'gd-annotation annotation svg'
							  
			}
			let path = null;		 
			path = svgList[canvas.id].path("M" + x + "," + y + " L" + x + "," + y).attr(option);			
						
			svgList[canvas.id].on('mousemove', event => {
				if (path) {
					mouse = getMousePosition(event);
					var endX = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
					var endY = mouse.y - canvasTopOffset - (parseInt($(canvas).css("margin-top")) * 2);
					
					path.plot("M" + x + "," + y + " L" + endX + "," + endY);
					path.marker('end', 20, 20, function(add) {
						var arrow = "M0,7 L0,13 L12,10 z";
						add.path(arrow);
						
						this.fill('red');
					});	
				}
			})
			svgList[canvas.id].on('mouseup', event => {
				if (path && currentPrefix == "arrow") {						
					currentAnnotation.left = x;
					currentAnnotation.top = y;	
					currentAnnotation.width = path.width();
					currentAnnotation.height = path.height();
					
					currentAnnotation.svgPath = path.attr("d");				
					annotationsList.push(currentAnnotation);
					addComment(currentAnnotation);		
					path = null;
				}
			});		
		},
		
		/**
		* Draw distance annotation
		*/
		drawDistance: function(event){
			mouse = getMousePosition(event);	
			var x = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
			var y = mouse.y - canvasTopOffset - (parseInt($(canvas).css("margin-top")) * 2);	
			currentAnnotation.id = annotationsCounter;	
			const option = {
				'stroke': 'red',
				'stroke-width': 2,
				'fill-opacity': 0,
				'id': 'gd-distance-annotation-' + annotationsCounter,
				'class': 'gd-annotation annotation svg'
							  
			};
			const textOptions = {
				'font-size': "10px",
				'data-id': currentAnnotation.id 
			};
			let path = null;		 
			path = svgList[canvas.id].path("M" + x + "," + y + " L" + x + "," + y).attr(option);			
			let text = null;
			text = svgList[canvas.id].text("0px").attr(textOptions);
			svgList[canvas.id].on('mousemove', event => {
				if (path) {
					mouse = getMousePosition(event);
					var endX = mouse.x - ($(canvas).offset().left * $(canvas).css("zoom")) - (parseInt($(canvas).css("margin-left")) * 2);
					var endY = mouse.y - canvasTopOffset - (parseInt($(canvas).css("margin-top")) * 2);
					
					path.plot("M" + x + "," + y + " L" + endX + "," + endY);
					text.path("M" + x + "," + (y - 3) + " L" + endX + "," + (endY - 3)).move(path.width() / 2, y).tspan(Math.round(path.width()) + "px");
					path.marker('start', 20, 20, function(add) {
						var arrow = "M12,7 L12,13 L0,10 z";						
						add.path(arrow);
						add.rect(1, 20).cx(0).fill('red')						
						this.fill('red');
					});						
					path.marker('end', 20, 20, function(add) {
						var arrow = "M0,7 L0,13 L12,10 z";
						add.path(arrow);
						add.rect(1, 20).cx(10).fill('red')
						this.fill('red');
						currentAnnotation.text = path.width() + "px";
					});	
				}
			})
			svgList[canvas.id].on('mouseup', event => {
				if (path) {
					currentAnnotation.left = x;
					currentAnnotation.top = y;	
					currentAnnotation.width = path.width();
					currentAnnotation.height = path.height();
					
					currentAnnotation.svgPath = path.attr("d");				
					annotationsList.push(currentAnnotation);
					addComment(currentAnnotation);		
					path = null;
				}
			});		
		}
	});
	
	// This is custom extension of polyline, which doesn't draw the circle
	SVG.Element.prototype.draw.extend('polyline', {

		init:function(e){
		// When we draw a polygon, we immediately need 2 points.
		// One start-point and one point at the mouse-position

			this.set = new SVG.Set();

			var p = this.startPoint,			
			arr = [
				[p.x - zoomCorrection.x, p.y - zoomCorrection.y],
				[p.x - zoomCorrection.x, p.y - zoomCorrection.y]
			];

			this.el.plot(arr);
		},

		// The calc-function sets the position of the last point to the mouse-position (with offset ofc)
		calc:function (e) {
			var arr = this.el.array().valueOf();
			arr.pop();

			if (e) {
				var p = this.transformPoint(e.clientX, e.clientY);	
				p.x = p.x - zoomCorrection.x;
				p.y = p.y - zoomCorrection.y;
				arr.push(this.snapToGrid([p.x, p.y]));
			}

			this.el.plot(arr);

		},

		point:function(e){

			if (this.el.type.indexOf('poly') > -1) {
			  // Add the new Point to the point-array
				var p = this.transformPoint(e.clientX, e.clientY),
				arr = this.el.array().valueOf();
				p.x = p.x - zoomCorrection.x;
				p.y = p.y - zoomCorrection.y;				
				arr.push(this.snapToGrid([p.x, p.y]));

				this.el.plot(arr);

				// Fire the `drawpoint`-event, which holds the coords of the new Point
				this.el.fire('drawpoint', {event:e, p:{x:p.x, y:p.y}, m:this.m});

				return;
			}

			// We are done, if the element is no polyline or polygon
			this.stop(e);

		},

		clean:function(){
			// Remove all circles
			this.set.each(function () {
			  this.remove();
			});

			this.set.clear();

			delete this.set;

		},
	});
})(jQuery);