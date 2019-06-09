/*页面滚动*/
(function(){
	/*页面刷新至顶部*/
	// setTimeout(function(){
	// 	$(window).scrollTop(0);
	// },100);
	var index = 0 ;
	var wH = $(window).height();/*页面高度*/
	var nowTime = new Date();
	$(window).resize(function(){
		wH = $(window).height();
	});
	var $sliderLi = $("#slider").find("li");
	var $box = $("#warp .box_center .box");
	/*点击左侧导航*/
	$sliderLi.click(function(){
		index = $(this).index();
		move();
	});
	/*鼠标滚轮滚动*/
	$(document).mousewheel(function(){
		if(new Date() - nowTime > 500){
			nowTime = new Date();
			var d = arguments[1];
			console.log("钱"+index)
			/*d<0向下滚动*/
			index = d<0?(index>=$box.length-1?0:index+1):(index<=0?$box.length-1:index-1);
			console.log(index,$box.length)
			move();
		}
	});
	/*页面滚动*/
	function move(){
		// $li.eq(index).addClass("on").siblings().removeClass("on");
		var top = index * wH;
		$("body,html").stop(true).animate({
			scrollTop:top
		},500);
		if(index==1){
			myEcharts();
		}else if(index==2){
			myDemo();
		}
		$sliderLi.eq(index).find(".icons").stop(true).fadeIn(300).parent().siblings().find(".icons").stop(true).fadeOut(300);

		// $(".part").eq(index).find(".img1").addClass("on").parent().siblings().find(".img1").removeClass("on");
	}
	/*侧边导航栏*/
	var $box  = $("#warp .box_center .box");
	var $slider = $("#slider");
	var $sliderLi = $("#slider").find("li");
	var sliderH = $slider.height();
	/*导航栏上下居中*/
	$slider.css({"marginTop":-(sliderH/2)+"px"});
	/*鼠标悬浮导航栏*/
	$sliderLi.hover(function(){
		$(this).find(".icons").stop(true).fadeIn(300);
		$(this).find(".tab").css({"width":"60px"});
	},function(){
		var i = $(this).index();
		if(i!=index){
			$(this).find(".icons").stop(true).fadeOut(300);	
		}
		$(this).find(".tab").css({"width":"0px"});
	});
	/*鼠标悬浮音乐图标*/
	$slider.find(".music").hover(function(){
		$(this).find(".tab").css({"width":"80px"});
	},function(){
		$(this).find(".tab").css({"width":"0px"});
	});
	/*点击音乐图标，暂停播放音乐*/
	$slider.find(".music .icon_music").click(function() {
        if($(this).css("webkitAnimationPlayState")=="running"){
             $(this).css("webkitAnimationPlayState","paused");
        }else {
             $(this).css("webkitAnimationPlayState","running");
        }

    });

})();

/*拖拽作品集*/
function myDemo(){
	var oWrap = document.getElementById("case");
	var oImg = oWrap.getElementsByTagName("li");
	var oImgLength = oImg.length;
	var Deg = 360 / oImgLength;

	var nowX,nowY,lastX,lastY,minusX=0,minusY=0;

	var roY=0 , roX=-30;

	var timer;
	/*图片位移赋值*/
	for (var i = 0; i < oImgLength; i++) {
		oImg[i].style.transition = "transform 1s "+ (oImgLength-1-i)*0.1+"s";
		oImg[i].style.transform = "rotateY("+i*Deg+"deg) translateZ(350px)";
	}

	mTop()
	function mTop(){
		var wH = document.documentElement.clientHeight;
		oWrap.style.marginTop = (wH / 2)-140+"px";
		console.log(-(wH / 2)-140+"px");
	}

	window.onresize = mTop;

	/*鼠标按下*/
	document.onmousedown = function(e){
		e = e || window.event;
		clearInterval(timer);
		/*顶部文字、导航栏隐藏*/
		$("#demo_title").stop(true).fadeOut(300);
		$("#slider").stop(true).fadeOut(300);
		/*避免第一次NaN*/
		lastX = e.clientX;
		lastY = e.clientY;
		/*鼠标拖拽*/
		this.onmousemove = function(e){
			e = e || window.event;
			/*当前坐标*/
			nowX = e.clientX;
			nowY = e.clientY;

			
			/*鼠标移动的距离*/
			minusX = nowX - lastX;
			minusY = nowY - lastY;

			roX -= minusY*0.1;/*y轴转动的距离*/
			roY += minusX*0.2;/*y轴转动的距离*/
			oWrap.style.transform = 'rotateX('+roX+'deg) rotateY('+roY+'deg) ';

			/*历史坐标*/
			lastX = nowX;
			lastY = nowY;
		};
		/*鼠标抬起*/
		this.onmouseup = function(){
			$("#demo_title").stop(true).fadeIn(500);
			$("#slider").stop(true).fadeIn(300);
			/*拖拽时间暂停*/
			this.onmousemove = null;
			/*顺滑*/
			timer = setInterval(function(){
				minusX *= 0.95;
				minusY *= 0.95;
				roY += minusX*0.2;
				roX -= minusY*0.1;
				oWrap.style.transform = 'rotateX('+ roX +'deg)  rotateY('+ roY +'deg) ';
				if(Math.abs(minusX)<0.1 && Math.abs(minusY)<0.1){
					clearInterval(timer);
				}
			},13);
		}
		return false;
	}
}

