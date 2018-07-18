function endIeStatus(m){
document.getElementById("question"+m).focus(false,true);
}
	var m13cId ='';
	var oldM3='';
	var oldNode;
	function loadData(node){
	    oldNode = node;
	    var tempId  = node.attributes.id;
		effectResult = node.attributes.result;
		m2Id = node.attributes.m2Id;
		waitMsg.show();
		Ext.Ajax.request( {
				url : goUrl,
				params : {					
					msiId : msiId,
					m13cId : tempId
				},
				method : "POST",
				callback: function(options, success, response){
				    waitMsg.hide();
					var all = Ext.util.JSON.decode(response.responseText);
					showData(all);
				}
			});
	}

	
	//处理后台传入值
	function showData(all){
		m13cId = all.desc[4];		
		document.getElementById('fun').innerHTML = all.desc[0];
		document.getElementById('fail').innerHTML = all.desc[1];
		document.getElementById('eff').innerHTML = all.desc[2];
		document.getElementById('cause').innerHTML = all.desc[3];
		clearStyle()
		changeColor();
		clearArea();
		clearRadioAble();
		initRadioAble();
		selectTask();
		if (all.m3 == ''){
			m3Id = "";
			oldM3 = getM3Json();
			return;
		}
		m3Id = all.m3[0].m3Id;
		changeRadioCheck("1", all.m3[0].baoyang);
		changeRadioCheck("2", all.m3[0].jianyan);
		changeRadioCheck("3", all.m3[0].jiankong);
		changeRadioCheck("4", all.m3[0].jiancha);
		changeRadioCheck("5", all.m3[0].chaixiu);
		changeRadioCheck("6", all.m3[0].Baofei);
		changeRadioCheck("7", all.m3[0].zonghe);
		changeRadioCheck("8", all.m3[0].gaijin);
		document.getElementById('area1').value = all.m3[0].baoyangDesc;
		document.getElementById('area2').value = all.m3[0].jianyanDesc;
		document.getElementById('area3').value = all.m3[0].jiankongDesc;
		document.getElementById('area4').value = all.m3[0].jianchaDesc;
		document.getElementById('area5').value = all.m3[0].chaixiuDesc;
		document.getElementById('area6').value = all.m3[0].BaofeiDesc;
		document.getElementById('area7').value = all.m3[0].zongheDesc;
		document.getElementById('area8').value = all.m3[0].remark;	
		oldM3 = getM3Json();
	}
	
	//清除影响分析中的原有样式
	function clearStyle(){
		var name;
		for (var i = 6; i <= 11; i++){
			for (var k = 1; k <= 8; k++){
				name = i + '' + k;
				document.getElementById("td" + name).style.backgroundColor = "#dfe8f7";
				document.getElementById("font" + name).style.color = "#4f6b72";
			}
		}
		document.getElementById("radio8Yes").style.color = "#4f6b72";
	}
	
	//给影响分析结果变色
	function changeColor(){
		var name;
		for (var k = 1; k <= 8; k++){
			name = effectResult + '' + k;
			document.getElementById("td" + name).style.backgroundColor = "green";
			document.getElementById("font" + name).style.color = "white";
		}
	}	
	
	//清除文本框的内容
	function clearArea(){
		for (var i = 1; i <= 8; i++){
			document.getElementById("area" + i).value = "";
		}
	}
	
	//清除按钮的无效性
	function clearRadioAble(){
		for (var i = 1; i <= 8; i++){
			if(i!=8){
				document.getElementsByName("radio" + i)[0].disabled = false;
				document.getElementsByName("radio" + i)[1].disabled = false;
				document.getElementsByName("radio" + i)[2].disabled = false;
				document.getElementsByName("radio" + i)[0].checked = false;
				document.getElementsByName("radio" + i)[1].checked = false;
				document.getElementsByName("radio" + i)[2].checked = false;
			}else{
				document.getElementsByName("radio" + i)[0].disabled = false;
				document.getElementsByName("radio" + i)[1].disabled = false;
				document.getElementsByName("radio" + i)[0].checked = false;
				document.getElementsByName("radio" + i)[1].checked = false;
			}
		}
	}	
	
	//初始化按钮的无效性
	function initRadioAble(){		
		if (effectResult == 6 || effectResult == 7||effectResult == 8){
			setRadioAble(2, true);
			if(effectResult==8){
				setRadioAble(7, true);
			}
		}else if(effectResult == 9 || effectResult == 10 || effectResult == 11 ){
			setRadioAble(3, true);
			if(effectResult == 11){
				setRadioAble(7, true);
			}
		}
	}
	
	//设置按钮有效或无效
	function setRadioAble(num, value){
		if(num == 9){
			document.getElementsByName("radio8")[0].disabled = value;
			document.getElementsByName("radio8")[1].disabled = value;
			/*if(!value){
				document.getElementsByName(name)[0].checked = true;
			}*/
		}else if(num==8){
			var name = "radio" + num;
			document.getElementsByName(name)[0].disabled = value;
			document.getElementsByName(name)[1].disabled = value;
			if(!value){
				document.getElementsByName(name)[0].checked = true;
				document.getElementById("radio8Yes").style.color = "#FF0000";
			}else{
//				document.getElementsByName(name)[0].checked = false;
				document.getElementById("radio8Yes").style.color = "#4f6b72";
			}
		}else{
			var name = "radio" + num;
			document.getElementsByName(name)[0].disabled = value;
			document.getElementsByName(name)[1].disabled = value;
			document.getElementsByName(name)[2].disabled = value;
			if (value) {
				document.getElementsByName(name)[2].checked = value;
			}
		}
	}
	
	//得到按钮的值
	function getRadioValue(num){
		if(num!=8){
			if (document.getElementsByName("radio" + num)[0].checked){
				return "1";
			} else if (document.getElementsByName("radio" + num)[1].checked){
				return "0";
			} else if (document.getElementsByName("radio" + num)[2].checked){
				return "2";
			} else {
				return "-1";
			}
		}else{
			if(document.getElementsByName("radio" + num)[0].disabled&&document.getElementsByName("radio" + num)[1].disabled){
				return '2';
			}
			if (document.getElementsByName("radio" + num)[0].checked){
				return "1";
			} else if (document.getElementsByName("radio" + num)[1].checked){
				return "0";
			} else {
				return "-1";
			}
		}
		
	}
	
	//取得所有按钮的值
	function getRadiosValueArray(){
		radiosValue = new Array();
		for (var i = 1; i <= 8; i++){
			radiosValue[i] = getRadioValue(i);
		}
	}
	
	//根据传入的值显示按钮，并修改其后的有效性
	function changeRadioCheck(num, value){
		var name = "radio" + num;
		if(num!=8){
			if (value == 1){
				document.getElementsByName(name)[0].checked = true;
			} else if (value == 0){
				document.getElementsByName(name)[1].checked = true;
			} else {
				document.getElementsByName(name)[2].checked = true;
			}
			checkRadio(num, value,1);
		}else{
			if (value == 1){
				document.getElementsByName(name)[0].checked = true;
			} else if (value == 0){
				document.getElementsByName(name)[1].checked = true;
			}
		}
	}
	
	//点击按钮事件
	//num 单选框组序号
	//value 单选框值
	function selectRadio(num, value){		
		if(isMaintain!='1'){
			//alert("能没有修改权限，无权修改！");
			return ;
		    }
		if (effectResult == 6 || effectResult == 9){	
			checkTaskNo(num, value);
		} else if(effectResult == 7 || effectResult == 10) {
			if (num == 1 || num == 7){
				checkTaskNo(num, value);
			} else {
				if (isDiffer == 0){
					checkTaskYes(num, value);
				} else {
					checkTaskNo(num, value);
				}
			}
		}else{
			if (num == 1 || num == 6){
				checkTaskNo(num, value);
			} else {
				if (isDiffer == 0){
					checkTaskYes(num, value);
				} else {
					checkTaskNo(num, value);
				}
			}
		}
	}
	
	//处理点击按钮后，联动的按钮有效性
	function checkRadio(num, value,isInit){
		if (isDiffer == 0){
			if (effectResult == 6 || effectResult == 7 || effectResult == 8){
				setRadioAble(8,true);
				setRadioAble(2, true);
				if(effectResult == 7){
					if (num == 3){
						if (value == 1){
							setRadioAble(4, true);
							setRadioAble(5, true);
							setRadioAble(6, true);
							setRadioAble(7, true);
						} else if(isInit!=1){
							setRadioAble(4, false);
							setRadioAble(5, false);					
							setRadioAble(6, false);
							setRadioAble(7, false);
						}
					} else if (num == 4){
						if (value == 1){
							setRadioAble(5, true);
							setRadioAble(6, true);
							setRadioAble(7, true);
						}  else if(isInit!=1){
							setRadioAble(5, false);					
							setRadioAble(6, false);
							setRadioAble(7, false);					
						}
					}else if (num == 5){
						if (value == 1){
							setRadioAble(6, true);
							setRadioAble(7, true);
						} else if(isInit!=1){
							setRadioAble(6, false);
							setRadioAble(7, false);					
						}
					}else if (num == 6){
						if (value == 1){
							setRadioAble(7, true);
						} else if(isInit!=1){
							setRadioAble(7, false);					
						}
					}
				}else if(effectResult == 8){
					setRadioAble(7, true);
					if (num == 3){
						if (value == 1){
							setRadioAble(4, true);
							setRadioAble(5, true);
							setRadioAble(6, true);
						} else if(isInit!=1){
							setRadioAble(4, false);
							setRadioAble(5, false);					
							setRadioAble(6, false);
						}
					} else if (num == 4){
						if (value == 1){
							setRadioAble(5, true);
							setRadioAble(6, true);
						} else if(isInit!=1){
							setRadioAble(5, false);					
							setRadioAble(6, false);
						}
					}else if (num == 5){
						if (value == 1){
							setRadioAble(6, true);
						} else if(isInit!=1){
							setRadioAble(6, false);
						}
					}
				}
			} else{
				setRadioAble(8, true);
				setRadioAble(3, true);
				if(effectResult == 10){
					if (num == 2){
						if (value == 1){
							setRadioAble(4, true);
							setRadioAble(5, true);
							setRadioAble(6, true);
							setRadioAble(7, true);
						} else if(isInit!=1){
							setRadioAble(4, false);
							setRadioAble(5, false);					
							setRadioAble(6, false);
							setRadioAble(7, false);					
						}				
					} else if (num == 4){
						if (value == 1){
							setRadioAble(5, true);
							setRadioAble(6, true);
							setRadioAble(7, true);
						} else if(isInit!=1){
							setRadioAble(5, false);					
							setRadioAble(6, false);
							setRadioAble(7, false);					
						}
					}else if (num == 5){
						if (value == 1){
							setRadioAble(6, true);
							setRadioAble(7, true);
						} else if(isInit!=1){
							setRadioAble(6, false);
							setRadioAble(7, false);					
						}
					}else if (num == 6){
						if (value == 1){
							setRadioAble(7, true);
						} else if(isInit!=1){
							setRadioAble(7, false);					
						}
					}
				}else if(effectResult == 11){
					setRadioAble(7, true);
					if (num == 2){
						if (value == 1){
							setRadioAble(4, true);
							setRadioAble(5, true);
							setRadioAble(6, true);
						} else if(isInit!=1){
							setRadioAble(4, false);
							setRadioAble(5, false);					
							setRadioAble(6, false);
						}
					} else if (num == 4){
						if (value == 1){
							setRadioAble(5, true);
							setRadioAble(6, true);
						} else if(isInit!=1){
							setRadioAble(5, false);					
							setRadioAble(6, false);
						}
					}else if (num == 5){
						if (value == 1){
							setRadioAble(6, true);
						} else if(isInit!=1){
							setRadioAble(6, false);
						}
					}
				}
			}
		}
		var count = 0;
		var total = 0;
		for(var t = 1;t<=7;t++){
			count =  getRadioNumValue(t);
			if(count>=0){
				if(count == 2){
					count = 0;
				}
			}else{
				count = 1;
			}
			total += count;
		}
		if(total == 0){
			if(effectResult==6||effectResult==9){
				setRadioAble(8, false);
			}else{
				setRadioAble(9, false);
			}
		}
	}	
	
	function getRadioNumValue(num){
		if (document.getElementsByName("radio" + num)[0].checked){
			return 1;
		} else if (document.getElementsByName("radio" + num)[1].checked){
			return 0;
		} else if (document.getElementsByName("radio" + num)[2].checked){
			return 2;
		} else {
			return -1;
		}
	}
	
	//验证数据
	function test(){
		getRadiosValueArray();
		for (var i = 1; i <= 8; i++){
			if(i!=8){
				if (radiosValue[i] == "-1"){
					alert("请选择第" + i + "个问题");
					return false;
				}
			}else{
				if (radiosValue[i] == "-1"){
					alert("请选择是否需要改进设计");
					return false;
				}
			}
		}
		for (var i = 0; i < store.getCount(); i++){
			if (store.getAt(i).get('taskCode') == '' || store.getAt(i).get('taskCode') == null){
				alert("请填写任务编号");
				return false;
			}
			if(store.getAt(i).get('taskType')== '' || store.getAt(i).get('taskType')==  null){
				alert("请选择任务类型！");
				return false;
			}
			if(store.getAt(i).get('taskDesc')== '' || store.getAt(i).get('taskDesc')==  null){
				alert("请填写任务描述！");
				return false;
			}
		}
		return true;
	}
	function getM3Json(){
	    getRadiosValueArray();
		var json = [];
		var temp = {};
		temp["m3Id"] = m3Id;
		temp["baoyang"] = radiosValue[1];
		temp["baoyangDesc"] = document.getElementById('area1').value;
		temp["jianyan"] = radiosValue[2];
		temp["jianyanDesc"] = document.getElementById('area2').value;
		temp["jiankong"] = radiosValue[3];
		temp["jiankongDesc"] = document.getElementById('area3').value;
		temp["jiancha"] = radiosValue[4];
		temp["jianchaDesc"] = document.getElementById('area4').value;
		temp["chaixiu"] = radiosValue[5];
		temp["chaixiuDesc"] = document.getElementById('area5').value;
		temp["Baofei"] = radiosValue[6];
		temp["BaofeiDesc"] = document.getElementById('area6').value;
		temp["zonghe"] = radiosValue[7];
		temp["gaijin"] = radiosValue[8];
		temp["zongheDesc"] = document.getElementById('area7').value;
		temp["remark"] = document.getElementById('area8').value;	
		json.push(temp);
		return json;
	}
	//保存
	function save(value,node){
		var json = getM3Json();
		var jsonTask = [];
		var tempData = new Array();
		for (var i = 0; i < store.getCount(); i++){
			var record = store.getAt(i);
			var temp = {};			
			temp['hasSave'] = record.get('hasSave');
			temp['whichPro'] = record.get('whichPro');
			temp['taskCode'] = record.get('taskCode');
			//alert(record.get('taskCode'));
			temp['workGroup'] = record.get('workGroup');
			temp['taskType'] = record.get('taskType');			
			temp['taskDesc'] = record.get('taskDesc');
			temp['reachWay'] = record.get('reachWay');
			temp['effectType'] = record.get('effectType');
			temp['effectiveness'] = record.get('effectiveness');
			temp['zoneTransfer'] = record.get('zoneTransfer');
			jsonTask.push(temp);
		}
		waitMsg.show();
		Ext.Ajax.request( {
			url : "${contextPath}/sys/m3/saveM3.do",
			params : {			
				jsonData : Ext.util.JSON.encode(json),
				jsonTask : Ext.util.JSON.encode(jsonTask),
				msiId : msiId,
				m2Id : m2Id,
				m13cId : m13cId
			},
			method : "POST",
			success : function(form,action) {
			        waitMsg.hide();	

				   	store.modified = [];				
				 	var msg = Ext.util.JSON.decode(form.responseText);
					if(msg.success == false){
				      alert( "该任务编号已被使用，请重新填写！");
				    	taskCodeCombo.store.load();
				    	store.reload();
				      return ;
					}
			         loadStep();
			          refreshTreeNode2();
				    alert(commonality_messageSaveMsg);
				    if (node == ''){
				    	parent.refreshTreeNode();
				        goNext(value);
				        loadData(oldNode);
				    }else{		
				       loadData(node);
	                }
				 
			},
			failure : function(form,action) {
			    waitMsg.hide();
				alert( commonality_cautionMsg);
			}
		});
	}
		//点击下一步执行的方法
	nextOrSave = function (value){
	
	     if(value == null){
			value = 6;
				}
		saveTest(value,'');
	
	}
		//点击故障原因树时的操作
	function readData(node){
		nowcode = '1';
		if (node.attributes.leaf){
			if (node.attributes.result == ""){
				alert("该原因的故障影响还未做故障分析，不能做原因分析!");
		 		return false;
			}	
          saveTest('',node);
		}		
	}
	function saveTest(value,node){
		if (m13cId!= '' &&(Ext.util.JSON.encode(getM3Json()) != Ext.util.JSON.encode(oldM3)||store.modified.length > 0)){	
		    //判断是否有权限修改
		    if (isMaintain!='1'){
			  // alert("您没有修改权限，无权修改！");
			  if(node == ''){
			   goNext(value);
		    }else{
				 loadData(node);
			}
			   return ;
			 }
		  Ext.MessageBox.show({
		    title : commonality_affirm,
		    msg : commonality_affirmSaveMsg,
		    buttons : Ext.Msg.YESNOCANCEL,
		    fn : function(yesNo){
		    	if (yesNo == 'cancel'){
					return;
				} else if (yesNo == 'yes'){
					if (!test()){
						 return;
						}
						save(value,node)
	
				} else if (yesNo == 'no'){
				 	store.modified = [];	
				   	if(node == ''){
				   		
						 goNext(value);
					}else{
					    loadData(node);
					}
				}
		    }
		});	
		}else{
		 	if(node == ''){
			   goNext(value);
		    }else{
				 loadData(node);
			}
		 }
	}