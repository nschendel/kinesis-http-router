package com.peoplenet.kinesis.router.filters

import com.peoplenet.packets.mid.Mid
import com.peoplenet.packets.mid.MidUtil
import com.peoplenet.packets.mid.MidWhitelist
import com.peoplenet.packets.mid.MidWhitelistImpl
import com.peoplenet.packets.mid.PhoenixMid
import com.peoplenet.packets.mid.block.Block
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Filter data packets that are MIDs by their MID ID
 */
@CompileStatic
@Slf4j
class MidPacketFilter implements DataFilter {

    static final byte[] EMPTY_BYTES = [] as byte[]

    @Override FilterType getType() { FilterType.MIDPACKET }

    /** Filter to check MID agains, for example VIN Discovery is:
     *
     *     mid=227,blocks=-65
     */
    protected final String midFilter
    protected final MidWhitelist whitelist
    protected final MidUtil midUtil

    MidPacketFilter(String midFilter) {
        this.midFilter = midFilter
        this.whitelist = new MidWhitelistImpl(midFilter)
        String blockMids = this.whitelist.topLevelMids.join(',')
        this.midUtil = new MidUtil(blockMids)
    }

    @Override
    String toString() {
        "${type}: ${midFilter}"
    }

    @Override
    byte[] filter(byte[] data) {
        if (data.size() == 0) { return EMPTY_BYTES }
        try {
            PhoenixMid phoenixMid = new PhoenixMid(data)
            log.info "phoenixMid=${phoenixMid}"
            if (!phoenixMid) { return EMPTY_BYTES }
            Mid mid = midUtil.unwrap(phoenixMid.mid)
            log.info "mid=${mid}"
            Block topBlock = midUtil.getTopBlock(mid)
            log.info "topBlock=${topBlock}"
            if (whitelist.isInWhitelist(mid.getMessageId(), topBlock)) {
                return data
            }
            log.info "nope."
        } catch (Exception ex) {
            log.warn('Caught exception trying to parse data as a MIDPACKET: {}', ex.message)
        }
        return EMPTY_BYTES
    }

}
