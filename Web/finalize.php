<?php
$username = $_POST['username'];
$course   = strtolower($_POST['course']);
$term     = strtolower($_POST['term']);
$year     = $_POST['year'];
$students = $_POST['students'];

$codename = preg_replace("/[^a-z0-9_\s-]/", "", $course);
$codename = preg_replace("/[\s-]+/", " ", $codename);
$codename = preg_replace("/[\s_]/", "-", $codename);
$codename = $codename . "-" . $term . "-" . $year;

require_once ("db/db_const.php");
$mysqli = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);    

# check connection
if ($mysqli -> connect_errno) {
    echo "<p>MySQL error no {$mysqli->connect_errno} : {$mysqli->connect_error}</p>";
    exit();
}

$sql = "INSERT INTO `KompotCourses` (`id`, `username`, `coursename`, `term`, `year`, `codename`, `students`)
    VALUES (NULL, '{$username}', '{$course}', '{$term}', {$year}, '{$codename}', '{$students}')";

if ($mysqli -> query($sql)) {
} else {
    echo "<p>MySQL error no {$mysqli->errno} : {$mysqli->error}</p>";
    exit();
}

$result = $mysqli->query("SELECT * from KompotCourses WHERE codename = '{$codename}' LIMIT 1");
$row = $result -> fetch_assoc();
$courseid = $row['id'];

$students_array = explode ("\n", $students);
foreach ($students_array as &$student) {
    echo $student . "<br />";
}

?>
