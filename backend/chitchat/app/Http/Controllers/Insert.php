<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;
use Log;

class Insert extends Controller
{
    public function insertfun(Request $request){
        //Log::info("New attempt");
    	$email = $request->input('email');
    	$password = $request->input('password');

    	$name=DB::table('users')->insert(array('email' => $email, 'password' => $password));
    	$login_result['response']=false;
    	if(isset($name)){
    		$login_result['response']=true;
    		$login_result['name']="Inserted.";
    	}
    	return response()->json($login_result);
    }
}
