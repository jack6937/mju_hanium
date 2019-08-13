
<?php
   include("dbconnect.php");
   session_start();

   $android = strpos($_SERVER['HTTP_USER_AGENT'],"Android");

   if(($_SERVER["REQUEST_METHOD"] == "POST") || $android) {
        //username and password sent from form

        $id = mysqli_real_escape_string($conn,$_POST['ID']);
        $password = mysqli_real_escape_string($conn,$_POST['password']);

        $sql = "SELECT * FROM Client WHERE ID = '$id' and password = '$password'";
	$result = mysqli_query($conn,$sql);
	$row = mysqli_fetch_array($result,MYSQLI_ASSOC);
        $active = $row['active'];

        $count = mysqli_num_rows($result);

      // If result matched $myusername and $mypassword, table row must be 1 row

      if($count == 1) {
         //session_register("id");
 
       $_SESSION['login_user'] = $id;

	 $error= 'login success';

		//echo "login success";
//         header("location: welcome.php");
      }else {
	$sqlID = "SELECT * FROM Client WHERE ID='$id'";
	$resultID = mysqli_query($conn,$sqlID);
	$countID = mysqli_num_rows($resultID);
	if($countID == 1) $error = 'Password is invalid';
	else $error = 'ID is invalid';

       // $error = "Your Login Name or Password is invalid";
	//echo "login fail";
      }
   }
?>


<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'],"Android");
if( !$android)
{
?>

<html>

   <head>
      <title>Login Page</title>

      <style type = "text/css">
         body {
            font-family:Arial, Helvetica, sans-serif;
            font-size:14px;
         }
         label {
            font-weight:bold;
            width:100px;
            font-size:14px;
         }
         .box {
            border:#666666 solid 1px;
         }
      </style>
   </head>
   <body bgcolor = "#FFFFFF">
      <div align = "center">
         <div style = "width:300px; border: solid 1px #333333; " align = "left">
            <div style = "background-color:#333333; color:#FFFFFF; padding:3px;"><b>Login</b></div>

            <div style = "margin:30px">

               <form action = "" method = "post">
                  <label>ID  :</label><input type = "text" name = "ID" class = "box"/><br /><br />
                  <label>Password  :</label><input type = "password" name = "password" class = "box" /><br/><br />
                  <input type = "submit" value = " Submit "/><br />
               </form>
               <div style = "font-size:11px; color:#cc0000; margin-top:10px"><?php echo $error; ?></div>

            </div>
         </div>
      </div>
   </body>
</html>

<?php
}
?>
