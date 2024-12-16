%====================================================================================
% wis24 description   
%====================================================================================
dispatch( startIncinerator, startIncinerator(0) ).
dispatch( startBurning, startBurning(BTIME) ).
event( burning, burning(START_TIME) ).
event( finishedBurning, finishedBurning(TIME_ELAPSED) ).
dispatch( ashMeasurement, ashMeasurement(L) ).
request( engage, engage(OWNER,STEPTIME) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
dispatch( robotpositioninfo, robotpositioninfo(X,Y) ).
%====================================================================================
context(ctxwis24, "localhost",  "TCP", "8121").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( wis, ctxwis24, "it.unibo.wis.Wis").
 static(wis).
  qactor( oprobot, ctxwis24, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( incinerator, ctxwis24, "it.unibo.incinerator.Incinerator").
 static(incinerator).
