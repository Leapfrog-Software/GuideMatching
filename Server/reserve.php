<?php

class ReserveData {
  public $id;
	public $guestId;
  public $guideId;
  public $fee;
  public $applicationFee;
  public $meetingPlace;
  public $day;
  public $startTime;
  public $endTime;
  public $reserveDate;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 10) {
      $reserveData = new ReserveData();
      $reserveData->id = $datas[0];
			$reserveData->guestId = $datas[1];
      $reserveData->guideId = $datas[2];
      $reserveData->fee = $datas[3];
      $reserveData->applicationFee = $datas[4];
      $reserveData->meetingPlace = $datas[5];
      $reserveData->day = $datas[6];
      $reserveData->startTime = $datas[7];
      $reserveData->endTime = $datas[8];
      $reserveData->reserveDate = $datas[9];
			return $reserveData;
		}
		return null;
	}

  function toFileString() {
    $str = "";
    $str .= $this->id;
    $str .= ",";
    $str .= $this->guestId;
    $str .= ",";
    $str .= $this->guideId;
    $str .= ",";
    $str .= $this->fee;
    $str .= ",";
    $str .= $this->applicationFee;
    $str .= ",";
    $str .= $this->meetingPlace;
    $str .= ",";
    $str .= $this->day;
    $str .= ",";
    $str .= $this->startTime;
    $str .= ",";
    $str .= $this->endTime;
    $str .= ",";
    $str .= $this->reserveDate;
    $str .= "\n";
    return $str;
  }
}

class Reserve {

  const FILE_NAME = "data/reserve.txt";

	static function readAll() {
		if (file_exists(Reserve::FILE_NAME)) {
			$fileData = file_get_contents(Reserve::FILE_NAME);
			if ($fileData !== false) {
				$reserveList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$reserveData = ReserveData::initFromFileString($lines[$i]);
					if (!is_null($reserveData)) {
						$reserveList[] = $reserveData;
					}
				}
				return $reserveList;
			}
		}
		return [];
	}

  static function create($guestId, $guideId, $fee, $applicationFee, $meetingPlace, $day, $startTime, $endTime) {

    $maxReserveId = -1;

    $reserveList = Reserve::readAll();
    foreach ($reserveList as $reserveData) {
      $reserveId = intval($reserveData->id);
      if ($reserveId > $maxReserveId) {
        $maxReserveId = $reserveId;
      }
    }

    $nextReserveId = strval($maxReserveId + 1);

    date_default_timezone_set('Asia/Tokyo');

    $reserveData = new ReserveData();
    $reserveData->id = $nextReserveId;
    $reserveData->guestId = $guestId;
    $reserveData->guideId = $guideId;
    $reserveData->fee = $fee;
    $reserveData->applicationFee = $applicationFee;
    $reserveData->meetingPlace = $meetingPlace;
    $reserveData->day = $day;
    $reserveData->startTime = $startTime;
    $reserveData->endTime = $endTime;
    $reserveData->reserveDate = date("YmdHis");

   $str = $reserveData->toFileString();
    return file_put_contents(Reserve::FILE_NAME, $str, FILE_APPEND) !== false;
  }
}

?>
