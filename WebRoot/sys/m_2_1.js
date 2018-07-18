	for(var i=1; i<=5;i++){
	document.getElementById("radio"+i+"Yes").innerHTML = commonality_shi;
	document.getElementById("radio"+i+"No").innerHTML = commonality_fou;
	}
	for(var i = 1 ;i <=7 ; i++){
	document.getElementById("yes"+i).innerHTML = commonality_shi;
	document.getElementById("no"+i).innerHTML = commonality_fou;
	}
	var headerStepForm = new Ext.form.Label({
		applyTo : 'headerStepDiv'
	});
	var myDivForm = new Ext.form.Label({
		applyTo : 'myDiv'
	});
	
	
	var view = new Ext.Viewport({
		layout : 'border',
	    items : [
	    	{ 
	    		region : 'north',
	    		layout : 'fit',
	    		height : 60,
	    		frame : true,
	    		items : [headerStepForm]
	    	}, {
				title : returnPageTitle("分析故障影响类别",'m_2'),
				region : 'center',
				layout : 'border',
				items : [
					{   
						region : 'west',
						layout : 'fit',
						width : 130,
						items : [treePanelTwo]
					}, {
						region : 'center',
						layout : 'border',
						items : [
							{
								region : 'center',
								layout : 'fit',
								items : [myDivForm]
							}
						]
					}
				]
			}
		]
	});
	
	function test(){
		if (m13fId == ''){
			alert("请选择故障影响后再做保存");
			return false;
		}
		if (document.getElementById('failureCauseType').innerHTML == ""){
			alert("请选择问题");
			return false;
		}
	      if(document.getElementsByName("radio1")[0].disabled==false&&
	  	    	document.getElementsByName("radio1")[1].disabled==false&&
	  	    	(document.getElementById('area1').value==null||document.getElementById('area1').value=="")){
	  	    	  alert("请填写第一个问题的说明");
	  	    	  return;
	  	      }
	  	      if(document.getElementsByName("radio2")[0].disabled==false&&
	  	  	    	document.getElementsByName("radio2")[1].disabled==false&&
	  		    	(document.getElementById('area2').value==null||document.getElementById('area2').value=="")){
	  	  	      alert("请填写第二个问题的说明");
	  	  	      return;
	  	  	  }
	  	      if(document.getElementsByName("radio4")[0].disabled==false&&
	  		  	  document.getElementsByName("radio4")[1].disabled==false&&
	  		    	(document.getElementById('area4').value==null||document.getElementById('area4').value=="")){
	  		  	  alert("请填写第三个问题的说明");
	  		  	  return;
	  		  }
	  	      if(document.getElementsByName("radio3")[0].disabled==false&&
	  		  	    	document.getElementsByName("radio3")[1].disabled==false&&
	  			    	(document.getElementById('area3').value==null||document.getElementById('area3').value=="")){
	  		  	  alert("请填写第四个问题的说明");
	  		  	  return;
	  		  }
	  	      if(document.getElementsByName("radio7")[0].disabled==false&&
	  		  	    	document.getElementsByName("radio7")[1].disabled==false&&
	  			    	(document.getElementById('area7').value==null||document.getElementById('area7').value=="")){
	  		  	  alert("请填写第五个问题的说明");
	  		  	  return;
	  		  }
		return true;
	}
	
	function save(value,node){
		var m2Json = getM2Json();
		var afmJson = [];
		temp = {};
		temp["afmId"] = afmId;
		temp["refAfm"] = Ext.getCmp('refAfm').getValue();
		temp["reviewResult"] = Ext.getCmp('reviewResult1').getValue();
		temp["reviewDate"] = Ext.getCmp('reviewDate1').getValue();
		temp["remark"] = Ext.getCmp('remark1').getValue();
		afmJson.push(temp);
		
		var mmelJson = [];
		temp = {};
		temp["mmelId"] = mmelId;
		temp["isRefPmmel"] = Ext.getCmp('isRefPmmel').getValue();
		temp["reviewResult"] = Ext.getCmp('reviewResult2').getValue();
		temp["reviewDate"] = Ext.getCmp('reviewDate2').getValue();
		temp["remark"] = Ext.getCmp('remark2').getValue();
		temp["pmmelId"] = Ext.getCmp('mmelCom').getValue();
		mmelJson.push(temp);
		waitMsg.show();
		Ext.Ajax.request( {
			url : contextPath+"/sys/m2/saveM2.do",
			params : {			
				jsonData : Ext.util.JSON.encode(m2Json),
				afmJsonData : Ext.util.JSON.encode(afmJson),
				mmelJsonData : Ext.util.JSON.encode(mmelJson),
				msiId : msiId,
				m13fId : m13fId ,
				isSaveAfm : isSaveAfm,
				isSaveMmel : isSaveMmel
			},
			method : "POST",
			success : function(form,action) {
				loadStep();
				waitMsg.hide();
				alert(commonality_messageSaveMsg);
				if(node == ''){
					step[5] = 2;
					goNext(value);
					parent.refreshTreeNode();
				}else{
		          loadData(node);
		          refreshTreeNode2();
		          //rootNode.reload();
				}
			},
			failure : function(form,action) {
			    waitMsg.hide();
				alert(commonality_messageSaveMsg);
			}
		});
	}
	//function
	function  getM2Json(){
		var m2Json = [];
		var temp = {};
		temp["m2Id"] = m2Id;
		for(var i=1;i<=5;i++){
			if(i=='5'){
				i='7';
			}
			temp["q"+i] = getRadio("radio"+i);
			if (document.getElementById("area"+i).disabled){
		       	temp["q"+i+"Desc"] = "";
		    } else {
			    temp["q"+i+"Desc"]=document.getElementById("area"+i+"").value;
		    }
		}
		temp["failureCauseType"] = document.getElementById('failureCauseType').innerHTML;
		temp["mmel"] = document.getElementById('mmelValue').innerHTML;
		temp["remark"] = getArea("area5");
		temp["isRefAfm"] = getRadio("radio5");
		temp["isRefMmel"] = getRadio("radio6");
		m2Json.push(temp);
		return m2Json;
	}
	function getArea(name){
		if (document.getElementById(name).disabled){
			return "";
		} else {
			return document.getElementById(name).value;
		}
	}
	
	function getRadio(name){		
		if (document.getElementsByName(name)[0].disabled){
			return "2";
		} else {
			if (document.getElementsByName(name)[0].checked){
				return "1";
			} else if (document.getElementsByName(name)[1].checked){
				return "0";
			} else {
				return "2";
			}
		}
	}
	//设置动态图片
	function issueSelect(){
	document.getElementById('category11').className = '';
	document.getElementById('category22').className = '';
	document.getElementById('category33').className = '';
	document.getElementById('category44').className = '';
	document.getElementById('category55').className = '';
	document.getElementById('category66').className = '';
	var m = new Array(0,1, 2,3,4,7); 
	  for(var i=1 ; i<=5;i++){
	  	if (document.getElementsByName('radio'+m[i])[0].disabled){
	      document.getElementById('question'+i).className = '';
	  	  document.getElementById('radio'+i+'No').className = '';
	  	  document.getElementById('radio'+i+'Yes').className = '';
	  }else if (document.getElementsByName('radio'+m[i])[0].checked){
	      document.getElementById('question'+i).className = 'questionHighlight';
	      document.getElementById('radio'+i+'No').className = '';
	  	  document.getElementById('radio'+i+'Yes').className = 'change1';
	  	  if(i== '3'){
	  	   document.getElementById('category44').className = 'questionHighlight';
	  	  }else if(i == '2'){
	  	  document.getElementById('category11').className = 'questionHighlight';
	  	  }else if(i == '4'){
	  	  document.getElementById('category22').className = 'questionHighlight';
	  	  }else if(i=='5'){
	  		 document.getElementById('category55').className = 'questionHighlight';
	  	  }
	  }else if(document.getElementsByName('radio'+m[i])[1].checked){
		  document.getElementById('question'+i).className = 'questionHighlight';
		  document.getElementById('radio'+i+'No').className = 'change1';
		  document.getElementById('radio'+i+'Yes').className = '';
		  if(i == '4'){
		  	   document.getElementById('category33').className = 'questionHighlight';
		  }else if(i=='5'){
			  document.getElementById('category66').className = 'questionHighlight';
		  }
	  }else{
	     document.getElementById('question'+i).className = '';
	   	 document.getElementById('radio'+i+'No').className = '';
	  	 document.getElementById('radio'+i+'Yes').className = '';
	  }
	  
	}
 }
	

	
	//载入数据
	function loadData(node){
	    var tempId  = node.attributes.id;
	    showText = node.attributes.showtext;
	    waitMsg.show();
		Ext.Ajax.request( {
			url : goUrl,
			params : {					
				msiId : msiId,
				m13fId : tempId
			},
			method : "POST",
			callback: function(options, success, response){
			    waitMsg.hide();
				var all = Ext.util.JSON.decode(response.responseText);
				showData(all);
			}
		});
	}
	
	//显示数据
	function showData(all){
	    allData= all;
		m13fId = all.desc[3];		
		document.getElementById('fun').innerHTML = all.desc[0];
		document.getElementById('fail').innerHTML = all.desc[1];
		document.getElementById('eff').innerHTML = all.desc[2];
				
		m2Id = "";
		afmId = "";
		mmelId = "";
		isSaveAfm = 0;
		isSaveMmel = 0;
		if (all.m2 == ''){			
			changeRadioNoCheck();			
			changeRadioStatus("radio2", true);
			changeRadioStatus("radio3", true);
			changeRadioStatus("radio4", true);
			changeRadioStatus("radio7", true);
			document.getElementById('area1').value = '';
			document.getElementById('area2').value = '';
			document.getElementById('area3').value = '';
			document.getElementById('area4').value = '';	
			document.getElementById('area5').value = '';	
			document.getElementById('area7').value = '';	
			document.getElementById('failureCauseType').innerHTML = "";
			document.getElementById('mmelValue').innerHTML = "";	
			Ext.getCmp('msiCodeAfm').setValue(ataCode);
			Ext.getCmp('fFfFeAfm').setValue(showText);
			Ext.getCmp('msiCodeMmel').setValue(ataCode);
			Ext.getCmp('feMmel').setValue(showText);
			Ext.getCmp('refAfm').setValue('');
			Ext.getCmp('reviewResult1').setValue('');
			Ext.getCmp('reviewDate1').setValue('');
			Ext.getCmp('remark1').setValue('');
			Ext.getCmp('isRefPmmel').setValue('');
			Ext.getCmp('reviewResult2').setValue('');
			Ext.getCmp('reviewDate2').setValue('');
			Ext.getCmp('remark2').setValue('');
			Ext.getCmp('mmelCom').setValue('');
			issueSelect();	
			oldM2 = getM2Json();	
			return;
		} else {
			m2Id = all.m2[0].m2Id;
			document.getElementById('area1').value = all.m2[0].q1Desc;
			document.getElementById('area2').value = all.m2[0].q2Desc;
			document.getElementById('area3').value = all.m2[0].q3Desc;
			document.getElementById('area4').value = all.m2[0].q4Desc;	
			document.getElementById('area5').value = all.m2[0].remark;	
			document.getElementById('area7').value = all.m2[0].q7Desc;	
			document.getElementById('failureCauseType').innerHTML = all.m2[0].failureCauseType;
			document.getElementById('mmelValue').innerHTML = all.m2[0].mmel;
			changeRadioCheck('radio1', all.m2[0].q1);
			changeRadioCheck('radio2', all.m2[0].q2);
			changeRadioCheck('radio3', all.m2[0].q3);
			changeRadioCheck('radio4', all.m2[0].q4);
			changeRadioCheck('radio5', all.m2[0].isRefAfm);
			changeRadioCheck('radio6', all.m2[0].isRefMmel);
			changeRadioCheck('radio7', all.m2[0].q7);
			oldM2 = getM2Json();	
			issueSelect();			
			Ext.getCmp('msiCodeAfm').setValue(ataCode);
		    Ext.getCmp('fFfFeAfm').setValue(showText);
		    Ext.getCmp('answer1Afm').setValue(all.m2[0].q1Desc);
			if (all.afm == ''){
				afmId = '';
				Ext.getCmp('refAfm').setValue('');
				Ext.getCmp('reviewResult1').setValue('');
				Ext.getCmp('reviewDate1').setValue('');
				Ext.getCmp('remark1').setValue('');
			} else {
				afmId = all.afm[0].afmId;
				Ext.getCmp('refAfm').setValue(all.afm[0].refAfm);
				Ext.getCmp('reviewResult1').setValue(all.afm[0].reviewResult);
				Ext.getCmp('reviewDate1').setValue(all.afm[0].reviewDate);
				Ext.getCmp('remark1').setValue(all.afm[0].remark);
			}
				Ext.getCmp('msiCodeMmel').setValue(ataCode);
				Ext.getCmp('feMmel').setValue(showText);
				Ext.getCmp('answer4Mmel').setValue('');
			if (all.mmel == ''){
				mmelId = '';
				Ext.getCmp('isRefPmmel').setValue('');
				Ext.getCmp('reviewResult2').setValue('');
				Ext.getCmp('reviewDate2').setValue('');
				Ext.getCmp('remark2').setValue('');
				Ext.getCmp('mmelCom').setValue('');
			} else {
				mmelId = all.mmel[0].mmelId;
		        Ext.getCmp('isRefPmmel').setValue(all.mmel[0].isRefPmmel);
				Ext.getCmp('reviewResult2').setValue(all.mmel[0].reviewResult);
				Ext.getCmp('reviewDate2').setValue(all.mmel[0].reviewDate);
				Ext.getCmp('remark2').setValue(all.mmel[0].remark);
				Ext.getCmp('mmelCom').setValue(all.mmel[0].pmmelId);
			}
		}
       				
	}
	
	//点击单选按钮的事件
	function selectRadio(num, value){
		if (num == 1){
			if (value == 1){
				changeRadioStatus("radio2", false);
				changeRadioStatus("radio3", true);
				changeRadioStatus("radio7", true);
				
				var twoRadioYes = document.getElementsByName("radio2")[0].checked;
				var twoRadioNo = document.getElementsByName("radio2")[1].checked;
				if (twoRadioYes || twoRadioNo){
					if (twoRadioYes){
						changeRadioStatus("radio4", true);
					}
					if (twoRadioNo){
						changeRadioStatus("radio4", false);
					}
				} else {
					changeRadioStatus("radio4", true);
				}
			} else if (value == 0){
				changeRadioStatus("radio2", true);
				changeRadioStatus("radio3", false);
				changeRadioStatus("radio4", true);
				var twoRadioYes = document.getElementsByName("radio3")[0].checked;
				var twoRadioNo = document.getElementsByName("radio3")[1].checked;
				if(twoRadioNo){
					changeRadioStatus("radio7", false);
				}else{
					changeRadioStatus("radio7", true);
				}
			}			
		}
		if (num == 2){
			if (value == 1){
				changeRadioStatus("radio4", true);
			} else if (value == 0){
				changeRadioStatus("radio4", false);
			}			
		}
		if (num == 3){
			if (value == 1){
				changeRadioStatus("radio7", true);
			} else if (value == 0){
				changeRadioStatus("radio7", false);
			}			
		}
		if (num != 5 && num != 6){
			changefailureCauseType();
			issueSelect();
		}
		if (num == 5 && value == 1){
			addAFM();
		}
		if (num == 6){
			if (value == 1){
				addMMEL();
			} else {
				document.getElementById('mmelValue').innerHTML = "No";
			}			
		}
	}
	
	//显示故障影响类别
	function changefailureCauseType(){
		var one = getRadio("radio1");
		var two = getRadio("radio2");
		var three = getRadio("radio3");
		var four = getRadio("radio4");
		var five = getRadio("radio7");
		if (one == 1){
			if (two == 1){
				document.getElementById("failureCauseType").innerHTML = 6;
				document.getElementById('mmelValue').innerHTML = "N/A";
			} else if (two == 0){
				if (four == 1){
					document.getElementById("failureCauseType").innerHTML = 7;
					if (getRadio('radio6') == 1 && Ext.getCmp('isRefPmmel').getValue() == 1){
						document.getElementById('mmelValue').innerHTML = 
						mmelCombo.lastSelectionText;
					} else {
						document.getElementById('mmelValue').innerHTML = "No";
					}					
				} else if (four == 0){
					document.getElementById("failureCauseType").innerHTML = 8;
					if (getRadio('radio6') == 1 && Ext.getCmp('isRefPmmel').getValue() == 1){
						document.getElementById('mmelValue').innerHTML = 
							mmelCombo.lastSelectionText;
					} else {
						document.getElementById('mmelValue').innerHTML = "No";
					}
				} else {
					document.getElementById("failureCauseType").innerHTML = "";
					document.getElementById('mmelValue').innerHTML = "";
				}				
			} else {
				document.getElementById("failureCauseType").innerHTML = "";
				document.getElementById('mmelValue').innerHTML = "";
			}			
		} else if (one == 0) {
			if (three == 1){
				document.getElementById("failureCauseType").innerHTML = 9;
				document.getElementById('mmelValue').innerHTML = "N/A";
			} else if (three == 0){
				if(five == 1){
					document.getElementById("failureCauseType").innerHTML = 10;
					document.getElementById('mmelValue').innerHTML = "";
				}else if(five == 0){
					document.getElementById("failureCauseType").innerHTML = 11;
					document.getElementById('mmelValue').innerHTML = "";
				}else{
					document.getElementById("failureCauseType").innerHTML = "";
					document.getElementById('mmelValue').innerHTML = "";
				}
			} else {
				document.getElementById("failureCauseType").innerHTML = "";
				document.getElementById('mmelValue').innerHTML = "";
			}
		} else {
			document.getElementById("failureCauseType").innerHTML = "";
			document.getElementById('mmelValue').innerHTML = "";
		}		
	}
	
	//单选按钮选择
	function changeRadioCheck(name, value){
		var num = name.substring(name.length - 1, name.length);
		if (value == 1){
			document.getElementsByName(name)[0].checked = true;
			document.getElementsByName(name)[1].checked = false;
			changeRadioStatus(name, false);
		} else if (value == 0){
			document.getElementsByName(name)[0].checked = false;
			document.getElementsByName(name)[1].checked = true;
			changeRadioStatus(name, false);
		} else if (value == 2){
			document.getElementsByName(name)[0].checked = false;
			document.getElementsByName(name)[1].checked = false;
			if(num!=5&&num!=6){
				changeRadioStatus(name, true);
			}
		}
	}
	
	function changeRadioNoCheck(){
		var name;
		for (var i = 1; i <= 7; i++){
			name = "radio" + i;
			document.getElementsByName(name)[0].checked = false;
			document.getElementsByName(name)[1].checked = false;
		}		
	}
	
	//单选按钮和输入框操作————有效或无效
	function changeRadioStatus(name, value){	
		document.getElementsByName(name)[0].disabled = value;
		document.getElementsByName(name)[1].disabled = value;
		
		var num = name.substring(name.length - 1, name.length);
		if (num != 5 && num != 6){
			name = "area" + num;
			document.getElementById(name).disabled = value;
		}		
	
		if (num == 4){
			name = "radio6";
			document.getElementsByName(name)[0].disabled = value;
			document.getElementsByName(name)[1].disabled = value;
		}
	}
	
	//点击下一步执行的方法
	nextOrSave = function (value){
	     if(value == null){
              value = 5;
				}
	  saveTest(value,'')
	}
		
	//点击故障影响树时的操作
    function readData(node){
          saveTest('',node)
	}
	function saveTest(value,node){
	      var newM2Json =  Ext.util.JSON.encode(getM2Json());
	      var oldM2Json =  Ext.util.JSON.encode(oldM2);
		if (m13fId != ''&&(newM2Json != oldM2Json || isSaveMmel == 1 || isSaveAfm ==1)){	 
			//判断是否有权限修改
		    if (isMaintain!='1'){
			   //alert("您没有修改权限，无权修改！");
			   goNext(value);
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
							save(value,node);						
					} else if (yesNo == 'no'){
					      if (node ==''){
					          goNext(value);
					      }else{
					         loadData(node);
					      }
					}
			    }
			});
	       }else{
	          if(node ==''){
			      goNext(value);
			      }else{
			       loadData(node);
			      }
	       }
	}