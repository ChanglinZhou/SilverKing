package com.ms.silverking.cloud.dht.net.protocol;

import com.ms.silverking.cloud.dht.RetrievalOptions;
import com.ms.silverking.cloud.dht.common.DHTConstants;
import com.ms.silverking.cloud.dht.net.SecondaryTargetSerializer;
import com.ms.silverking.numeric.NumConversion;


public class RetrievalMessageFormat extends KeyedMessageFormat {    
    public static final int stDataOffset = RetrievalResponseMessageFormat.optionBytesSize;
    
    public static final int getOptionsBufferLength(RetrievalOptions retrievalOptions) {
        return RetrievalResponseMessageFormat.optionBytesSize // same format for both directions
                + NumConversion.BYTES_PER_SHORT 
                + SecondaryTargetSerializer.serializedLength(retrievalOptions.getSecondaryTargets())
                + NumConversion.BYTES_PER_INT
                + (retrievalOptions.getUserOptions() == DHTConstants.noUserOptions ? 0 : retrievalOptions.getUserOptions().length);
    }
}
