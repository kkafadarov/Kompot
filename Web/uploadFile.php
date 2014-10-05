<?php
$target_path1 = "uploads/";
$qr=$_FILES['uploaded_file']['name'];
/* Add the original filename to our target path.
Result is "uploads/filename.extension" */
$target_path1 = $target_path1 . basename( $_FILES['uploaded_file']['name']);
$file = $target_path1;
if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $target_path1)) {
    echo "The first file ".  basename( $_FILES['uploaded_file']['name']).
    " has been uploaded.";
} else{
    echo "There was an error uploading the file, please try again!";
    echo "filename: " .  basename( $_FILES['uploaded_file']['name']);
    echo "target_path: " .$target_path1;
}

require_once ("db/db_const.php");
$mysqli = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME); 

# check connection
if ($mysqli -> connect_errno) {
echo "<p>MySQL error no {$mysqli->connect_errno} : {$mysqli->connect_error}</p>";
exit();
}

#if(!mysql_connect("localhost", "root", "H4ck4R007") || !mysql_select_db('KompotDB')) {
	#die("cannot connect");
#}


$course_id='39';
$sql = "SELECT * FROM KompotCourse{$course_id}";

$myfile = fopen("uploads/{$qr}.check.txt", "w");

$result = $mysqli->query($sql);

while($row = $result->fetch_assoc()) {

$matr = $row['matriculation'];
$name = $row['name'];

printf("%s %s\n", $matr, $name);

fwrite($myfile, "{$matr} {$name}\n");
}
fclose($myfile);

shell_exec("process.sh {$file}");

$f_id = fopen("uploads/{$qr}.out.txt", "r");
$matr_res = fgets($f_id);
$matr_res = str_replace("\n",'',$matr_res);
$sql = "UPDATE KompotCourse{$course_id} SET qrmd5=\"{$qr}\" WHERE matriculation=\"{$matr_res}\" ";
$result = $mysqli->query($sql);

?>

