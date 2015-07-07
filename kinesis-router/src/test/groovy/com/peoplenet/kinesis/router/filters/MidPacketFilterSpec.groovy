package com.peoplenet.kinesis.router.filters

import com.peoplenet.packets.mid.block.oem.OemMiscHeaderDataSimpleBlock
import com.peoplenet.packets.mid.block.oem.OemClearFaultsComplexBlock
import com.peoplenet.packets.mid.block.oem.OemPmgInfoSimpleBlock
import com.peoplenet.packets.mid.block.oem.OemTestData
import com.peoplenet.packets.mid.block.oem.OemVehicleDataSimpleBlock
import com.peoplenet.packets.mid.block.oem.OemVinDiscoveryComplexBlock
import com.peoplenet.packets.mid.block.oem.OemVinRollCallDataSimpleBlock
import com.peoplenet.packets.mid.block.oem.OemVinRollCallTestData

import spock.lang.Ignore
import spock.lang.Specification

class MidPacketFilterSpec extends Specification {

    static final String VIN_DISCOVERY_FILTER = 'mid=227,blocks=-65'

    void 'packet filter matches vin discovery'() {
        given:
        MidPacketFilter filter = new MidPacketFilter(VIN_DISCOVERY_FILTER)
        byte[] data = vinDiscoveryPacket()

        when:
        byte[] filtered = filter.filter(data)

        then:
        filtered.size() > 0
    }

    void 'packet filter skips fault code'() {
        given:
        MidPacketFilter filter = new MidPacketFilter(VIN_DISCOVERY_FILTER)
        byte[] data = faultCodePacket()

        when:
        byte[] filtered = filter.filter(data)

        then:
        filtered.size() == 0
    }

    protected byte[] vinDiscoveryPacket() {
        OemVinDiscoveryComplexBlock block = new OemVinDiscoveryComplexBlock(
                new OemMiscHeaderDataSimpleBlock(OemTestData.createOemMiscHeaderDataSimpleBlock()),
                new OemPmgInfoSimpleBlock(OemTestData.createOemPmgInfoSimpleBlock()),
                [ new OemVinRollCallDataSimpleBlock(
                        OemVinRollCallTestData.createOemVinRollCallDataSimpleBlock(1, 1)
                ) ]
        )
        byte[] packet = block.asPacket().getBytes(block.blockLength)
        println "VIN Discovery packet size: ${packet.size()}"
        return packet
    }

    protected byte[] faultCodePacket() {
        OemClearFaultsComplexBlock block = new OemClearFaultsComplexBlock(
            new OemMiscHeaderDataSimpleBlock(OemTestData.createOemMiscHeaderDataSimpleBlock()),
            new OemVehicleDataSimpleBlock(OemTestData.createOemVehicleDataSimpleBlock())
        )
        byte[] packet = block.asPacket().getBytes(block.blockLength)
        println "Fault packet size: ${packet.size()}"
        return packet
    }

}
