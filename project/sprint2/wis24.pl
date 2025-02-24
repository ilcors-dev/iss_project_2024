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
context(ctxwis24_functional_test, "localhost",  "TCP", "8121").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( wis, ctxwis24_functional_test, "it.unibo.wis.Wis").
 static(wis).
  qactor( oprobot, ctxwis24_functional_test, "it.unibo.oprobot.Oprobot").
 static(oprobot).
  qactor( incinerator, ctxwis24_functional_test, "it.unibo.incinerator.Incinerator").
 static(incinerator).
  qactor( scale, ctxwis24_functional_test, "it.unibo.scale.Scale").
 static(scale).
  qactor( scale_device, ctxwis24_functional_test, "it.unibo.scale_device.Scale_device").
 static(scale_device).
  qactor( led, ctxwis24_functional_test, "it.unibo.led.Led").
 static(led).
  qactor( led_device, ctxwis24_functional_test, "it.unibo.led_device.Led_device").
 static(led_device).
  qactor( sonar, ctxwis24_functional_test, "it.unibo.sonar.Sonar").
 static(sonar).
  qactor( sonar_device, ctxwis24_functional_test, "it.unibo.sonar_device.Sonar_device").
 static(sonar_device).
