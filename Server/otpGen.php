<?php
include('dbconnect.php');
include('login2.php');
//session_start();

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

// Function to generate OTP
function generateNumericOTP($n) {

    // Take a generator string which consist of
    // all numeric digits
    $generator = "1357902468";

    // Iterate for n-times and pick a single character
    // from generator and append it to $result

    // Login for generating a random character from generator
    //     ---generate a random number
    //     ---take modulus of same with length of generator (say i)
    //     ---append the character at place (i) from generator to result

    $otpnum = "";

    for ($i = 1; $i <= $n; $i++) {
        $otpnum .= substr($generator, (rand()%(strlen($generator))), 1);
    }

    // Return result
    return $otpnum;
}

// Main program
//$n = 4;
//print_r(generateNumericOTP($n));

if(($_SERVER["REQUEST_METHOD"] == "POST") || $android){
	//$id = mysqli_real_escape_string($conn,$_POST['ID']);
	//$password = mysqli_real_escape_string($conn,$_POST['password']);

	//$sql = "SELECT * FROM Client WHERE ID='$id'";
	//$result = mysqli_query($conn,$sql);
	//$row = mysqli_fetch_array($result,MYSQLI_ASSOC);
	//$active = $row['active'];

	//$count = mysqli_num_rows($result);
//	if(isset($_SESSION['login_user'])){
		$num = 4;
		$OTP = generateNumericOTP($num);
		$sql = "UPDATE Client SET OTP = '$OTP' WHERE ID = '$id'";
		mysqli_query($conn,$sql);
//	}
	$data = array();
	array_push($data, array('OTP'=>$OTP));
	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array("Client"=>$data),JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	echo $json;
}

echo $id;


?>
