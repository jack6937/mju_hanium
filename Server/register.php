<?php

include('dbconnect.php');

// Create connection
//$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
//if ($conn->connect_error) {
//    die("Connection failed: " . $conn->connect_error);
//}

$android = strpos($_SERVER['HTTP_USER_AGENT'],'Android');

if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']))||$android)
    {
	$id =$_POST['ID'];
        $name=$_POST['name'];
        $password=$_POST['password'];
	$hp = $_POST['HP'];

        if(empty($id)){
            $errMSG = "아이디를  입력하세요.";
        }
	else if(empty($name)){
            $errMSG = "이름을 입력하세요.";
        }
	else if(empty($password)){
            $errMSG = "비밀번호를 입력하세요.";
        }
	else if(empty($hp)){
            $errMSG = "핸드폰번호를 입력하세요.";
        }

	// OTP 랜덤 생성
	$otpNum = mt_rand(1000,9999);
	// id 중복 체크
	$check ="SELECT * FROM Client WHERE ID='$id'";
	$result = mysqli_query($conn,$check);
	$num_rows = mysqli_num_rows($result);

	if(!isset($errMSG))
        {
		//try{
          	if($num_rows>0){
               		// $stmt = $conn->prepare('INSERT INTO Client VALUES(?,?,?,?,?)');
      			// $stmt->bind_param('ssssi',$id,$name,$password,$hp,$otpNum);
		  	// $stmt->execute();
               		// $successMSG = "새로운 사용자를 추가했습니다.";
			$errMSG = "아이디 중복";
                }

                else
                {	$sql = "INSERT into Client(ID,name,password,HP,OTP) VALUES('$id', '$name', '$password','$hp','$otpNum')";
			mysqli_query($conn,$sql);
			$successMSG = " 사용자 추가함";

                    //$errMSG = "아이디 중복";
                }

            // } catch(Exception $e) {
            //     die("Database error: " . $e->getMessage()); 
            // }
        }
    }


?>

        <?php
        if (isset($errMSG)) echo $errMSG;
        if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'],'Android');
	if(!$android)
	{
        ?>

        <form action="<?php $_PHP_SELF ?>" method="POST">
	    ID:<input type="text" name="ID"/>
            Name: <input type = "text" name = "name" />
            password: <input type = "text" name = "password" />
	    HP: <input type="text" name="HP" />
            <input type = "submit" name = "submit" />
        </form>

   </body>
</html>
<?php
}
?>

