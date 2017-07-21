(function($){
    $.extend($.fn, {
        /*实现一个jquery滑动条插件*/
        jSlider: function(setting){
		    var ps = $.extend({
                renderTo: $(document.body),
                enable: true,
                initPosition: 'max',
                size: {barWidth: 200,sliderWidth: 5},
                barCssName: 'defaultbar',
                completedCssName: 'jquery-completed',
                sliderCssName: 'jquery-jslider',
                sliderHover: 'jquery-jslider-hover',
                onChanging: function(){
                },
                onChanged: function(){
                }
            }, setting);
            
			
            //强制将renderTo强制转换成jQuery对象
            ps.renderTo = (typeof ps.renderTo == 'string' ? $(ps.renderTo) : ps.renderTo);
            
            //渲染UI
            var sliderbar = $('<span><span>&nbsp;</span><span>&nbsp;</span></span>')
							.attr('class', ps.barCssName)
								.css('width', ps.size.barWidth)
									.appendTo(ps.renderTo);
            
            var completedbar = sliderbar.find('span:eq(0)')
									.attr('class', ps.completedCssName);
            
            var slider = sliderbar.find('span:eq(1)')
						.attr('class', ps.sliderCssName)
							.css('width', ps.size.sliderWidth);
            
			
            var bw = sliderbar.width(), sw = slider.width();
            
            ps.limited = {min: 0, max: bw - sw};
            
			//定位completedbar的填充长度以及slider左侧距离
            if (typeof window.$sliderProcess == 'undefined') {
                window.$sliderProcess = new Function('obj1', 'obj2', 'left', 
												'obj1.css(\'left\',left);obj2.css(\'width\',left);');
            }
            

			//eval('ps.limited.' + ps.initPosition)来获取，从而避免switch操作
			//此处相当于调用 sliderProcess(xx,xx,xxx)   执行slider.css('left',value);completedbar.css('left',value)
            $sliderProcess(slider, completedbar, eval('ps.limited.' + ps.initPosition));
            
            /*jQuery拖拽功能*/
            var slide = {
                drag: function(e){
                    var d = e.data;
					
                    var l=Math.min(Math.max(e.pageX - d.pageX + d.left, ps.limited.min), ps.limited.max);
					
					$sliderProcess(slider,completedbar,l);
					
					ps.onChanging(l/ps.limited.max,e);
                },
				
				drop:function(e){
					slider.removeClass(ps.sliderHover);
					
					ps.onChanged(parseInt(slider.css('left'))/ps.limited.max,e);
					//去除绑定
					$().unbind('mousemove',slide.drag).unbind('mouseup',slide.drop);
				}
            };
			
//			jSlider enable属性为true时，在end-user按下鼠标时绑定mousemove事件，在鼠标弹起时移除，我们只需要同步更新slider的left 属性和completedbar的width即可，同时在drag中绑定onChanging方法，在drop中绑定onChanged方法，向这两个方法推送的参数相同，1>百分比，即value值，介于0~1，2>event。
			if(ps.enable){
				slider.bind('mousedown',function(e){
					var d={
						left:parseInt(slider.css('left')),
						pageX:e.pageX
					};
					$(this).addClass(ps.sliderHover);
					$().bind('mousemove',d,slide.drag).bind('mouseup',d,slide.drop);
				});
			}
			
			slider.data = { bar: sliderbar, completed: completedbar };
            return slider;
        }
    });
})(jQuery);
