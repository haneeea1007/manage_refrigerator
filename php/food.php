<?php
    //mysqli_connect([아이피], [아이디], [비밀번호], [DB명]);

     require_once('dbConnect.php');

    $now = DateTime::createFromFormat('U.u', microtime(true));
    $title = $now->format('YmdHisu');

    $image = $_REQUEST['image'];
    $content = $_POST["content"];

    $id = $_POST["id"];
        $category = $_POST["category"];
        $section = $_POST["section"];
        $name = $_POST["name"];
        $memo = $_POST["memo"];
        $purchaseDate = $_POST["purchaseDate"];
        $expirationDate = $_POST["expirationDate"];
        $code = $_POST["code"];
        $place = $_POST["place"];
        $alarmID = $_POST["alarmID"];

    $upload_folder="Images";
    $path = "$upload_folder/$title.png";
    $actualpath = "http://jms1132.dothome.co.kr/$path";


   if(file_put_contents($path, base64_decode($image))){

    $statement = mysqli_prepare($con, "INSERT INTO foodTBL VALUES (?,?,?,?,?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "isssssssssi", $id, $category, $section, $name, $actualpath, $memo, $purchaseDate, $expirationDate, $code, $place, $alarmID);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
   }else{
      echo "failes";
   }

?>
