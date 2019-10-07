package com.lei2j.douyu.admin.danmu;

import java.io.IOException;
import java.util.*;

/**
 * author: 98611
 * date: 2019/10/4
 */

public class DouyuNioWorkEventLoop {

    private List<DouyuNioConnection> connectionList;

    public DouyuNioWorkEventLoop() throws IOException {
        this(5);
    }

    public DouyuNioWorkEventLoop(int workerSize) throws IOException {
        int size = (int) (workerSize / 0.75) + 1;
        connectionList = new ArrayList<>(size);
        for (int i = 0; i < workerSize; i++) {
            connectionList.add(DouyuNioConnection.createConnection());
        }
    }

    public DouyuNioLogin get(Integer room) {
        Optional<DouyuNioConnection> optional = connectionList.stream().min(Comparator.comparing(DouyuNioConnection::getChannelLength));
        if (!optional.isPresent()) {
            throw new NullPointerException();
        }
        DouyuNioConnection douyuNioConnection = optional.get();
        DouyuNioLogin douyuNioLogin = new DouyuNioLogin(room, douyuNioConnection);
        return douyuNioLogin;
    }
}
