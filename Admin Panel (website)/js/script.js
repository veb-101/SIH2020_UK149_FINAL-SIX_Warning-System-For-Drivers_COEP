function show()
{
	var show = document.getElementById("Password").type;
	var Eye = document.getElementById("Eye");
   
	if(show == "password")
	{
		document.getElementById("Password").type = "text";
		Eye.classList.remove("fa-eye");
		Eye.classList.add("fa-eye-slash");
		document.getElementById("Eye").style.color = "red";
	}
	else
	{
		document.getElementById("Password").type = "password";
		Eye.classList.add("fa-eye");
		Eye.classList.remove("fa-eye-slash");
		document.getElementById("Eye").style.color = "black";
	}
}

function showSignup()
{
	var show = document.getElementById("Stud_Password").type;
	var Eye = document.getElementById("Eye");
   
	if(show == "password")
	{
		document.getElementById("Stud_Password").type = "text";
		Eye.classList.remove("fa-eye");
		Eye.classList.add("fa-eye-slash");
		document.getElementById("Eye").style.color = "red";
	}
	else
	{
		document.getElementById("Stud_Password").type = "password";
		Eye.classList.add("fa-eye");
		Eye.classList.remove("fa-eye-slash");
		document.getElementById("Eye").style.color = "black";
	}
}

var red = "1px solid red !important";
var normal = "1px solid #015600";
var flag = 0,n = 0;

function lengthDefine(inputtext, alertMsg, min, max, i)
{
	var uInput = inputtext.value;
	var numericExpression = /^[0-9]+$/;
	if (inputtext.value.length == 0)
	{
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		if (inputtext.value.match(numericExpression))
		{
			if (uInput.length >= min && uInput.length <= max)
			{
				var x = document.getElementsByClassName("focus");
				x[i].style.border = normal ; 
				return true;
			}
			else
			{
				alertMsg = "* Number should be between " + min + " and " + max + " digits only. *";
				document.getElementById('Display').innerText = alertMsg;
				var x = document.getElementsByClassName("focus");
				x[i].style.border = red ;
				alert(alertMsg);
				inputtext.focus();
				return false;
			}
		}
		else
		{
			alertMsg = "* Please enter numbers only for this field. *";
			document.getElementById('Display').innerText = alertMsg;
			var x = document.getElementsByClassName("focus");
			x[i].style.border = red ;
			alert(alertMsg);
			inputtext.focus();
			return false;
		}
	}
}

function inputAlphabet(inputtext, alertMsg, i)
{
	var alphaExp = /^[a-zA-Z ]+$/;
	if (inputtext.value.length == 0)
	{
		alertMsg = "* Please Enter Value. *";
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		if (inputtext.value.match(alphaExp))
		{
			var x = document.getElementsByClassName("focus");
			x[i].style.border = normal;
			return true;
		}
		else
		{
			document.getElementById('Display').innerText = alertMsg; 
			var x = document.getElementsByClassName("focus");
			x[i].style.border = red;
			alert(alertMsg);
			inputtext.focus();
			return false;
		}
	}
}

function emailValidation(inputtext, alertMsg, i)
{
	var emailExp = /^[a-z][a-zA-Z0-9_.]*(\.[a-zA-Z][a-zA-Z0-9_.]*)?@[a-z][a-zA-Z-0-9]*\.[a-z]+(\.[a-z]+)?$/;
	if (inputtext.value.length == 0)
	{
		alertMsg = "* Please Enter Email. *";
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		if (inputtext.value.match(emailExp))
		{
			var x = document.getElementsByClassName("focus");
			x[i].style.border = normal;
			return true;
		}	 
		else 
		{
			document.getElementById('Display').innerText = alertMsg;
			var x = document.getElementsByClassName("focus");
			x[i].style.border = red;
			alert(alertMsg);
			inputtext.focus();
			return false;
		}
	}
}

function checkContain(inputtext, alertMsg, i)   
{   
	if (inputtext.value.length == 0)
	{
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		var x = document.getElementsByClassName("focus");
		x[i].style.border = normal;
		return true;  
	} 	 
}

