<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="shortcut icon" type="image/png" href="../img/WSD Logo (2).png">
    <title> Warning System for Driver </title>
    
    <link rel="stylesheet" type="text/css" href="../css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="../css/bootstrap.css">
    
    <script type="text/javascript" src="../js/script.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" type="text/javascript"></script>
    <script>
        if (!window.jQuery) { document.write('<script src="js/jquery-1.9.1.min.js"><\/script>'); }
    </script>
    <script src="../js/bootstrap.min.js"></script>
</head>

<body>
<nav class="navbar navbar-expand-sm navbar-dark">
    <div class="row pad-10px" id="header">
        <a class="navbar-brand" href="#">
            <img src="../img/WSD Logo (2) - Header.png" alt="Avatar" class="navbar-brand border-black header-img">
        </a>
        <span class="navbar-text bg-white border-black pad-10px" id="logout">
            <h4 class="text-center">
                <small>User Name: Mumbai BMC</small>
            </h4>
            <span class="logout-btn col-md-12"><a class="text-center" href="logout.php"><h4><small> Logout </small></h4></a></span>
        </span>
        <button class="navbar-toggler ml-auto border-black" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>
    </div>
</nav>

<!-- The Modal -->
<div class="modal fade" id="myModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header text-center">
                <h2 id="modalHeader"> </h2>
                <button type="button" class="close" data-dismiss="modal">×</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
                <h3 id="modalBody"> </h3>
            </div>
            <!-- Modal footer -->        
            <div class="modal-footer text-center">
                <h5 style="margin: 0 auto;"> Have a nice day! </h5>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var url = "";
    // Get the modal
    var modal = document.getElementById('myModal');
    // Get the button that opens the modal
    //var btn = document.getElementById("submit");
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];
    // When the user clicks the button, open the modal 
    /*btn.onclick = function()
    {
        modal.style.display = "block";
    }*/
    // When the user clicks on <span> (x), close the modal
    span.onclick = function()
    {
        modal.style.display = "none";
        window.location.replace(url);
        //location.href = "http://localhost/Main_Site/pages/edit_emp.php";
    }
    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event)
    {
        if (event.target == modal)
        {
            modal.style.display = "none";
        }
    }

function showPopup(x,y,z)
{
    var headtxt = x ;
    var bodytxt = y ;
    url = z;
    
    document.getElementById("modalHeader").innerText = headtxt;
    document.getElementById("modalBody").innerText = bodytxt;
    $("#myModal").modal("show");
    // document.getElementById("myModal").className = "modal show";
    // document.getElementById("myModal").style.display = "block !important";
}
</script>