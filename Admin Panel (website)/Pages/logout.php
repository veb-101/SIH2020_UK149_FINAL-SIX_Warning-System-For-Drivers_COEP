<!-- <script src="https://www.gstatic.com/firebasejs/7.17.1/firebase-auth.js"></script>
<script type="text/javascript">
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

	// signout modify according to you
firebase.auth().signOut().then(function() {
    alert("Logout");
  }).catch(function(error) {
    // An error happened.
  });
</script> -->
<?php
	include 'header.php';
	session_destroy();
	//header('Location: ../login.php');
	//echo "<META HTTP-EQUIV=\"refresh\" CONTENT=\"0; URL=".$_SERVER['../login.php']."\" >";
	echo '	<script>
				window.location.replace("../index.php");
			</script>';
?>
