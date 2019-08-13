<?php

include ("dbconnect.php");
include("login2.php");
session_start();

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if(($_SERVER["REQUEST_METHOD"] == "POST") || $android){
	$OTP = $_POST['OTP'];
}


?>
