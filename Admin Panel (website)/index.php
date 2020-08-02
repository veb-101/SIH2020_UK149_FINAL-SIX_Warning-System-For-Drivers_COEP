<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="shortcut icon" type="image/png" href="img/WSD Logo (2).png">
	<title> Warning System for Driver </title>
    
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="css/bootstrap.css">
    
    <script type="text/javascript" src="js/script.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" type="text/javascript"></script>
    <script>
      if (!window.jQuery) { document.write('<script src="js/jquery-1.9.1.min.js"><\/script>'); }
    </script>
    <script src="js/bootstrap.min.js"></script>
    <script src="https://www.gstatic.com/firebasejs/7.17.1/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/7.17.1/firebase-auth.js"></script>
    <!-- TODO: Add SDKs for Firebase products that you want to use
       https://firebase.google.com/docs/web/setup#available-libraries -->
    <script src="https://www.gstatic.com/firebasejs/7.17.1/firebase-analytics.js"></script>
</head>

<body>
<!-- <h3>SHREE SHIMPOLI KASTURPARK S. M. JAIN SANGH</h3>
<h2>SHREE MEHUL JAIN PATHSHALA</h2>
<h2>CHATURMASIK AARADHANA PATRAK</h2>
 -->
	<div class="container login-container">
        <div class="row">
            <div class="col-md-6">
  				<img src="img/WSD Logo (2).png" alt="Avatar" class="img-fluid rounded border-black">
                <h1> Warning System for Driver </h1>
            </div>
            <div class="col-md-6 login-form">
                <h2 class="text-center"> Login Form </h2>
                <div class="form-group row">
                	<label class="col-md-4 lead text-center" for="Email_Id"><h3><small> Email Id </small></h3></label>
                    <input type="email" name="Email_Id" id="Email_Id" class="col-md-7 form-control" placeholder="Enter Email Id" value="" />
                </div>
                <div class="form-group row">
                	<label class="col-md-4 lead text-center" for="Password"><h3><small> Password </small></h3></label>
                    <input type="password" name="Password" id="Password" class="col-md-7 form-control" placeholder="Enter Password" value="" />
                </div>
                <div id="Display" style="font-size: 16px;color: red;text-align: left;padding: 0px 15px;clear: both;"> </div>
                <div class="form-group row">
                    <button class="btnSubmit border-gold color-blue lead col-md-5 p-2" onclick="Login();"> Login </button>
                    <button class="btnSubmit border-gold color-blue lead col-md-5 p-2" onclick="Register();"> Register </button>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript">
    function Login() 
    {
        var firebaseConfig = {
            apiKey: "AIzaSyB3i7kBpykiGE2y6rDoODQEd7pV47ZoR7k",
            authDomain: "sihlocationadding.firebaseapp.com",
            databaseURL: "https://sihlocationadding.firebaseio.com",
            projectId: "sihlocationadding",
            storageBucket: "sihlocationadding.appspot.com",
            messagingSenderId: "90144960065",
            appId: "1:90144960065:web:2fba9d01c696b8e2fee0c0",
            measurementId: "G-GL3R0VE2P7"
          };
          firebase.initializeApp(firebaseConfig);
          firebase.analytics();

        // actual code
        var email = document.getElementById('Email_Id').value;
        var password = document.getElementById('Password').value;

        firebase.auth().signInWithEmailAndPassword(email, password)
        .then(() => window.location.assign('Pages/Verify-Location.php'))
        .catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            console.log(errorMessage,errorCode);
            // ...
        });
    }
</script>

<?php include("Pages/footer.php");?>