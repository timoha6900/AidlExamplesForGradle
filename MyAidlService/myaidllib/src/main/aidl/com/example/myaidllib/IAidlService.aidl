package com.example.myaidllib;

import com.example.myaidllib.IAidlServiceCallback;

interface IAidlService {

    void registerCallback(IAidlServiceCallback cb);

    void unregisterCallback(IAidlServiceCallback cb);

}
