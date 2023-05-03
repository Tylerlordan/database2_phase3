<?php
$userId = $_POST['user_id'];
$userId = intval($userId);
$userType = $_POST['user_type'];
$meeting_id = $_POST['meeting_id'];
$meeting_id = intval($meeting_id);

$response["success"] = false;
$response["message"] = "Query Error.";

// Connect to the database
$myconnection = mysqli_connect('localhost', 'root', '') or die(json_encode($response));
$mydb = mysqli_select_db($myconnection, 'db2') or die(json_encode($response));

// Query meetings table to get meeting details for the given year
$meetings_query = "SELECT material_id, meeting_name, title, author, type, url, assigned_date, notes 
                FROM material
                LEFT JOIN meetings
                ON material.meeting_id = meetings.meeting_id
                WHERE material.meeting_id = $meeting_id";
$meetings_result = mysqli_query($myconnection, $meetings_query) or die(json_encode($response));


$i = 0;
$materials = [];

// Loop through meetings query results and print table rows
while ($meeting_row = mysqli_fetch_array($meetings_result, MYSQLI_ASSOC)) {
        $i += 1;
        $temp_row["material_id"] = $meeting_row["material_id"];
        $temp_row["title"] = $meeting_row["title"];
        $temp_row["meeting"] = $meeting_row["meeting_name"];
        $temp_row["author"] = $meeting_row["author"];
        $temp_row["type"] = $meeting_row["type"];
        $temp_row["url"] = $meeting_row["url"];
        $temp_row["date"] = $meeting_row["assigned_date"];
        $temp_row["notes"] = $meeting_row["notes"];
        array_push($materials, $temp_row);
}
if($i == 0) {
        $response["message"] = "No materials found.";
        die(json_encode($response));
}

$response["success"] = true;
$response["materials"] = $materials;
echo json_encode($response);

mysqli_close($myconnection);

?>