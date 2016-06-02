<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use DB;

use Log;

class Contact extends Controller
{
    //
    public function resolveContacts(Request $request)
    {
    	$data = $request->getContent();
    	$phone_numbers = json_decode($data)->numbers;
    	Log::info("Numbers");
    	Log::info($phone_numbers);
    	$contactsToResolve = isset($phone_numbers)?count($phone_numbers):0;
    	if($contactsToResolve > 0){
    		$contacts = DB::table('users')
    				->select('name','status','lastseen')
                    ->whereIn('phone_number', $phone_numbers)
                    ->get();
    	}
    	//$result['response']=false;
    	$contactsResolved = count($contacts);
    	if($contactsResolved > 0){
       		//$result['response']=true;
    		$result['contacts']=$contacts;
    	}
        
        return response()->json($result);
    }
}
