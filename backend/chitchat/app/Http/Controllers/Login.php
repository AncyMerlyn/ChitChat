<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;
use Log;

class Login extends Controller
{
    public function login(Request $request){
        Log::info("New attempt");
    	$email = $request->input('email');
    	$password = $request->input('password');
        Log::info("New attempt email");
    	$name=DB::table('users')->where('email','=',$email)
    					->where('password','=',$password)
    					->value('nick_name');
    	$login_result['response']=false;
    	if(isset($name)){
    		$login_result['response']=true;
    		$login_result['name']=$name;
    	}
    	return response()->json($login_result);
    }
}
