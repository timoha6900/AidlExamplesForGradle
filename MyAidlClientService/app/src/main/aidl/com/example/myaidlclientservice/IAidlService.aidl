package com.example.myaidlclientservice;

import com.example.myaidlclientservice.IAidlServiceCallback;

interface IAidlService {

    void registerCallback(IAidlServiceCallback cb);

    void unregisterCallback(IAidlServiceCallback cb);

}
