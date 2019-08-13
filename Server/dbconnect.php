
<?php
$servername = "localhost";
$username = "yongrok";
$password = "root";
$dbname ="testDB";


// Create connection
$conn = new mysqli($servername, $username, $password,$dbname);

mysqli_set_charset($conn,"utf8");

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
//echo "Connected successfully";

header('Content-Type: text/html; charset=utf8');


//mysql_query("set session character_set_connection=utf8;");
//mysql_query("set session character_set_results=utf8;");
//mysql_query("set session character_set_client=utf8;");

?>
