package com.andriiprudyus.network.exception

import java.io.IOException

class NetworkException(val errorCode: ErrorCode) : IOException()