//根据传入的参数判断取值
function arrResult(arr,alg){		//arr:数组 alg：算法（MIN、MAX、AVG）
	if(alg == 'MIN'){
		return arrMin(arr);
	}else if(alg == 'MAX'){
		return arrMax(arr);
	}else if(alg == 'AVG'){
		return arrAvg(arr);
	}else if(alg == 'MULT'){		//乘积
		return arrMult(arr);
	}else if(alg == 'SUM'){		//求和
		return arrSum(arr);
	}
	return;
}

//计算数组的最大值
function arrMax(arr){				//arr:数组 
	var newArr = fiterArr(arr);
	return Math.max.apply(Math,newArr);
}

//计算数组的最小值
function arrMin(arr){				//arr:数组 
	var newArr = fiterArr(arr);
	return Math.min.apply(Math,newArr);
}
//计算数组的平均值
function arrAvg(arr){				//arr:数组 
	var newArr = fiterArr(arr);
	var sum=0;
	for(var i=0;i<newArr.length;i++){
		sum += newArr[i];
	}
	return Math.round(sum/newArr.length);
}
//求和
function arrSum(arr){
	var newArr = fiterArr(arr);
	var sum = 0;
	for(var i=0;i<newArr.length;i++){
		sum +=newArr[i];
	}
	return sum;
}
//计算数组的乘积
function arrMult(arr){
	var newArr = fiterArr(arr);
	var mult = 1;
	for(var i=0;i<newArr.length;i++){
		mult *=newArr[i];
	}
	return mult;
}

//去除数组中为空的数字
function fiterArr(arr){				//arr:数组 
	var newArr = new Array();
  	var index = 0;
  	for(var i=0;i<arr.length;i++){				//遍历数组去除空值
		if(arr[i]!=null){
			newArr[index] = arr[i];
			index++;
		}			
  	}
	return newArr;
}

