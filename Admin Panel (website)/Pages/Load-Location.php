<?php
    if(isset($_POST["Id"])) 
    {
        $View_Mem_Id = $_POST["Id"];
        echo '<div id="View_Details"></div>';
    }
?>
<script type="text/javascript">
    function approve(id)
    {
        db.collection("Location").doc(id)
            .onSnapshot(function(doc) {
                console.log(doc.data());
                db.collection("Adding").doc(id).set({
                    Name: doc.data()["Name"],
                    Address: doc.data()["Address"],
                    Area: doc.data()["Area"],
                    lat: doc.data()["lat"],
                    lng: doc.data()["lng"]
                })
                .then(function() {
                    alert("Location Approved Successfully! It will be embeded on the map.");
                    db.collection("Location").doc(id).delete().then(function() {
                        alert("Document successfully deleted from Location!");
                    }).catch(function(error) {
                        alert("Error removing document: " + error);
                    });
                })
                .catch(function(error) {
                    alert("Error writing document: " + error);
                });
            });

    }
    function reject(id)
    {
        db.collection("Location").doc(id).delete().then(function() {
            alert("Document successfully deleted from Location!");
        }).catch(function(error) {
            alert("Error removing document: " + error);
        });
    }
</script>

<script type='text/javascript' src='http://www.bing.com/api/maps/mapcontrol?callback=GetMap&key= AtEZXSXwdHgb-TnLu1iqJUEmc4Jwy6u33NRb1f2ItXgx7TxkqMNCNsOm33IZUDhP'></script>

<script>
    var id = "<?php echo $View_Mem_Id; ?>";
    db.collection("Location").doc(id)
        .onSnapshot(function(doc) {
            $('#View_Details').append(
            `
            <form method="post">
                <h3>${doc.data()["Name"]}<h3>
                <div class="form-group row">
                    <label class="col-md-3 lead text-center" for="Image"><h3><small> Image </small></h3></label>
                    <img name="Image" id="Image" class="col-md-3" alt="Image N.A." src="${doc.data()["Image"]}">
                    <label class="col-md-2 lead text-center" for="Latitude"><h3><small> Latitude </small></h3></label>
                    <input type="number" name="Latitude" id="Latitude" class="col-md-1 form-control" value="${doc.data()["lat"]}" readonly/>
                    <label class="col-md-2 lead text-center" for="Longitude"><h3><small> Longitude </small></h3></label>
                    <input type="number" name="Longitude" id="Longitude" class="col-md-1 form-control" value="${doc.data()["lng"]}" readonly/>
                </div>
                <div class="form-group row">
                    <label class="col-md-3 lead text-center" for="Address"><h3><small> Address </small></h3></label>
                    <input type="text" name="Address" id="Address" class="col-md-3 form-control" value="${doc.data()["Address"]}" readonly/>
                    <label class="col-md-3 lead text-center" for="Area"><h3><small> Area </small></h3></label>
                    <input type="text" name="Area" id="Area" class="col-md-3 form-control" value="${doc.data()["Area"]}" readonly/>
                </div>
                <div class="form-group row">
                    <label class="col-md-2 lead text-center" for="Yes"><h3><small> Yes </small></h3></label>
                    <input type="text" name="Yes" id="Yes" class="col-md-1 form-control" value="${doc.data()["Yes"]}" readonly/>
                    <label class="col-md-2 lead text-center" for="Yes_Prob"><h3><small> Probablity </small></h3></label>
                    <input type="text" name="Yes_Prob" id="Yes_Prob" class="col-md-1 form-control" value="${(doc.data()["Yes"] / (doc.data()["Yes"]+doc.data()["No"])) * 100 }" readonly/>
                    <label class="col-md-2 lead text-center" for="No"><h3><small> No </small></h3></label>
                    <input type="text" name="No" id="No" class="col-md-1 form-control" value="${doc.data()["No"]}" readonly/>
                    <label class="col-md-2 lead text-center" for="Yes_Prob"><h3><small> Probablity </small></h3></label>
                    <input type="text" name="No_Prob" id="No_Prob" class="col-md-1 form-control" value="${(doc.data()["No"] / (doc.data()["Yes"]+doc.data()["No"])) * 100 }" readonly/>
                </div>
                <div id="Display" style="font-size: 16px;color: red;text-align: left;padding: 0px 15px;clear: both;"> </div>
                <div class="form-group row">
                    <a name="Approve" id="Approve" onclick="approve('${doc.id}');" class="btnSubmit border-black color-red lead col-md-3 text-center"> Approve </a>
                    <a name="Reject" id="Reject"  onclick="reject('${doc.id}');"class="btnSubmit border-black color-red lead col-md-3 text-center"> Reject </a>
                </div>
            </form>
            `
        )
    });
</script>