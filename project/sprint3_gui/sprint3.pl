%====================================================================================
% sprint3 description   
%====================================================================================
event( burnEnd, burnEnd(N) ).
dispatch( activation_command, activation_command(N) ).
dispatch( startBurn, startBurn(N) ).
dispatch( startRobot, startRobot(X) ).
request( engage, engage(OWNER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
request( moverobot, moverobot(TARGETX,TARGETY) ).
dispatch( info, info(X,Y) ).
dispatch( numRP, numRP(X) ).
dispatch( statoIn, statoIn(N) ).
dispatch( statoOp, statoOp(N) ).
dispatch( valAsh, valAsh(X) ).
dispatch( doread, doread(X) ).
event( scaledata, scaledata(D) ).
dispatch( scalework, scalework(X) ).
request( pollingRP, pollingRP(X) ).
reply( numberRP, numberRP(X) ).  %%for pollingRP
request( pollingAsh, pollingAsh(X) ).
reply( valueAsh, valueAsh(X) ).  %%for pollingAsh
dispatch( pickRP, pickRP(X) ).
dispatch( turnLedOn, turnLedOn(X) ).
dispatch( turnLedOff, turnLedOff(X) ).
%====================================================================================
context(ctxsprintdue, "localhost",  "TCP", "8080").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmonitor, "127.0.0.1",  "TCP", "8128").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( monitoring_device, ctxmonitor, "external").
  qactor( wis, ctxsprintdue, "it.unibo.wis.Wis").
 static(wis).
  qactor( incinerator, ctxsprintdue, "it.unibo.incinerator.Incinerator").
 static(incinerator).
  qactor( oprobot, ctxsprintdue, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( scale, ctxsprintdue, "it.unibo.scale.Scale").
 static(scale).
  qactor( scale_device, ctxsprintdue, "it.unibo.scale_device.Scale_device").
 static(scale_device).
  qactor( observedactor, ctxsprintdue, "it.unibo.observedactor.Observedactor").
 static(observedactor).