/**
	audio的方法:
		paly() 播放  pause() 暂停
	audio的事件：
		oncanplaythrough : 音乐加载完毕时候调用的回调函数
		ontimeupdate : 音乐执行中执行的回调函数
*/
var player = {
	audioDom:null,//音乐播放器对象 
	playList:[],//音乐列表
	init:function(options){//初始化播放器
		var opts = extend({
			loop:false,//是否循环
			volume:5,//音量
			canplaythrough:function(){},
			timeupdate:function(){}
		},options);
		/*创建播放器*/
		this.create();
		/*音乐初始化*/
		this.audioDom.src=this.playList[0];
		/*循环的控制*/
		if(opts.loop)this.audioDom.loop = "loop";
		/*音量控制*/
		this.audioDom.volume = opts.volume /10;
		/*时间初始化*/
		this.timer(opts);
		/*加载完毕的回调函数*/
		if(opts.loadSuccess)opts.loadSuccess.call(opts);
	},
	add:function(link){//添加音乐
		this.playList.push(link);
	},
	create:function(){//创建播放器
		this.audioDom = document.createElement("audio");
	},
	play:function(){//播放 		
		this.audioDom.play();
	},
	stop:function(){//暂停音乐
		this.audioDom.pause();
	},
	timer:function(opts){//时间控制
		var $this = this;
		/*监听播放器音乐加载完毕*/
		this.audioDom.addEventListener("canplaythrough",function(){
			/*duration:总时长单位(秒)*/
			var duration  = $this.timeFormat(this.duration);
			/*回调函数*/
			if(opts.timercallback)opts.timercallback.call(this,duration);
		});

		/*监听音乐的播放*/
		this.audioDom.addEventListener("timeupdate",function(){
			/*currentTime : 当前时间 (s)*/
			var currentTime = $this.timeFormat(this.currentTime);
			var sf = $this.timeFormat(this.duration - this.currentTime);/*剩余时间*/
			/*回调函数*/
			if(opts.timeupdate)opts.timeupdate.call(this,currentTime,sf);
		});
	},
	timeFormat:function(timer){//时间的格式化
		var m = parseInt(timer / 60);//分钟
		var s = parseInt(timer % 60);//剩余秒数
		m = m<10 ? "0"+m : m;
		s = s<10 ? "0"+s : s;
		return m+":"+s;
	},
	setVolume:function(v){ //声音的控制
		this.audioDom.volume = v /10;
	},
	setCurrentTime:function(v){//跳转到指定的时间
		if(v){
			this.audioDom.currentTime=v;
			this.play();	
		}
	},
	getTime:function(){//获取当前歌曲总时长
		return this.audioDom.duration;
	},
	next:function(link){//下一首
		this.audioDom.src = link;
		this.play();
	},
	getObj:function(){//获取audio对象
		return this.audioDom;
	},
	setList:function(list){
		this.playList = list;
		this.audioDom.src=this.playList[0];
	},
	prev:function(){//上一首

	},
	loop:function(){//循环控制

	}
};


/*对象继承*/
function extend(target,source){
	var args = Array.prototype.slice.call(arguments);
	var mark = typeof args[args.length-1] ==="boolean"?args.pop():true;
	var i = 1;
	if(args.length===1){
		i = -1;
	}
	while((source = args[i++])){//undefined null "" false 0
		for(var key in source){
			if(mark  || !(callback in target)){
				target[key] = source[key];
			}
		}
	}
	return target;
};



var fail =true;
function init(){
	player.add("mp3/03.mp3");
	player.init({
			volume : 5,
			timercallback:function(timer){
				document.getElementById("sum_time").innerHTML = timer;
			},
			timeupdate:function(timer,sf){
				document.getElementById("play_time").innerHTML = timer;//+"/"+sf;
				if(fail){
					/*歌曲进度条*/
					var t = (this.currentTime / this.duration) * 100 +"%";
					document.getElementById("music_slider_cover").style.width = t; 	
				}
			},
			loadSuccess:function(){
				var s = (this.volume / 10) * 100 + "%";
				document.getElementById("perter").style.width = s;
			}
		});	
}

/*显示音乐页面*/
$("#music_btn").click(function(){
	$("#box_left").stop(true).animate({
		left:"0"
	},500,function(){
		$("#left_music").hide();
	});
});

