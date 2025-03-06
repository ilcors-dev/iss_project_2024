package facade24;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Logica applicativa (domain core) della gui
Creata da ServiceFacadeController usando FacadeBuilder
 */
public class ApplguiCore {
    private ActorOutIn outinadapter;

    public ApplguiCore(ActorOutIn outinadapter) {
        this.outinadapter = outinadapter;
        ApplSystemInfo.setup();
    }

    // Chiamato da CoapObserver
    public void handleMsgFromActor(String msg, String requestId) {
        CommUtils.outcyan("AGC | hanldeMsgFromActor " + msg + " requestId=" + requestId);
        updateMsg(msg);
    }

    public void updateMsg(String msg) {
        CommUtils.outblue("AGC updateMsg " + msg);
        outinadapter.sendToAll(msg);
        // potrei mandare a M2M ... che poi manda la risposta a REST POST
        // M2MController.m2mCtrl.setAnswer(msg);
    }

}
