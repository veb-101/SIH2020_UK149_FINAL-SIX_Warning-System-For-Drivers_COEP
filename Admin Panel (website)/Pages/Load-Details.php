<?php include 'config.php';?>
<?php include 'Queries.php';?>

<?php
    $count = 0;
    $print = 0;
    if (isset($_POST["Family_Id"])) 
    {
        $Family_Id = $_POST["Family_Id"];
        echo '<div class="form-group row">
                <label class="col-md-4 lead text-center" for="Mem_Id"><h3><small> Select Member </small></h3></label>
                <select class="col-md-8 form-control" id="Mem_Id" name="Mem_Id">';   
        
        $resultFam->data_seek(0);
        while($rowFam = mysqli_fetch_array($resultFam))
        {
            if($rowFam["Family_Id"] == $Family_Id)
            {
                $D_O_B = new DateTime($rowFam["D_O_B"]);
                $Curr_Date = new DateTime(date("Y-m-d"));
                $age = ($D_O_B->diff($Curr_Date))->y;

                $User_Id = $rowFam["Mem_Id"];
                $Name = $rowFam["fname"] ." ". $rowFam["mname"] ." ". $rowFam["lname"];
                if ($age >= 5 and $age <= 16)
                {
                    $print += 1;
                    echo '<option value="'.$User_Id.'"> '.$Name.' (Age: '.$age.') </option>';
                }
                $count += 1;
            }
        }
        echo '  </select>
        </div>
        <div class="form-group row">
            <label class="col-md-8 lead text-center"><h3><small> Mehul Jain Pathshala student? </small></h3></label>
            <div class="" style="margin: 0 auto;display: grid;">
            <div class="form-check-inline">
              <label class="form-check-label">
                <input type="checkbox" class="form-check-input checkbox-2x" name="MJP_Mem" value="Yes">&nbsp;&nbsp;<h4 class="d-inline"> Yes </h4>
              </label>
            </div>&nbsp;&nbsp;
            <div class="form-check-inline">
              <label class="form-check-label">
                <input type="checkbox" class="form-check-input checkbox-2x" name="MJP_Mem" value="No">&nbsp;&nbsp;<h4 class="d-inline"> No </h4>
              </label>
            </div>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-md-4 lead text-center"><h3><small> Password </small></h3></label>
            <input type="password" name="Password" id="Password" class="col-md-8 form-control" placeholder="Enter Password" value="" />
        </div>
        <div class="form-group row">
            <label class="col-md-4 lead text-center"><h3><small> Re-enter Password </small></h3></label>
            <input type="password" name="Re_Password" id="Re_Password" class="col-md-8 form-control" placeholder="Enter Password Again" value="" />
        </div>
        <div id="Display" style="font-size: 16px;color: red;text-align: left;padding: 0px 15px;clear: both;"> </div>
        <div class="form-group row">
            <input type="submit" name="Register" id="Register" class="btnSubmit border-gold color-blue lead col-md-6 align-self-center p-2" value="Register" />
        </div>
        <div class="form-group">
            <a href="index.php" class="ForgetPwd" value=""><h4> Existing User? Click Here </h4></a>
        </div>';
        if ($print == 0)
        {
            echo "<script> document.getElementById('Display').innerText = '* No Family Member age is under 15 years. *' </script> ";        
            echo'<div class="form-group">
                    <a href="signupForm.php" class="ForgetPwd" value=""><h4> Click Here to go back </h4></a>
                </div>';
        }
        if ($count == 0)
        {
            echo "<script> document.getElementById('Display').innerText = '* Please Enter Valid Family ID. *' </script> ";        
            echo'<div class="form-group">
                    <a href="signupForm.php" class="ForgetPwd" value=""><h4> Click Here to go back </h4></a>
                </div>';
        }
    }
?>