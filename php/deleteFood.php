<?php
    require_once('dbConnect.php');

    $id = $_POST["id"];

    $statement = mysqli_prepare($con, "DELETE FROM foodTBL WHERE id = ?");
    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