var $li_con = $("#box_left .content ul li");
/*点击音乐列表*/
$li_con.click(function(){
	var play = $(this).data("play");
	var $li = $(this).siblings();
	var $img = $(this).find("img");
	//播放
	if(play==0){
		$li.data("play",0);
		$li.find("img").css("webkitAnimationPlayState","paused");/*其他img暂停旋转*/
		$img.addClass("rotate");/*添加旋转class*/
		$img.css("webkitAnimationPlayState","running");/*当前img开始旋转*/
		$li.find(".icon-stop-copy").hide();/*隐藏所有暂停按钮*/
		$li.find(".icon-Play").show();
		$(this).find(".icon-stop-copy").show();
		$(this).find(".icon-Play").hide();
		$(this).data("play",1);
		$("#music_play").hide();
		$("#music_stop").show();
		player.setList(arr1);
		console.log(arr1)
		player.play();
	}else{
		$img.find("img").removeClass("rotate");
		$img.css("webkitAnimationPlayState","paused");
		$(this).find(".icon-stop-copy").hide();
		$(this).find(".icon-Play").show();
		$(this).data("play",0);
		$("#music_play").show();
		$("#music_stop").hide();
		player.stop();
	}
});

/*点击播放按钮*/
$("#music_play").click(function(){
	$(this).hide();
	$("#music_stop").show();
	$li_con.each(function(i){
		if($(this).data("play")==1){
			$(this).find("img").css("webkitAnimationPlayState","running");
		}
	});
	player.play();
});
/*点击暂停按钮*/
$("#music_stop").click(function(){
	$(this).hide();
	$li_con.find("img").css("webkitAnimationPlayState","paused");
	$("#music_play").show();
	player.stop();
});

//点击音量
document.getElementById("perClick").onmousedown = function(e){
	e = e || window.event;
	var x = e.clientX;//鼠标点击x坐标
	var left = $(this).offset().left;//距离页面左侧X坐标
	var per = document.getElementById("perter");
	per.style.width = (x - left)+"px";//改变宽度
	var width = per.offsetWidth;//获取当前宽度
	var pW = $(this).width();//获取最大宽度
	var vom = (width/pW)*10;//计算音量
	player.setVolume(vom);
	return false;
}

/*鼠标点击进度条*/
document.getElementById("music_slider").onmousedown = function(e){
	e = e || window.event;
	var width = this.offsetWidth;/*进度条盒子宽度*/
	var x = e.clientX;//鼠标点击x坐标
	var left = this.parentElement.offsetLeft;//父元素距离页面左侧距离
	var per = document.getElementById("music_slider_cover");
	var playWidth = x-left;
	per.style.width=playWidth+"px";
	var time = player.getTime();
	if(time){
		var currentTime = ((playWidth/width))*time;
		player.setCurrentTime(currentTime);
		console.log(currentTime);
	}
	
}

/*鼠标拖拽音量*/
document.getElementById("volume_move").onmousedown = function(e){
	e = e || window.event;/*事件函数,存放事件相关信息 */
	var $perClick  = $("#perClick");
	var left =$perClick.offset().left/*圆点距离页面左侧X坐标*/
	var width = $perClick.width();/*音量盒子宽度*/
	document.onmousemove=function(e){
		e = e || window.event;
		var rate = (e.clientX) - left;
		// console.log("x"+e.clientX+"-left"+left+"=",rate)
		if(rate>=0 && rate <= width){
			var vom = (rate/width)*10;
			player.setVolume(vom);/*音量赋值*/
			document.getElementById("perter").style.width = rate+"px";/*音量盒子移动效果*/
		}
		return false;
	}
	document.onmouseup = function(e){
		document.onmousemove = null;
		document.onmouseup = null;
		return false;
	}
	return false;
}


/*鼠标拖拽进度条*/
document.getElementById("music_move").onmousedown = function(e){
	e = e || window.event;/*事件函数,存放事件相关信息 */
	fail =false;
	var volume  = document.getElementById("music_slider");
	var $music_slider = $("#music_slider");
	var left = $music_slider.offset().left;/*音量盒子距离页面左侧距离*/
	var width = $music_slider.width();/*音量盒子宽度*/
	var currentTime;
	document.onmousemove=function(e){
		e = e || window.event;
		var rate = (e.clientX) - left;
		if(rate>=0 && rate <= width){
			document.getElementById("music_slider_cover").style.width = rate+"px";/*音量盒子移动效果*/
			var time = player.getTime();
			if(time){
				currentTime = ((rate/width))*time;
			}
		}
		return false;
	}
	document.onmouseup = function(e){
		if(currentTime){
			player.setCurrentTime(currentTime);	
			currentTime=null;
		}
		document.onmousemove = null;
		document.onmouseup = null;
		fail =true;
		return false;
	}
}

init();

var arr1 = ["mp3/01.mp3"];
var arr2 = ["mp3/02.mp3"];
var arr3 = ["mp3/03.mp3"];
var arr4 = ["mp3/04.mp3"];
