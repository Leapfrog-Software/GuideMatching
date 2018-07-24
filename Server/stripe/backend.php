<?php

require('stripe-php-6.10.4/init.php');

\Stripe\Stripe::setApiKey("sk_test_LcKqhnYm6KRh4Jmk5ANNos0C");
//\Stripe\Stripe::setApiKey("sk_test_FR3TfcFZ8lnbPRlBWk7h1sdG");  // 開発用

$command = $_POST["command"];

if (strcmp($command, "createAccount") == 0) {
  createAccount();
} else if (strcmp($command, "createCustomer") == 0) {
  createCustomer();
} else if (strcmp($command, "createEphemeralKey") == 0) {
  createEphemeralKey();
} else if (strcmp($command, "createCharge") == 0) {
  createCharge();
} else {
  echo("unknown");
}

function createAccount() {

  $email = $_POST["email"];
  if (!isValidEmail($email)) {
    echo(json_encode(Array("result" => "1")));
    return;
  }

  try {
    $account = \Stripe\Account::create(
      Array(
        "country" => "JP",
        "type" => "standard",
        "email" => $email
      )
    );
  } catch (Exception $e) {
    echo(json_encode(Array("result" => "2", "message" => $e->getMessage())));
    return;
  }

  $json = json_decode($account->__toJSON());
  if (!is_null($json)) {
    $accountId = $json->id;
    if ((!is_null($accountId)) && (strlen($accountId) > 0)) {
      echo(json_encode(Array("result" => "0", "accountId" => $accountId)));
      return;
    }
  }
  echo(json_encode(Array("result" => "2")));
}

function createCustomer() {

  $email = $_POST["email"];
  if (!isValidEmail($email)) {
    echo(json_encode(Array("result" => "1")));
    return;
  }

  try {
    $customer = \Stripe\Customer::create(
      Array(
        "email" => $email
      )
    );
  } catch (Exception $e) {
    echo(json_encode(Array("result" => "2")));
    return;
  }
  $customerId = json_decode($customer->__toJSON())->id;
  echo(json_encode(Array("result" => "0",
                          "customerId" => $customerId)));
}

function createEphemeralKey() {

  $apiVersion = $_POST["apiVersion"];
  $customerId = $_POST["customerId"];

  if ((!isset($apiVersion)) || (!isset($customerId))) {
    exit(http_response_code(400));
  }

  try {
    $key = \Stripe\EphemeralKey::create(
      Array("customer" => $customerId),
      Array("stripe_version" => $apiVersion)
    );
    echo(json_encode($key));
    
  } catch (Exception $e) {
    exit(http_response_code(500));
  }
}

function createCharge() {

  $customerId = $_POST["customerId"];
  $cardId = $_POST["cardId"];
  $amount = $_POST["amount"];  
  $applicationFee = $_POST["applicationFee"];
  $destination = $_POST["destination"];

  if ((!isset($customerId)) || (!isset($cardId)) || (!isset($destination))) {
    echo(json_encode(Array("result" => "1")));
    return;
  }
  $amountInt = intval($amount);
  $applicationFeeInt = intval($applicationFee);
  if (($amountInt == 0) || ($applicationFee == 0)) {
    echo(json_encode(Array("result" => "1")));
    return;
  }

  try {
    $charge = \Stripe\Charge::create(
      Array(
        "customer" => $customerId,
        "amount" => $amountInt,
        "currency" => "jpy",
        "source" => $cardId,
        "application_fee" => $applicationFeeInt,
        "destination" => Array(
          "account" => $destination
        )
      )
    );
    echo(json_encode(Array("result" => "0")));
  } catch (Exception $e) {
    echo(json_encode(Array("result" => "1", "message" => $e->getMessage())));
  }
}

function isValidEmail($email) {

  if (strlen($email) == 0) {
    return false;
  }
  $exploded = explode("@", $email);
  if (count($exploded) != 2) {
    return false;
  }
  if (strlen($exploded[0]) == 0) {
    return false;
  }
  if (count(explode(".", $exploded[1])) <= 1) {
    return false;
  }

  return true;
}

function DebugSave($str){

	date_default_timezone_set('Asia/Tokyo');

	$d = date('Y-m-d H:i:s');
	$s = $d . " " . $str . "\r\n";
	file_put_contents("debug.txt", $s, FILE_APPEND);
}

?>