function textNumeric(inputtext, alertMsg, min, max, i) 
{
	var numericExpression = /^[0-9]+$/;
	var uInput = inputtext.value;
	if (inputtext.value.length == 0)
	{
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		if (inputtext.value.match(numericExpression))
		{
			if (uInput.length >= min && uInput.length <= max)
			{
				var x = document.getElementsByClassName("focus");
				x[i].style.border = normal;
				return true;
			}
			else
			{
				alertMsg = "* Phone number should be between " + min + " and " + max + " digits only.*";
				document.getElementById('Display').innerText = alertMsg;
				alert(alertMsg);
				inputtext.focus();
				var x = document.getElementsByClassName("focus");
				x[i].style.border = red ;
				return false;
			}
		}	 
		else 
		{
			alertMsg = "* Please enter digits only for Phone Number. *";
			document.getElementById('Display').innerText = alertMsg; 
			var x = document.getElementsByClassName("focus");
			x[i].style.border = red;
			alert(alertMsg);
			inputtext.focus();
			return false;
		}
	}
}

function checkDate(inputtext, alertMsg, i)   
{   
	if (inputtext.value.length == 0)
	{
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		var x = document.getElementsByClassName("focus");
		x[i].style.border = normal;
		return true;  
	} 	 
}

function compareDate(inputtext, inputtext1, inputtext2, alertMsg, i, j)   
{
	var From_Date = new Date(inputtext1.value);
	var To_Date = new Date(inputtext2.value);
	var Days = inputtext.value - 1;
 
	if (From_Date > To_Date)
	{
    	document.getElementById('Display').innerText = alertMsg;
		inputtext1.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		var oneDay = 24*60*60*1000; // hours*minutes*seconds*milliseconds
		var tempDays = Math.abs((From_Date.getTime() - To_Date.getTime())/(oneDay));
		if(tempDays == Days)
		{
   			var x = document.getElementsByClassName("focus");
			x[i].style.border = normal;
			return true;
		}
		else
		{
			document.getElementById('Display').innerText = "* Number of Days are not proper between From and To Date. *";
			inputtext2.focus();
			var x = document.getElementsByClassName("focus");
			x[i].style.border = red ;
			x[j].style.border = red ;
			return false;
		}
	}
}	

function trueSelection(inputtext, alertMsg, i)
{
	if (inputtext.options[inputtext.selectedIndex].value == "")
	{
		document.getElementById('Display').innerText = alertMsg; 
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red;
		alert(alertMsg);
		inputtext.focus();
		return false;
	}	 
	else 
	{
		var x = document.getElementsByClassName("focus");
		x[i].style.border = normal;
		return true;
	}
}

function trueCheck(inputtext, alertMsg, i)
{
	if (inputtext.checked == false)
	{
		document.getElementById('Display').innerText = alertMsg; 
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red;
		alert(alertMsg);
		inputtext.focus();
		return false;
	}	 
	else 
	{
		var x = document.getElementsByClassName("focus");
		x[i].style.border = normal;
		return true;
	}
}

function checkPassword(inputtext , alertMsg, i)   
{   
	var passw =  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,15}/; 
	if (inputtext.value.length == 0)
	{
		alertMsg = "* Please Enter Password. *";
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		if(inputtext.value.match(passw))   
		{
			var x = document.getElementsByClassName("focus");
			x[i].style.border = normal;
			return true;  
		} 	 
		else  
		{   
			document.getElementById('Display').innerText = alertMsg;
			var x = document.getElementsByClassName("focus");
			x[i].style.border = red;
			alert(alertMsg); 
			inputtext.focus();
			return false;  
		}  
	} 
}

function enter(inputtext, alertMsg, i)   
{   
	if (inputtext.value.length == 0)
	{
		document.getElementById('Display').innerText = alertMsg;
		alert(alertMsg);
		inputtext.focus();
		var x = document.getElementsByClassName("focus");
		x[i].style.border = red ;
		return false;
	}
	else
	{
		var x = document.getElementsByClassName("focus");
		x[i].style.border = normal;
		return true;  
	} 	 
}

