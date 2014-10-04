<!DOCTYPE html>
<html lang="en-US">
<head>
    <title>Register - Anonymous Exams</title>
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
    
    <script src="js/register.js"></script>
</head>

<body>
    <div class="center">
        <h1>Register</h1>

        <?php
        function IsNullOrEmptyString($question){
            return (!isset($question) || trim($question)==='');
        }

        require_once("db/db_const.php");
        if (!isset($_POST['submit'])) {
        ?>

            <!-- The HTML registration form -->
            <form action="<?=$_SERVER['PHP_SELF']?>" method="post">
                <table widht="300px">
                
                <tr><td style="text-align: right;">
                Username: 
                <input type="text" name="username" />
                </td></tr>
                
                <tr><td style="text-align: right;">
                Password: 
                <input type="password" name="password" id="pass" />
                </td></tr>

                <tr><td style="text-align: right;">
                First name:
                <input type="text" name="first_name" /><br />
                </td></tr>

                <tr><td style="text-align: right;">
                Last name:
                <input type="text" name="last_name" /><br />
                </td></tr>

                <tr><td style="text-align: right;">
                Email:
                <input type="type" name="email" /><br />
                </td></tr>

                </table>

                <button value="Submit" name="submit" type="submit" class="btn btn-success"> Submit </button>
            </form>

        <?php
        } else {
        ## connect mysql server
            $mysqli = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
            # check connection
            if ($mysqli->connect_errno) {
                echo "<p>MySQL error no {$mysqli->connect_errno} : {$mysqli->connect_error}</p>";
                exit();
            }
        ## query database
            # prepare data for insertion
            $username	= $_POST['username'];
            $password	= $_POST['password'];
            $first_name	= $_POST['first_name'];
            $last_name	= $_POST['last_name'];
            $email		= $_POST['email'];

            # check if username and email exist else insert
            $exists = 0;
            $result = $mysqli->query("SELECT username from KompotUsers WHERE username = '{$username}' LIMIT 1");
            if ($result->num_rows == 1) {
                $exists = 1;
                $result = $mysqli->query("SELECT email from KompotUsers WHERE email = '{$email}' LIMIT 1");
                if ($result->num_rows == 1) $exists = 2;	
            } else {
                $result = $mysqli->query("SELECT email from KompotUsers WHERE email = '{$email}' LIMIT 1");
                if ($result->num_rows == 1) $exists = 3;
            }

            if (IsNullOrEmptyString($username)
                || IsNullOrEmptyString($password)
                || IsNullOrEmptyString($first_name)
                || IsNullOrEmptyString($last_name)
                || IsNullOrEmptyString($email)) echo "<p>Please fill in all of the fields!</p>";
            else if ($exists == 1) echo "<p>Username already exists!</p>";
            else if ($exists == 2) echo "<p>Username and Email already exists!</p>";
            else if ($exists == 3) echo "<p>Email already exists!</p>";
            else {
                # insert data into mysql database
                $sql = "INSERT  INTO `KompotUsers` (`id`, `username`, `password`, `first_name`, `last_name`, `email`) 
                        VALUES (NULL, '{$username}', '{$password}', '{$first_name}', '{$last_name}', '{$email}')";

                if ($mysqli->query($sql)) {
                    //echo "New Record has id ".$mysqli->insert_id;
                    echo "<p>Registred successfully!</p>";
                } else {
                    echo "<p>MySQL error no {$mysqli->errno} : {$mysqli->error}</p>";
                    exit();
                }
            }
        }
        ?>		
    </div>
</body>

</html>
