			// 获取画布对象
			var canvas = document.getElementById('canvas');
			console.info(canvas);
			// 获取一个2d绘图环境(拿到一支画笔)
			var ctx = canvas.getContext('2d');
			
			function draw(){
				var date = new Date();
				var hours = date.getHours();
				var min = date.getMinutes();
				var sec = date.getSeconds();
//				console.info(hours+':'+min+':'+sec);
				hours = hours > 12 ? hours-12 : hours;					
				
				// 清除画布(防止覆盖)
				ctx.clearRect(0,0,500,500);
				
				// 初始化画笔的样式
				ctx.lineWidth = 5;
				ctx.strokeStyle = '#fff'; // 设置线条颜色
				
				ctx.beginPath(); // 开始一个绘图路径
				// 设置一个圆形路径
				ctx.arc(250,250,230,0,360,false);
				ctx.stroke(); // 绘制图形
				ctx.closePath(); // 结束一个绘图路径
				
				// 绘制刻度(时刻度)
				for(var i = 0; i < 12; i++){
					ctx.beginPath();
					ctx.lineWidth = 10;
					ctx.save(); // 保存当前绘图环境
					ctx.translate(250,250); // 重置绘制起始位置(将圆心点作为坐标原点(0,0))
					ctx.rotate(i*30*Math.PI/180); // 旋转画布到一定的弧度
					ctx.moveTo(0,200); // 设置绘制线条的起始位置
					ctx.lineTo(0,230); // 设置线条的结束位置
					ctx.stroke(); // 绘制路径
					
					ctx.restore(); // 还原初始绘图环境
					ctx.closePath();
				}
				
				// 绘制刻度(分刻度)
				for(var i = 0; i < 60; i++){
					ctx.beginPath();
					ctx.lineWidth = 3;
					ctx.save();
					ctx.translate(250,250);
					ctx.rotate(i*6*Math.PI/180);
					ctx.moveTo(0,210);
					ctx.lineTo(0,230);
					ctx.stroke();
					ctx.restore();
					ctx.closePath();
				}
				
				// 绘制时针 分针 秒针
				ctx.beginPath();
				ctx.lineWidth = 8;
				ctx.save();
				ctx.translate(250,250);
				ctx.rotate(((hours+min/60)+sec/3600)*30*Math.PI/180);
				ctx.moveTo(0,15);
				ctx.lineTo(0,-120);
				ctx.stroke();
				ctx.restore();
				ctx.closePath();
				
				ctx.beginPath();
				ctx.lineWidth = 6;
				ctx.save();
				ctx.translate(250,250);
				ctx.rotate((min+sec/60)*6*Math.PI/180);
				ctx.moveTo(0,15);
				ctx.lineTo(0,-160);
				ctx.stroke();
				ctx.restore();
				ctx.closePath();
				
				ctx.beginPath();
				ctx.lineWidth = 4;
				ctx.strokeStyle = '#f00';
				ctx.save();
				ctx.translate(250,250);
				ctx.rotate(sec*6*Math.PI/180);
				ctx.moveTo(0,15);
				ctx.lineTo(0,-185);
				ctx.stroke();
				ctx.restore();
				ctx.closePath();
			}
			
			setInterval(draw, 1000);
			
			function f1(){
				var date = new Date();
				var year = fmtDate(date.getFullYear());//完整的年份
				var month = fmtDate(date.getMonth()+1);// 7  月份从0开始
				var day = fmtDate(date.getDate());// 号
				var hours = fmtDate(date.getHours());
				var min = fmtDate(date.getMinutes());
				var sec = fmtDate(date.getSeconds());
				var content = (year+'-'+month+'-'+day+'<br>'+hours+':'+min+':'+sec);
				
				document.getElementById('box').innerHTML = content;
			}
			// 每隔一秒执行一次函数
			setInterval(f1,1000);
			
			function fmtDate(v){
				if(v < 10){
					return '0'+v;
				}
				return v;
			}
