<?php include("header.php");?>
<link rel="stylesheet" href="css/bootstrap.css" type="text/css"/>

<script src="https://www.gstatic.com/firebasejs/7.16.1/firebase-app.js"></script>
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- The core Firebase JS SDK is always required and must be listed first -->
<script src="https://www.gstatic.com/firebasejs/7.16.1/firebase-app.js"></script>
<script src="https://www.gstatic.com/firebasejs/7.15.1/firebase-firestore.js"></script>

<!-- TODO: Add SDKs for Firebase products that you want to use
     https://firebase.google.com/docs/web/setup#available-libraries -->
<script src="https://www.gstatic.com/firebasejs/7.16.1/firebase-analytics.js"></script>

<script>
  // Your web app's Firebase configuration
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
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();
</script>
<script type="text/javascript" src="Location_List.js"></script>

<div class="container" style="margin: 30px 0px 50px 10px;">
    <div class="row">
        <div class="col-sm-4">
            <div style='width: 100%;'>
            <table style='overflow-x:auto;text-align: left;' border='0' cellpadding='0' cellspacing='0' class='table demo scroll' data-page-size='5'>
                <thead class='fixHeader color-gold text-center'>
                    <tr>
                        <th> Name </th>
<!--                         <th data-type="numeric" data-sort-initial="descending"> Total </th> -->
                    </tr>
                </thead>
                <tbody id="data" class='scrollContent color-gold text-center'>
                    
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="2">
                            <div class="pagination pagination-centered"></div>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
    <div class="col-sm-8" id="DisplayReport">
        
    </div>
  </div>
</div>

<?php include("footer.php");?>