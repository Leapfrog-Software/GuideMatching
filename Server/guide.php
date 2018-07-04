<?php

class GuideData {
  public $id;
	public $name;
  public $nationality;
  public $language;
  public $specialty;
  public $category;
  public $message;
  public $timeZone;
  public $applicableNumber;
  public $fee;
  public $notes;
  public $loginDate;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 12) {
      $guideData = new GuideData();
      $guideData->id = $datas[0];
			$guideData->name = $datas[1];
      $guideData->nationality = $datas[2];
      $guideData->language = $datas[3];
      $guideData->specialty = $datas[4];
      $guideData->category = $datas[5];
      $guideData->message = $datas[6];
      $guideData->timeZone = $datas[7];
      $guideData->applicableNumber = $datas[8];
      $guideData->fee = $datas[9];
      $guideData->notes = $datas[10];
      $guideData->loginDate = $datas[11];
			return $guideData;
		}
		return null;
	}

  function toFileString() {
    $str = "";
    $str .= $this->id;
    $str .= ",";
    $str .= $this->name;
    $str .= ",";
    $str .= $this->nationality;
    $str .= ",";
    $str .= $this->language;
    $str .= ",";
    $str .= $this->specialty;
    $str .= ",";
    $str .= $this->category;
    $str .= ",";
    $str .= $this->message;
    $str .= ",";
    $str .= $this->timeZone;
    $str .= ",";
    $str .= $this->applicableNumber;
    $str .= ",";
    $str .= $this->fee;
    $str .= ",";
    $str .= $this->notes;
    $str .= ",";
    $str .= $this->loginDate;
    $str .= "\n";
    return $str;
  }
}

class Guide {

  const FILE_NAME = "data/guide.txt";

	static function readAll() {
		if (file_exists(Guide::FILE_NAME)) {
			$fileData = file_get_contents(Guide::FILE_NAME);
			if ($fileData !== false) {
				$dataList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$data = GuideData::initFromFileString($lines[$i]);
					if (!is_null($data)) {
						$dataList[] = $data;
					}
				}
				return $dataList;
			}
		}
		return [];
	}

  static function create($name, $nationality, $language, $specialty, $category, $message, $timeZone, $applicableNumber, $fee, $notes) {

    $maxGuideId = -1;

    $guideList = Guide::readAll();
    foreach ($guideList as $guideData) {
      $guideId = intval(substr($guideData->id, 6, strlen($guideData->id) - 6));
      if ($guideId > $maxGuideId) {
        $maxGuideId = $guideId;
      }
    }

    $nextGuideId = strval($maxGuideId + 1);

    date_default_timezone_set('Asia/Tokyo');

    $guideData = new GuideData();
    $guideData->id = "guide_" . $nextGuideId;
    $guideData->name = $name;
    $guideData->nationality = $nationality;
    $guideData->language = $language;
    $guideData->specialty = $specialty;
    $guideData->category = $category;
    $guideData->message = $message;
    $guideData->timeZone = $timeZone;
    $guideData->applicableNumber = $applicableNumber;
    $guideData->fee = $fee;
    $guideData->notes = $notes;
    $guideData->loginDate = date("YmdHis");

    if (file_put_contents(Guide::FILE_NAME, $guideData->toFileString(), FILE_APPEND) !== FALSE) {
      return $guideData->id;
    } else {
      return null;
    }
  }

  static function update($id, $name, $nationality, $language, $specialty, $category, $message, $timeZone, $applicableNumber, $fee, $notes) {

    $guideList = Guide::readAll();
    $find = false;

    date_default_timezone_set('Asia/Tokyo');

    foreach ($guideList as &$guideData) {
      if (strcmp($guideData->id, $id) == 0) {
        $guideData = new GuideData();
        $guideData->id = $id;
        $guideData->name = $name;
        $guideData->nationality = $nationality;
        $guideData->language = $language;
        $guideData->specialty = $specialty;
        $guideData->category = $category;
        $guideData->message = $message;
        $guideData->timeZone = $timeZone;
        $guideData->applicableNumber = $applicableNumber;
        $guideData->fee = $fee;
        $guideData->notes = $notes;
        $guideData->loginDate = date("YmdHis");

        $find = true;
        break;
      }
    }
    if (!$find) {
      return false;
    }

    $str = "";
    foreach($guideList as $data) {
      $str .= $data->toFileString();
    }
    return file_put_contents(Guide::FILE_NAME, $str) !== false;
  }

  static function getGuideArray() {

    $ret = [];

    $guideList = Guide::readAll();
    foreach ($guideList as $guideData) {
      $ret[] = Array("id" => $guideData->id,
                      "name" => $guideData->name,
                      "nationality" => $guideData->nationality,
                      "language" => $guideData->language,
                      "specialty" => $guideData->specialty,
                      "category" => $guideData->category,
                      "message" => $guideData->message,
                      "timeZone" => $guideData->timeZone,
                      "applicableNumber" => $guideData->applicableNumber,
                      "fee" => $guideData->fee,
                      "notes" => $guideData->notes,
                      "loginDate" => $guideData->loginDate);
    }
    return $ret;
  }

  static function login() {

    $guideList = Guide::readAll();
    $find = false;

    foreach ($guideList as &$guideData) {
      if (strcmp($guideData->id, $id) == 0) {
        date_default_timezone_set('Asia/Tokyo');
        $guideData->loginDate = date("YmdHis");
        $find = true;
        break;
      }
    }
    if (!$find) {
      return false;
    }

    $str = "";
    foreach($guideList as $data) {
      $str .= $data->toFileString();
    }
    return file_put_contents(Guide::FILE_NAME, $str) !== false;
  }
}

?>
