<?php
define('HOST','localhost');
define('USER','jms1132');
define('PASS','jss4232367!');
define('DB','jms1132');

$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
mysqli_query($con,'SET NAMES utf8');
?>
