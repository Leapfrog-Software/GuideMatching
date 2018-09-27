<?php

require "reserve.php";

$str = "ID,Reserve date,Guest ID,Guide ID,Guide fee,Transaction fee,Meeting place,Date,Start time,End time\n";

$reserveList = Reserve::readAll();
foreach ($reserveList as $reserveData) {
	$str .= ($reserveData->id . ",");
	$str .= (dateToYmdhis($reserveData->reserveDate) . ",");
	$str .= ($reserveData->guestId . ",");
	$str .= ($reserveData->guideId . ",");
	$str .= ($reserveData->fee . ",");
	$str .= ($reserveData->applicationFee . ",");
	$str .= ($reserveData->meetingPlace . ",");
	$str .= (dateToYmd($reserveData->day) . ",");
	$str .= (timeOffsetToString($reserveData->startTime) . ",");
	$str .= (timeOffsetToString($reserveData->endTime) . "\n");
}

$filePath = "settlement.csv";

file_put_contents($filePath, $str);

date_default_timezone_set("Asia/Tokyo");
$fileName = "settlement_" . date("YmdHis") . ".csv";

header("Content-Type: application/force-download");
header("Content-Length: ". filesize($filePath));
header("Content-disposition: attachment; filename=\"" . $fileName. "\"");
readfile($filePath);

function dateToYmdhis($string) {

	if (strlen($string) != 14) {
		return $string;
	}
	$dateStr = "";
	$dateStr .= substr($string, 0, 4);
	$dateStr .= "/";
	$dateStr .= substr($string, 4, 2);
	$dateStr .= "/";
	$dateStr .= substr($string, 6, 2);
	$dateStr .= " ";
	$dateStr .= substr($string, 8, 2);
	$dateStr .= ":";
	$dateStr .= substr($string, 10, 2);
	$dateStr .= ":";
	$dateStr .= substr($string, 12, 2);
	return $dateStr;
}

function dateToYmd($string) {

	if (strlen($string) != 8) {
		return $string;
	}
	$dateStr = "";
	$dateStr .= substr($string, 0, 4);
	$dateStr .= "/";
	$dateStr .= substr($string, 4, 2);
	$dateStr .= "/";
	$dateStr .= substr($string, 6, 2);
	return $dateStr;
}

function timeOffsetToString($timeOffset) {

	$offsetInt = intval($timeOffset);
	return sprintf("%02d", $offsetInt / 2) . ":" . sprintf("%02d", 30 * ($offsetInt % 2));
}

?>
