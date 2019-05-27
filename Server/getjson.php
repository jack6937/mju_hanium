<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbconnect.php');

   // $stmt = $conn->prepare('select * from Client');
   // $stmt->execute();
    $check = 'select * from Client';
    $result = mysqli_query($conn,$check);
    $num_rows = mysqli_num_rows($result);

    if ($num_rows > 0)
    {
        $data = array();

        while($row=$result->fetch_assoc())
        {
            extract($row);
            array_push($data,
                array('ID'=>$ID,
                'name'=>$name,
                'HP'=>$HP,
		'OTP'=>$OTP
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("Client"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }

?>
