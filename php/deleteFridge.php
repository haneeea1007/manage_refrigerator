<?php
    require_once('dbConnect.php');

    // $code = "sn804y856638aw8v0607";
    $code = $_POST["code"];

    $statement = mysqli_prepare($con, "DELETE FROM refrigeratorTBL WHERE code = ?");
    mysqli_stmt_bind_param($statement, "s", $code);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

    mysqli_close($con);
?>
