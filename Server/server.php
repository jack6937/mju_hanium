<?php
session_start();
//define("_IP",    "0.0.0.0");
define("_PORT",  "25004");
include("open.php");
include("dbconnect.php");
include("login2.php");


$sSock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
socket_bind($sSock, $servername, _PORT);
socket_listen($sSock);

$sql = "SELECT * FROM Client WHERE ID='$id' and OTP='$OTP'";
$result1 = mysqli_query($conn,$sql);
$count = mysqli_num_rows($result1);

if($count == 1){
	while($cSock = socket_accept($sSock)){
		socket_write($cSock,$OTP);
	}
}


?>

