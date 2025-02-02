package com.rtm516.mcxboxbroadcast.core.webrtc;

import org.cloudburstmc.protocol.bedrock.codec.v712.Bedrock_v712;
import pe.pi.sctp4j.sctp.Association;
import pe.pi.sctp4j.sctp.AssociationListener;
import pe.pi.sctp4j.sctp.SCTPStream;

public class SctpAssociationListener implements AssociationListener {
    @Override
    public void onAssociated(Association association) {
        System.out.println("Association associated: " + association.toString());
    }

    @Override
    public void onDisAssociated(Association association) {
        System.out.println("Association disassociated: " + association.toString());
    }

    @Override
    public void onDCEPStream(SCTPStream sctpStream, String label, int i) throws Exception {
        if (label == null) {
            return;
        }
        System.out.println("Received DCEP SCTP stream: " + sctpStream.toString());

        if ("ReliableDataChannel".equals(label)) {
            sctpStream.setSCTPStreamListener(new MinecraftDataHandler(sctpStream, Bedrock_v712.CODEC));
        }
    }

    @Override
    public void onRawStream(SCTPStream sctpStream) {
        System.out.println("Received raw SCTP stream: " + sctpStream.toString());
    }
}