function SignUpFormValidation(form)
{
	var lname = document.getElementById('lname');
	var fname = document.getElementById('fname');
	var mname = document.getElementById('mname');
	var momname = document.getElementById('momname');
	
	var D_O_B = document.getElementById('D_O_B');
	//var Nat = document.getElementById('Nat');
	var N_O_M = document.getElementById('N_O_M');
	
	var Mob = document.getElementById('Mob');
	var wMob = document.getElementById('wMob');
	var Email = document.getElementById('Email');
	
	var Flat_No = document.getElementById('Flat_No');
	var Bldg = document.getElementById('Bldg');

	var Work = document.getElementById('Work');
	var Edu = document.getElementById('Edu');
	var Dha_Edu = document.getElementById('Dha_Edu');

	var Bld_Grp = document.getElementById('Bld_Grp');
	var R_W_A = document.getElementById('R_W_A');
	// var Lan_Com = document.getElementById('Lan_Com');

	// alert(Lan_Com);

	var i;
	var Ln;var Fn;var Mn;var Momn;var D;var B;
	var R;/*var M;var Wm;var Em;*/var W;var E;var De;
	var Ln_A = [];var Fn_A = [];var Mn_A = [];var Momn_A = [];
	var D_O_B_A = [];var Bld_Grp_A = [];var R_W_A_A = [];
	//var Mob_A = [];var wMob_A = [];var Email_A = [];
	var Work_A = [];var Edu_A = [];var Dha_Edu_A = [];
	
	//Number of Family Members for (i = 1; i <= N_O_M.value; i++)
    for (i = 1; i < N_O_M.value; i++) 
	{
		Ln = 'lname' + i;Fn = 'fname' + i;Mn = 'mname' + i;Momn = 'momname' + i;
		D = 'D_O_B' + i;B = 'Bld_Grp' + i;R = 'R_W_A' + i;
		//M = 'Mob' + i;Wm = 'wMob' + i;Em = 'Email' + i;
		W = 'Work' + i;E = 'Edu' + i;De = 'Dha_Edu' + i;
		
		Ln_A[i] = document.getElementById(Ln);
		Fn_A[i] = document.getElementById(Fn);
		Mn_A[i] = document.getElementById(Mn);
		Momn_A[i] = document.getElementById(Momn);
		D_O_B_A[i] = document.getElementById(D);
		Bld_Grp_A[i] = document.getElementById(B);
		R_W_A_A[i] = document.getElementById(R);
		//Mob_A[i] = document.getElementById(M);
		//wMob_A[i] = document.getElementById(Wm);
		//Email_A[i] = document.getElementById(Em);
		Work_A[i] = document.getElementById(W);
		Edu_A[i] = document.getElementById(E);
		Dha_Edu_A[i] = document.getElementById(De);
		
	}
	
	if(lengthDefine(N_O_M, "* Please Enter number for Number of Family Members. *", 1, 2, n++)){
	if(inputAlphabet(lname, "* Only Alphabets and blankspace is allowed for Surname. *", n++)){
	if(inputAlphabet(fname, "* Only Alphabets and blankspace is allowed for First Name. *", n++)){
	if(inputAlphabet(mname, "* Only Alphabets and blankspace is allowed for Father's Name. *", n++)){
	if(inputAlphabet(momname, "* Only Alphabets and blankspace is allowed for Mother's Name. *", n++)){
	if(checkDate(D_O_B, "* Please Enter Date of Birth. *", n=n+2)){
	
	if(enter(Flat_No, "* Please Enter Flat No. & Wing. *", n=n+2)){
	if(trueSelection(Bldg, "* Please Select Building. *", n=n+1)){
		if(Bldg.value == 99999){
			var Bldg_Name = document.getElementById('Bldg_Name');
			var Addr = document.getElementById('Addr');
			var Area = document.getElementById('Area');

			if(checkContain(Bldg_Name, "* Please enter value for Building Name. *", n++)){
			if(checkContain(Addr, "* Please enter value for Address. *", n++)){
			if(trueSelection(Area, "* Please Select Landmark. *", n++)){
				//trueSelection
			}else{return false;}
			}else{return false;}
			}else{return false;}
			//n = n + 3; // n=17
		}
		else{
			n = n + 8; // n=20
		}

	if(textNumeric(Mob, "* Please Enter Mobile Number. *", 8, 11, n++)){
	if(textNumeric(wMob, "* Please Enter Whatsaap Number. *", 8, 11, n++)){
	if(emailValidation(Email, "* Please enter a valid email address. *", n++)){
	if(checkContain(Work, "* Only Alphabets and blankspace is allowed for Occupation. *", n++)){
	if(checkContain(Edu, "* Only Alphabets and blankspace is allowed for Education. *", n++)){
	if(trueSelection(Dha_Edu, "* Please Select Dharmik Education. *", n++)){
		if(Dha_Edu.value == 99999){
			var Course_Name = document.getElementById('Course_Name');
			if(checkContain(Course_Name, "* Please enter value for Course Name. *", n++)){
			}
			else{return false;}
			//n = n + 1 // n=21 or 24
		}
	if(trueSelection(Bld_Grp, "* Please Select Blood Group. *", n=n+3)){
	if(trueSelection(R_W_A, "* Please Select Relation with above. *", n++)){
		if(R_W_A.value == 999){
			var Relation_Name = document.getElementById('Relation_Name');
			if(checkContain(Relation_Name, "* Please enter value for Relation Name. *", n++)){
			}
			else{return false;}
			//n = n + 1 // n=24 or 27
		}
	// if(trueCheck(Lan_Com, "* Please select atleast one Language for Communication. *", n++)){
	// 	n = n + 2;
	if(N_O_M.value > 1)
	{
		//Number of Family Members for (i = 1; i <= N_O_M.value; i++)
        for (i = 1; i < N_O_M.value; i++) 
		{
			if(inputAlphabet(Ln_A[i], "* Only Alphabets and blankspace is allowed for Surname. *", n++)){
			if(inputAlphabet(Fn_A[i], "* Only Alphabets and blankspace is allowed for First Name. *", n++)){
			if(inputAlphabet(Mn_A[i], "* Only Alphabets and blankspace is allowed for Father's Name. *", n++)){
			if(inputAlphabet(Momn_A[i], "* Only Alphabets and blankspace is allowed for Mother's Name. *", n++)){
			if(checkDate(D_O_B_A[i], "* Please Enter Date of Birth. *", n=n+2)){
			if(trueSelection(Bld_Grp_A[i], "* Please Select Blood Group. *", n++)){
			if(trueSelection(R_W_A_A[i], "* Please Select Relation with above. *", n++)){
				if(R_W_A_A[i].value == 999){
					var Relation_Name = "Relation_Name" + i;
					var Relation_Name = document.getElementById(Relation_Name);
					if(checkContain(Relation_Name, "* Please enter value for Relation Name. *", n++)){
					}
					else{flag = 0;break;return false;}
				}
			if(checkContain(Work_A[i], "* Only Alphabets and blankspace is allowed for Occupation. *", n++)){
			if(checkContain(Edu_A[i], "* Only Alphabets and blankspace is allowed for Education. *", n++)){
			if(trueSelection(Dha_Edu_A[i], "* Please Select Dharmik Education. *", n++)){
				if(Dha_Edu_A[i].value == 99999){
					var Course_Name = "Course_Name" + i;
					var Course_Name = document.getElementById(Course_Name);
					if(checkContain(Course_Name, "* Please enter value for Course Name. *", n++)){
					}
					else{flag = 0;break;return false;}
				}
			// if(textNumeric(Mob_A[i], 8, 11, n++)){
			// if(emailValidation(Email_A[i], "* Please enter a valid email address. *", n++)){
				flag = 1;
			}else{flag = 0;break;}
			 }else{flag = 0;break;}
			  }else{flag = 0;break;}
			   }else{flag = 0;break;}
			    }else{flag = 0;break;}
			     }else{flag = 0;break;}
			      }else{flag = 0;break;}
			       }else{flag = 0;break;}
			        }else{flag = 0;break;}
			         }else{flag = 0;break;}
		}
	} 
	else
	{
		flag = 1;
	}

	} } } } } } } } } } } } } } } }

    if(flag==1)
    {
    	n=0;flag=0;
    	return true;
    	form.submit();
    }
    else
    {
    	n=0;flag=0;
    	return false;
    }
    n=0;flag=0;
	return false;
}


