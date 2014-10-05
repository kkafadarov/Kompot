<!DOCTYPE html>
<html lang="en-US">
<head>
    <title>Anonymous Exams</title>
    <!-- BOOTSRAP BEGIN -->
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> 
    <!-- BOOTSRAP END -->
    
    <!-- JQUERY -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>    

    <!-- OUR STYLE -->
    <link rel="stylesheet" type="text/css" href="css/style.css">    
    
    <script src="js/jquery.md5.js"></script>
    <script src="js/register.js"></script>
    <script src="js/login.js"></script>
</head>

<body>
    <div class="center">
        <h1>
        <div class="headline">Login</div>
        </h1>

        <?php
        if(!isset($_POST['submit'])) {
        ?>

            <!-- HTML LOGIN FORM -->
            <div class="register">
                <form action="<?=$_SERVER['PHP_SELF']?>" method="post">
                    <table>
                        <tr>
                            <td style="text-align: right;"> Username: <input type="text" name="username" /> </td>
                        </tr>
                        <tr>
                            <td style="text-align: right;"> Password: <input type="password" name="password" id="pass" /> </td>
                        </tr>
                    </table>

                    <br />
                    <button value="Submit" name="submit" type="submit" class="btn btn-success"> Submit </button>
                </form>
            </div>

        <?php
        } else {
        require_once("db/db_const.php");
        $mysqli = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);    

        # check connection
        if ($mysqli -> connect_errno) {
            echo "<p>MySQL error no {$mysqli->connect_errno} : {$mysqli->connect_error}</p>";
            exit();
        }

        $username = $_POST['username'];
        $password = $_POST['password'];

        $sql = "SELECT * from KompotUsers WHERE username LIKE '{$username}' AND password LIKE '{$password}' LIMIT 1";
        $result = $mysqli->query($sql);
        if (!$result->num_rows == 1) {
            echo "<p>Invalid username/password combination!</p>";
        } else {
        }
        ?>
        
        <script type="text/javascript">
            updateTitle ();
        </script>

        <div class="register">
            <form action="finalize.php" method="post" enctype="multipart/form-data">
                <input type="hidden" name="username" value="<?= $username ?>" />
                <table>
                    <tr>
                        <td style="text-align: left;"> Course: <br /> <input type="text" name="course" /> </td>
                    </tr>
                    <tr>
                        <td style="text-align: left;"> Term: <br />
                            <select name="term">
                                <option value="fall">Fall</option>
                                <option value="spring">Spring</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: left;"> Year: <br /> 
                            <select name="year">
                                <option value="2014">2014</option>
                                <option value="2015">2015</option>
                                <option value="2016">2016</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: left;"> Students: <br />
                            <textarea name="students" rows="15" cols="40"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: left;">
                            <label for="file">Exam .pdf file:</label>
                            <input type="file" name="file" id="file" />
                        </td>
                    </tr>
                </table>

                <br />
                <button value="Submit" name="submit" type="submit" class="btn btn-success"> Submit </button>
            </form>
        </div>

        <?php
        }
        ?>
    </div>
</body>
</html>
