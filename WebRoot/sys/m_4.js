var similar_td = new Ext.form.TextArea({
			name : 'similar',
			id : 'similar',
			maxLength :2000,
			width : 423,
			height : 50
		});
		var ana_td = new Ext.form.TextArea({
			name : 'ana',
			id : 'ana',
			maxLength :1000,
			width : 423,
			height : 50
		})
var taskInterval_td = new Ext.form.TextArea({
			name : 'taskInterval',
			id : 'taskInterval',
			maxLength :200,
			width : 423,
			height : 50
		})
		similar_td.render(document.getElementById("similar_td"));
		ana_td.render(document.getElementById("ana_td"));
		taskInterval_td.render(document.getElementById("taskInterval_td"));
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'qtip';	
   var taskId = '';
   var oldM4 = '';
   var oldNode;
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
				title :returnPageTitle("工作间隔确定",'m_4'),
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

	function loadData(node){
		  oldNode = node;
		var tempId  = node.attributes.id;		
		if (node.attributes.leaf){	
		   waitMsg.show();			
			Ext.Ajax.request( {
				url : goUrl,
				params : {					
					msiId : msiId,
					taskId : tempId
				},
				method : "POST",
				callback: function(options, success, response){
				    waitMsg.hide();
					all = Ext.util.JSON.decode(response.responseText);
					showData(all);
				}
			});
		}
	
	}
	//显示数据
	function showData(all){
		taskId = all.desc[5];
		document.getElementById('effectiveness').innerHTML = all.desc[0];
		document.getElementById('taskCode').innerHTML = all.desc[1];
		document.getElementById('taskDesc').innerHTML = all.desc[2];
		document.getElementById('result').innerHTML = all.desc[3];
		document.getElementById('taskInterval').value = all.desc[4];//taskInterval
		document.getElementById('ana').value = "";
		document.getElementById('similar').value = "";
		document.getElementById('engineerReview').value = "";
		document.getElementById('engineerSuggest').value = "";
		document.getElementById('groupReview').value = "";
		document.getElementById('other').value = "";
		document.getElementById('remark').value = "";
		
		if (all.m4 == ''){
			m4Id = '';
			oldM4 = getM4Json();
			return;
		} else {
			m4Id = all.m4[0].m4Id;
			document.getElementById('ana').value = all.m4[0].ana;
			document.getElementById('similar').value = all.m4[0].similar;
			document.getElementById('engineerReview').value = all.m4[0].engineerReview;
			document.getElementById('engineerSuggest').value = all.m4[0].engineerSuggest;
			document.getElementById('groupReview').value = all.m4[0].groupReview;
			document.getElementById('other').value = all.m4[0].other;
			document.getElementById('remark').value = all.m4[0].remark;
			oldM4 = getM4Json();
		}
	}
	function getM4Json(){
	    var json = [];
		var temp = {};
		temp["m4Id"] = m4Id;
		temp["ana"] = document.getElementById('ana').value;
		temp["similar"] = document.getElementById('similar').value;
		temp["engineerReview"] = document.getElementById('engineerReview').value;
		temp["engineerSuggest"] = document.getElementById('engineerSuggest').value;
		temp["groupReview"] = document.getElementById('groupReview').value;
		temp["other"] = document.getElementById('other').value;
		temp["remark"] = document.getElementById('remark').value;
		temp["taskInterval"] = document.getElementById('taskInterval').value;
		json.push(temp);
		return json;
	}
	
	function save(value,node){
		if(document.getElementById('taskInterval').value ==''){
			alert("间隔值不能为空！");
			return ;
		}
		if(document.getElementById('taskInterval').value.length>200||
		document.getElementById('similar').value.length>1000||
		document.getElementById('ana').value.length>500){
			alert(commonality_MaxLengthText);
			return ;
		}
		var json = getM4Json();
		waitMsg.show();
		Ext.Ajax.request( {
			url : "${contextPath}/sys/m4/saveM4.do",
			params : {			
				jsonData : Ext.util.JSON.encode(json),
				msiId : msiId,
				taskId : taskId
			},
			method : "POST",
			success : function(form,action) {
			      loadStep();
			      waitMsg.hide();
				  alert( commonality_messageSaveMsg);
				    if (node == ''){
				       // step[7] = 2;
				        goNext(value);
				        loadData(oldNode);
				        refreshTreeNode2();
				        parent.refreshTreeNode();
				    }else{		
				       loadData(node);
				       refreshTreeNode2();
				       parent.refreshTreeNode();
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
			value = 7;
			}
		saveTest(value,'');
	}
		
	//点击故障原因树时的操作
	function readData(node){
        saveTest('',node);	
	   }
	function saveTest(value,node){
		if (taskId!= '' &&(Ext.util.JSON.encode(getM4Json()) != Ext.util.JSON.encode(oldM4) )){	
		    //判断是否有权限修改
		   if (isMaintain!='1'){
			   alert("您没有修改权限，无权修改！");
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
						save(value,node);
	
				} else if (yesNo == 'no'){
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
	//默认加载第一条数据
	treePanelTwo.on("load", function() {
    	treePanelTwo.expandAll();
        var node = treePanelTwo.getRootNode().firstChild;
        loadData(node);
        treePanelTwo.getSelectionModel().select(node);		
	});
