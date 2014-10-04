<?php
$username = $_POST['username'];
$Course = $_POST['course'];
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

$result = $mysqli->query("CREATE TABLE KompotCourse{$courseid}(qrmd5 varchar(255), name varchar(255), matriculation varchar(255))");

require('fpdf/fpdf.php');
move_uploaded_file($_FILES["file"]["tmp_name"], 'exam.pdf');
shell_exec('cp exam.pdf tmerged.pdf');

$students_array = explode ("\n", $students);
foreach ($students_array as &$student) {
    echo $student." : ";

    $hash = md5($student.$courseid);
    $qrurl = "https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=".$hash;

    $student_info = explode(",", $student);
    $sql = "INSERT INTO `KompotCourse{$courseid}` (`qrmd5`, `name`, `matriculation`) VALUES ('', '{$student_info[0]}', '{$student_info[1]}')";
    $mysqli->query($sql);

    $pdf = new FPDF();
    $pdf -> SetAuthor($username);
    $pdf -> SetTitle($Course);
    $pdf->SetFont('Helvetica','B',20);
    $pdf->SetTextColor(50,60,100);

    //set up a page
    $pdf->AddPage('P'); 
    $pdf->SetDisplayMode(real,'default');

    //insert an image and make it a link
    file_put_contents('temp.png', file_get_contents($qrurl));
    $pdf->Image('temp.png',10,20,33,0,'PNG');

    //display the title with a border around it
    $pdf->SetXY(50,20);
    $pdf->SetDrawColor(50,60,100);
    $pdf->Cell(100,10,$course,1,0,'C',0);
    $pdf->Output('temp.pdf','F');

    $output = shell_exec('pdftk temp.pdf exam.pdf tmerged.pdf cat output merged.pdf');
    shell_exec('cp merged.pdf tmerged.pdf');
    echo "done!<br />";
}

echo "<br /><a href=\"merged.pdf\">Download exams</a>";

?>
