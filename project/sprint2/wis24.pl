%====================================================================================
% wis24 description   
%====================================================================================
dispatch( startIncinerator, startIncinerator(0) ).
dispatch( startBurning, startBurning(BTIME) ).
event( burning, burning(0) ).
event( finishedBurning, finishedBurning(0) ).
request( engage, engage(OWNER,STEPTIME) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
request( getrp, getrp(TARGETX,TARGETY) ).
reply( getrp_status, getrp_status(0) ).  %%for getrp
request( depositrp, depositrp(TARGETX,TARGETY) ).
reply( depositrp_status, depositrp_status(0) ).  %%for depositrp
request( extractash, extractash(TARGETX,TARGETY) ).
reply( extractash_status, extractash_status(0) ).  %%for extractash
request( depositash, depositash(TARGETX,TARGETY) ).
reply( depositash_status, depositash_status(0) ).  %%for depositash
request( gohome, gohome(TARGETX,TARGETY) ).
reply( gohome_status, gohome_status(0) ).  %%for gohome
dispatch( load_weight, load_weight(WEIGHT) ).
dispatch( unload_weight, unload_weight(WEIGHT) ).
dispatch( update_scale_count, update_scale_count(COUNT) ).
event( scale_data, scale_data(WEIGHT) ).
event( mqtt_info, mqtt_info(MSG) ).
dispatch( update_led_mode, update_led_mode(mode) ).
%====================================================================================
context(ctxwis24, "localhost",  "TCP", "8121").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmonitoringdevice, "127.0.0.1",  "TCP", "8125").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( led, ctxmonitoringdevice, "external").
  qactor( sonar, ctxmonitoringdevice, "external").
  qactor( wis, ctxwis24, "it.unibo.wis.Wis").
 static(wis).
  qactor( oprobot, ctxwis24, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( incinerator, ctxwis24, "it.unibo.incinerator.Incinerator").
 static(incinerator).
  qactor( scale, ctxwis24, "it.unibo.scale.Scale").
 static(scale).
  qactor( scale_device, ctxwis24, "it.unibo.scale_device.Scale_device").
 static(scale_device).
  qactor( mock_rp_loader_external, ctxwis24, "it.unibo.mock_rp_loader_external.Mock_rp_loader_external").
 static(mock_rp_loader_external).
