<?php
    require_once('dbConnect.php');

    $id = $_POST["id"];
    $code = $_POST["code"];

    $statement = mysqli_prepare($con, "INSERT INTO managementTBL VALUES (?,?)");
    mysqli_stmt_bind_param($statement, "ss", $id, $code);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