function deleletconfig()
{
	var del=confirm("Are you sure you want to delete this record?");
    if (del == false)
    {
       alert("Record Not Deleted");
       return false;
    }
    else
    {
    	return true;
    }
}

function addTileValidation()
{
	var Tile_Code = document.getElementById('Tile_Code');
	var Description  = document.getElementById('Description');
	var Link = document.getElementById('Link');

	if (lengthDefine(Tile_Code, 1, 2, 0))
	{
		if (enter(Description, "* Please Enter Tile Description. *", 1))
		{
			if (enter(Link, "* Please Enter Tile Link. *", 2))
			{
				return true;
				document.addTileForm.submit();
			}
		}
	}
	return false;
}

function editTileValidation()
{
	var Tile_Code = document.getElementById('Tile_Code');
	var Description  = document.getElementById('Description');
	var Link = document.getElementById('Link');

	if (lengthDefine(Tile_Code, 4, 5, 0))
	{
		if (enter(Description, "* Please Enter Tile Description. *", 1))
		{
			if (enter(Link, "* Please Enter Tile Link. *", 2))
			{
				return true;
				document.editTileForm.submit();
			}
		}
	}
	return false;
}

function addTileAuthorityValidation()
{
	var Sr_No = document.getElementById('Sr_No');
	var Tile_Code = document.getElementById('Tile_Code');
	var Div_Code  = document.getElementById('Div_Code');
	var User = document.getElementById('User');
	var Type = document.getElementById('Type');

	if (lengthDefine(Sr_No, 1, 2, 0))
	{
		if (trueSelection(Tile_Code, "* Please Select Tile Code. *", 1))
		{
			if (trueSelection(Div_Code, "* Please Select Tile Group Code. *", 2))
			{
				if (trueSelection(User, "* Please Select User. *", 3))
				{
					if (trueSelection(Type, "* Please Select Type. *", 4))
					{
						return true;
						document.addTileAuthorityForm.submit();
					}
				}
			}
		}
	}
	return false;
}

