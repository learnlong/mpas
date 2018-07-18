var editPanel=null;
var temp=null;
Ext.onReady(function(){
			Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
						temp = new Array([arrayValue.length]);
			for(var i =0;i<arrayValue.length;i++){
				temp[i]=arrayValue[i];
			}
				if(matrix){
				editPanel = new Ext.Panel({
					title:returnPageTitle('GD分析','s4'),
					region : 'center',
					id : "editPanel",
					autoScroll:true,
					html:matrix
			});}else{
				editPanel = new Ext.Panel({
					title:returnPageTitle('GD分析','s4'),
					region : 'center',
					id : "editPanel",
					autoScroll:true,
					html:'<a>请维护自定义矩阵</a>'
			});}
			//导航
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			
			
			
			var view = new Ext.Viewport({
				layout : 'border',
			    items : [
			    	{ 
			    		region : 'north',
			    		height : 60,
			    		frame : true,
			    		items : [headerStepForm]
			    	},
					editPanel
				]
			});
				if(matrix){
				document.getElementById("lRemark").innerHTML = '备注';
				document.getElementById("lArea").innerHTML = '区域的内外部';
				document.getElementById("lFeasibility").innerHTML = '适用性';
				document.getElementById("lAreaPrice").innerHTML = '外部';
				document.getElementById("lAttribute").innerHTML = '属性';
				document.getElementById("lComponent").innerHTML = '组成部分';
			}
			
			//点击下一步执行的方法
			nextOrSave = function (number){
				if(number){
					if (step[number] == 0){
						alert('请先完成之前的步骤！');
						return false;
					}
				}
					if (!test(number)){ 
						if(number){
								goNext(number);
								return null;
							}
							goNext(18);
					return false;
					};
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){							
							save(number);
						} else if (id == 'no'){
							if(number){
								goNext(number);
								return null;
							}
							goNext(18);
							return;
						}
				    }
				});
			};
			
			function test(number){
			var flag=true;//用来作为标记使用,判断当前所提交的数据里面是否还有null
			for(var i =0;i<arrayValue.length;i++){
					if(temp[i]!=arrayValue[i]){
						return true;
					}
					//return false;
				}
				if(remark!=document.getElementsByName("remark")[0].value){
					return true;
				}
				for(var i =0;i<arrayValue.length;i++){
					if(arrayValue[i]==null){
						flag=false;
					}
				}
				if(flag&&step[18]==0){
					return true;
				}
				if (isMaintain == '0'&&matrix){
					if(number){
						goNext(number);
						return null;
					}
					goNext(18);
					return false;
				}
				if(!matrix){
					alert(s4_matrix);
					return false;
				}
				return false;
			}
			
			function save(number){
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});
				
					waitMsg.show();
					Ext.Ajax.request( {
							url :  contextPath+"/struct/s4/saveS4aout.do",
							method : 'post',
							waitTitle : commonality_waitTitle,
							waitMsg : commonality_waitMsg,
							params:{
								ssiLength:ssiLength,
								arrayValue:arrayValue,
								s4IdArray:s4IdArray,
								ssiId:ssiId,
								remark:document.getElementsByName("remark")[0].value
							},
							success : function(e,action) {
							waitMsg.hide();
							if(e.responseText!='false'){
								if(number){
									goNext(number);
									return null;
								}
								alert(commonality_messageSaveMsg);
								parent.refreshTreeNode();
								successNext(18);
								}else{
									alert('请完成本页面');
								}
							},
							failure : function(form,action) {
							waitMsg.hide();
								alert(commonality_cautionMsg);
					}
						});
				 
			}
			doSetValue(arrayValue);
});
	//页面初始化赋值
	function doSetValue(value){
		items=document.getElementsByTagName("td");
		for(var i =0;i<arrayValue.length;i++){
			for(var j=0;j<items.length;j++){
			if(items[j].attributes.mytag!=undefined&&items[j].attributes.mytag.value=='1'){
				if((items[j].attributes.value.value==-1&&items[j].attributes.onIndex.value==i)||(items[j].attributes.onIndex.value==i&&items[j].attributes.value.value==arrayValue[i])){
						if(items[j].attributes.myflag.value==1){
							items[j].innerHTML=(arrayValue[i]==null?"":arrayValue[i].toFixed(2));
						}
						if(items[j].attributes.value.value!=-1){
							items[j].style.background='#00FF99';
							break;
					}
					}
			}}
		
		}
	}


	function select(e,count){
		value=e.attributes.value.value;
		items=document.getElementsByTagName("td");
		for(var i=0;i<items.length;i++){
			if(items[i].attributes.mytag!=undefined&&items[i].attributes.mytag.value=='1'){
			if(items[i].attributes.onIndex.value==e.attributes.onIndex.value){
				items[i].style.background='none';
			}
		}}
		e.style.background='#00FF99';
		arrayValue[e.attributes.onIndex.value]=value;
	
	
	
	/*ename=e.attributes.name.value;
		items=document.getElementsByName(ename);
		for(var i=0;i<items.length;i++){
			items[i].style.background='none';
		}
		e.style.background='#00FF99';
		arrayValue[count]=value;*/
		from=e.attributes.from.value-0;
		to=e.attributes.to.value-0;
		for(var j=from;j<to;j++){
			if(arrayValue[j]==undefined){
				return;
			}
		}
		
		var algorithm=e.attributes.algorithm.value;
		
		
		var value="";
		if(algorithm=='null'){
			alert('由于算法尚未维护,无法计算出当前的具体值');
		}
		if(algorithm=='MAX'){
			value=doMax(arrayValue,from,to);
		}		
		if(algorithm=='MIN'){
			value=doMin(arrayValue,from,to);
		}		
		if(algorithm=='AVG'){
			value=doAvg(arrayValue,from,to);
		}	
		if(algorithm=='SUM'){
			value=doSum(arrayValue,from,to);
		}
		doValue(e.attributes.s1Id.value+e.attributes.doName.value,value);
			var level=[];
		
		for(var i = 0 ; i<levelCount;i++){
			level[i]=document.getElementById(e.attributes.s1Id.value+(String.fromCharCode(i+65))).innerHTML;
		}
		//vr=document.getElementById(e.attributes.s1Id.value+"vr").innerHTML;
		//sc=document.getElementById(e.attributes.s1Id.value+"sc").innerHTML;
		//pr=document.getElementById(e.attributes.s1Id.value+"pr").innerHTML;
		//ev=document.getElementById(e.attributes.s1Id.value+"ev").innerHTML;
		var edr=document.getElementById(e.attributes.s1Id.value+"edr");
		var edrAlg=edr.attributes.algorithm.value;
		
		for(var i=0;i<level.length;i++){
			if(!level[i]){
				return
			}
		}
		
		//if(vr!=""&&sc!=""&&pr!=""&&ev!=""){
		if(edrAlg=='null'){
			alert('由于算法尚未维护,无法计算出当前的具体值');
		}
		from=edr.attributes.onIndex.value-levelCount;
		to=edr.attributes.onIndex.value;
		if(edrAlg=='MAX'){
			value=doMax(arrayValue,from,to);
		}		
		if(edrAlg=='MIN'){
			value=doMin(arrayValue,from,to);
		}		
		if(edrAlg=='AVG'){
			value=doAvg(arrayValue,from,to);
		}	
		if(edrAlg=='MULT'){
			value=doMult(arrayValue,from,to);
		}	
		if(edrAlg=='SUM'){
			value=doSum(arrayValue,from,to);
		}
			arrayValue[edr.attributes.onIndex.value]=value;
			edr.innerHTML=value+"";
		}
	//}
	//最大
	function doMax(array,from,to){
		var max=0;
		for(var i =from;i<to;i++){
			if(	array[i]>max){
				max=array[i];
			}
		}
		return (max-0).toFixed(2);
	}
	//最小
	function doMin(array,from,to){
		var min=999999;
		for(var i =from;i<to;i++){
			if(	array[i]<min){
				min=array[i];
			}
		}
		return (min-0).toFixed(2);
	}
	//平均
	function doAvg(array,from,to){
		var value=0;
		for(var k=from;k<to;k++){
			value=arrayValue[k]-0+value;
			
		}
		return (value/(to-from)).toFixed(2);
	}
		//求和
	function doSum(array,from,to){
		var value=0;
		for(var k=from;k<to;k++){
			value=arrayValue[k]-0+value;
			
		}
		return value.toFixed(2);
	}
	
	//求乘机
	function doMult(array,from,to){
		var value=1;
		for(var k=from;k<to;k++){
			value=arrayValue[k]*value;
			
		}
		return value.toFixed(2);
	}
	
	//给对应的地方赋值
	function doValue(name,value){
		var index=document.getElementById(name).attributes.onIndex.value;
		arrayValue[index]=value+"";
		document.getElementById(name).innerHTML=value;
	}
	
