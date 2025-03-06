%====================================================================================
% wis24 description   
%====================================================================================
dispatch( startIncinerator, startIncinerator(0) ).
dispatch( startBurning, startBurning(BTIME) ).
dispatch( burning, burning(0) ).
dispatch( finishedBurning, finishedBurning(0) ).
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
dispatch( update_led_mode, update_led_mode(MODE) ).
dispatch( update_physical_led_mode, update_physical_led_mode(MODE) ).
event( sonar_data, distance(D) ).
dispatch( sonar_sensitivity, sonar_sensitivity(S) ).
dispatch( ash_measurement, ash_measurement(L) ).
dispatch( load_ash, load_ash(D) ).
event( mqtt_info, mqtt_info(MSG) ).
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxwis24, "localhost",  "TCP", "8121").
context(ctxmonitoringdevice, "127.0.0.1",  "TCP", "8122").
context(ctxscale, "127.0.0.1",  "TCP", "8123").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( scale_device, ctxscale, "external").
  qactor( led, ctxmonitoringdevice, "external").
  qactor( sonar, ctxmonitoringdevice, "external").
  qactor( sonar_device, ctxmonitoringdevice, "external").
  qactor( wis, ctxwis24, "it.unibo.wis.Wis").
 static(wis).
  qactor( oprobot, ctxwis24, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( incinerator, ctxwis24, "it.unibo.incinerator.Incinerator").
 static(incinerator).
