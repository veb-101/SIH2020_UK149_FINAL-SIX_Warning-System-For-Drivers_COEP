const db = firebase.firestore();



db.collection("Location").onSnapshot(function(querySnapshot) {
    $('#data').empty();
    $('#DisplayReport').empty();
    querySnapshot.forEach(function(doc) {
        $('#data').append(
            `
            <tr><td><a id="${doc.id}">${doc.data()['Name']}</a></td></tr>
            <script>
            $(document).ready(function(){
                    $("#${doc.id}").click(function(){
                        $.ajax({
                            type: "POST",
                            url: "Load-Location.php",
                            data: "Id=${doc.id}",
                            context: document.body,
                            success: function(result)
                            {
                                $("#DisplayReport").html(result);
                            }
                        });
                    });
                });
            </script>
            `
        )
        // console.log(doc.id, " => ", doc.data());
    });
});
