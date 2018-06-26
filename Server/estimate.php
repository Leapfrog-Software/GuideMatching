<?php

class EstimateData {
  public $requesterId;
  public $guideId;
  public $score;
  public $comment;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 4) {
      $estimateData = new EstimateData();
			$estimateData->requesterId = $datas[0];
      $estimateData->guideId = $datas[1];
      $estimateData->score = $datas[2];
      $estimateData->comment = $datas[3];
			return $estimateData;
		}
		return null;
	}

  function toFileString() {
    $str = "";
    $str .= $this->requesterId;
    $str .= ",";
    $str .= $this->guideId;
    $str .= ",";
    $str .= $this->score;
    $str .= ",";
    $str .= $this->comment;
    $str .= "\n";
    return $str;
  }
}

class Estimate {

  const FILE_NAME = "data/estimate.txt";

	static function readAll() {
		if (file_exists(Estimate::FILE_NAME)) {
			$fileData = file_get_contents(Estimate::FILE_NAME);
			if ($fileData !== false) {
				$estimateList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$estimateData = EstimateData::initFromFileString($lines[$i]);
					if (!is_null($estimateData)) {
						$estimateList[] = $estimateData;
					}
				}
				return $estimateList;
			}
		}
		return [];
	}

  static function append($estimateData) {

    $str = $estimateData->toFileString();
    return file_put_contents(Estimate::FILE_NAME, $str, FILE_APPEND) !== false;
  }
}

?>