function editTileAuthorityValidation()
{
	var Sr_No = document.getElementById('Sr_No');
	var Tile_Code = document.getElementById('Tile_Code');
	var Div_Code  = document.getElementById('Div_Code');
	var User = document.getElementById('User');
	var Type = document.getElementById('Type');

	if (trueSelection(Sr_No, "* Please Select Sr_No. *", 0))
	{
		if (trueSelection(Tile_Code, "* Please Select Tile Code. *", 1))
		{
			if (trueSelection(Div_Code, "* Please Select Tile Group Code. *", 2))
			{
				if (trueSelection(User, "* Please Select User. *", 3))
				{
					if (trueSelection(Type, "* Please Select Type. *", 4))
					{
						return true;
						document.editTileAuthorityForm.submit();
					}
				}
			}
		}
	}
	return false;
}

function addTileGroupValidation()
{
	var Div_Code = document.getElementById('Div_Code');
	var Description  = document.getElementById('Description');
	var Icon = document.getElementById('Icon');

	if (lengthDefine(Div_Code, 4, 5, 0))
	{
		if (enter(Description, "* Please Enter Tile Group Description. *", 1))
		{
			if (enter(Icon, "* Please Enter Tile Group Icon. *", 2))
			{
				return true;
				document.addTileGroupForm.submit();
			}
		}
	}
	return false;
}

function editTileGroupValidation()
{
	var Div_Code = document.getElementById('Div_Code');
	var Description  = document.getElementById('Description');
	var Icon = document.getElementById('Icon');

	if (lengthDefine(Div_Code, 4, 5, 0))
	{
		if (enter(Description, "* Please Enter Tile Group Description. *", 1))
		{
			if (enter(Icon, "* Please Enter Tile Group Icon. *", 2))
			{
				return true;
				document.editTileGroupForm.submit();
			}
		}
	}
	return false;
}

function addTileGroupAuthorityValidation()
{
	var Sr_No = document.getElementById('Sr_No');
	var Div_Code  = document.getElementById('Div_Code');
	var User = document.getElementById('User');
	var Type = document.getElementById('Type');

	if (lengthDefine(Sr_No, 1, 2, 0))
	{
		if (trueSelection(Div_Code, "* Please Select Tile Group Code. *", 1))
		{
			if (trueSelection(User, "* Please Select User. *", 2))
			{
				if (trueSelection(Type, "* Please Select Type. *", 3))
				{
					return true;
					document.addTileGroupAuthorityForm.submit();
				}
			}
		}
	}
	return false;
}

function editTileGroupAuthorityValidation()
{
	var Sr_No = document.getElementById('Sr_No');
	var Div_Code  = document.getElementById('Div_Code');
	var User = document.getElementById('User');
	var Type = document.getElementById('Type');

	if (trueSelection(Sr_No, "* Please Select Sr_No. *", 0))
	{
		if (trueSelection(Div_Code, "* Please Select Tile Group Code. *", 1))
		{
			if (trueSelection(User, "* Please Select User. *", 2))
			{
				if (trueSelection(Type, "* Please Select Type. *", 3))
				{
					return true;
					document.editTileGroupAuthorityForm.submit();
				}
			}
		}
	}
	return false;
}

function passwordVerify()
{
	var newPassword = document.getElementById('newPassword');
	var rePassword = document.getElementById('rePassword');

	if(newPassword.value == rePassword.value)
	{
		return true;
		document.newPasswordForm.submit();
	}
	else
	{
		document.getElementById('Display').innerText = '* New Password and Confirm Password does not match. *';
		var x = document.getElementsByClassName("focus");
		x[0].style.border = red ;
		return false;
	}
	return false;
}