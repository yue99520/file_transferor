package com.yue99520.tool.file.transferor.service.connection;

import java.util.List;

public interface ConnectionService {

    Partner getPartner();

    boolean ping(String partnerName);
}
