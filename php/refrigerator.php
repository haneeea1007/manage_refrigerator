<?php
     require_once('dbConnect.php');

    $code = $_POST["code"];
    $name = $_POST["name"];
    $type = $_POST["type"];

    $statement = mysqli_prepare($con, "INSERT INTO refrigeratorTBL VALUES (?,?,?)");
    mysqli_stmt_bind_param($statement, "sss", $code, $name, $type);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
